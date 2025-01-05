package com.kaihang.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {
    //实现具体的地址分配算法
    String balance(List<String> addressList);
    //添加节点
    void addNode(String node);
    //删除节点
    void delNode(String node);
}
