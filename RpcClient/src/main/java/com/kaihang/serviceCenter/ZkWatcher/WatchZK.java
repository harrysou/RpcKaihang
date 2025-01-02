package com.kaihang.serviceCenter.ZkWatcher;

import com.kaihang.cache.ServiceCache;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

public class WatchZK {
    //zookeeper客户端
    private CuratorFramework client;
    //服务缓存
    ServiceCache cache;

    public WatchZK(CuratorFramework client, ServiceCache cache) {
        this.client = client;
        this.cache = cache;
    }

    public void watchToUpdate(String path) throws InterruptedException {
        //Curator（zookeeper客户端）提供的一个监听节点变化的API
        CuratorCache curatorCache = CuratorCache.build(client, "/");
        //注册一个事件监听器//三个参数
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                switch (type.name()){
                    case "NODE_CREATED":
                        String[] pathList = paresPath(childData1);
                        if(pathList.length <= 2) break;//出现这种情况一般是结构有问题或者是服务名，地址信息为空
                        else{
                            String serviceName = pathList[1];
                            String address = pathList[2];
                            cache.addServiceToCache(serviceName, address);
                        }
                        break;
                    case "NODE_CHANGED":
                        if(childData.getData() != null){
                            System.out.println("修改前的数据：" + new String(childData.getData()));
                        } else {
                            System.out.println("节点第一次赋值");
                        }
                        String[] oldPathList = paresPath(childData);
                        String[] newPathList = paresPath(childData1);
                        cache.replaceServiceAddress(oldPathList[1], oldPathList[2], newPathList[2]);
                        System.out.println("修改后的数据：" + new String(childData.getData()));
                        break;
                    case "NODE_DELETED":
                        String[] pathList_delete = paresPath(childData);
                        if(pathList_delete.length <= 2) break;
                        else {
                            String serviceName = pathList_delete[1];
                            String address = pathList_delete[2];
                            cache.removeServiceFromCache(serviceName, address);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        curatorCache.start();
    }

    private String[] paresPath(ChildData childData) {
        if (childData == null || childData.getPath() == null) {
            return new String[0];
        }
        String path = childData.getPath(); // 获取节点路径
        return path.split("/"); // 按 "/" 分割路径
    }
}
