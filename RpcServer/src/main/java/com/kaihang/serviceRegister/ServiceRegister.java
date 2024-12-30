package com.kaihang.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void registerService(String serviceName, InetSocketAddress serviceAddress);
}
