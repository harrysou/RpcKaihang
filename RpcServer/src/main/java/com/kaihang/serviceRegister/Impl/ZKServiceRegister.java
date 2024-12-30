package com.kaihang.serviceRegister.Impl;

import com.kaihang.serviceRegister.ServiceRegister;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;

public class ZKServiceRegister implements ServiceRegister {
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRpc";

    public ZKServiceRegister(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(retryPolicy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zookeeper连接成功");
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress serviceAddress) {
        try{
            if(client.checkExists().forPath("/" + serviceName) == null){
                //creating parents if needed同时创建父类节点若不存在
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
                String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("此服务已存在");
        }
    }

    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort();
    }

    private InetSocketAddress getServiceAddress(String address){
        String[] split = address.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
