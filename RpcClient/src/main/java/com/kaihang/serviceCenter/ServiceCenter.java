package com.kaihang.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {
    //根据服务名查找地址
    InetSocketAddress serviceDiscovery(String serviceName);
}
