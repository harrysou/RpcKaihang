package com.kaihang.serviceCenter;

import com.kaihang.cache.ServiceCache;
import com.kaihang.serviceCenter.ZkWatcher.WatchZK;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements ServiceCenter{
    //curator提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRpc";
    private ServiceCache cache;

    public ZKServiceCenter() throws InterruptedException {
        //重试设置间隔1000ms，最大重试次数为3
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")//zookeeper地址固定
                .sessionTimeoutMs(40000)//超时时间
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zookeeper连接成功");
        this.cache = new ServiceCache();
        WatchZK watchZK = new WatchZK(client,cache);
        watchZK.watchToUpdate(ROOT_PATH);
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try{
            //先从本地缓存中找
            List<String> strings = cache.getServiceFromCache(serviceName);
            System.out.println("缓存信息：" + strings);
            if(strings == null){
                strings = client.getChildren().forPath( "/" + serviceName);
            }
            //默认第一个
            String string = strings.get( 0 );
            return parseAddress(string);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
