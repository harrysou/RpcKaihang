package com.kaihang.serviceCenter.balance.impl;

import com.kaihang.serviceCenter.balance.LoadBalance;

import java.util.*;

public class ConsistencyHashBalance implements LoadBalance {
    //虚拟节点个数
    private static final int VIRTUAL_NUM = 5;
    //保存虚拟节点的hash值和对应的虚拟节点，key为hash值，value为虚拟节点
    private SortedMap<Integer,String> shards = new TreeMap<Integer,String>();
    //真实节点列表
    private List<String> realNodes = new LinkedList<String>();
    //模拟初始服务器
    private String[] servers = null;
    //初始化负载均衡器，并将真实的服务节点和对应虚拟节点添加到哈希环上
    private void init(List<String> serviceList) {
        for(String server : serviceList) {
            realNodes.add(server);
            System.out.println("真实节点[" + server + "]被添加");
            //遍历servers，每个真实节点都会生成VIRTUAL_NUM
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = server + "&&VN" + i;//虚拟节点的编号
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }
    //根据请求的node即请求类型（比如某个请求的标识符），选择一个服务器节点。
    private String getServer(String node, List<String> serviceList) {
        //首先调用init初始化serviceList
        init(serviceList);
        //getHash(node)计算请求哈希值从而得出该请求需要在哪个区间
        int hash = getHash(node);
        Integer key = null;
        //找到哈希环上第一个大于或等于请求哈希值的虚拟节点
        SortedMap<Integer, String> subMap = shards.tailMap(hash);
        if(subMap.isEmpty()){
            key = shards.lastKey();
        } else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        return virtualNode.substring(0,virtualNode.indexOf("&&VN"));
    }

    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();//模拟获取的用户ID
        return getServer(random, addressList);
    }

    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)) {
            realNodes.add(node);
            System.out.println("真实节点[" + node + "] 上线添加");
            for(int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }

    @Override
    public void delNode(String node) {
        if(realNodes.contains(node)) {
            realNodes.remove(node);
            System.out.println("真实节点[" + node + "] 下线删除");
            for(int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被删除");
            }
        }
    }

    //FNV1_32_HASH算法 + 额外位运算
    private static int getHash(String str){
        final int p = 1677619;
        int hash = (int) 2166136261L;
        for(int i = 0; i < str.length(); i++){
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if(hash < 0) hash = Math.abs(hash);
        return hash;
    }

}
