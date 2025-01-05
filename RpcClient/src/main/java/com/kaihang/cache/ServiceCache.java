package com.kaihang.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCache {
    //成员变量，存储服务名和地址列表（一个服务可能有多个地址）
    private static Map<String, List<String>> cache = new HashMap<>();
    //添加服务
    public void addServiceToCache(String serviceName, String address) {
        if(cache.containsKey(serviceName)) {
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为" + serviceName + "和地址为" + address + "的服务添加到本地缓存中");
        }else{
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName, addressList);
        }
    }
    //修改服务
    public void replaceServiceAddress(String serviceName, String oldAddress, String newAddress) {
        if(cache.containsKey(serviceName)) {
            List<String> addressList = cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        }else {
            System.out.println("修改失败，服务不存在");
        }
    }
    //获取服务对应地址
    public List<String> getServiceFromCache(String serviceName) {
        if(!cache.containsKey(serviceName)) {
            return null;
        }
        return cache.get(serviceName);
    }
    //删除地址
    public void removeServiceFromCache(String serviceName, String address) {
        List<String> addressList = cache.get(serviceName);
        if(addressList != null) {
            addressList.remove(address);
            System.out.println("将name为" + serviceName + "和地址为" + address + "的服务从本地缓存中删除");
        }
    }
}
