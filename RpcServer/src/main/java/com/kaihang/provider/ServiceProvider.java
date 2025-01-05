package com.kaihang.provider;

import com.kaihang.serviceRegister.Impl.ZKServiceRegister;
import com.kaihang.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    //集合中存放服务的实例
    private Map<String, Object> interfaceProvider;
    private Integer port;
    private String host;
    private ServiceRegister serviceRegister;
    public ServiceProvider(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
    }
    //本地注册服务
    public void provideServiceInterface(Object service, boolean canRetry){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaceName) {
            interfaceProvider.put(clazz.getName(), service);
            serviceRegister.registerService(clazz.getName(), new InetSocketAddress(host, port), canRetry);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
