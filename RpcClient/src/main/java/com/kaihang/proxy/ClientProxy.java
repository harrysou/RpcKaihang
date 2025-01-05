package com.kaihang.proxy;

import com.kaihang.retry.GuavaRetry;
import com.kaihang.rpcClient.impl.IOClient;
import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;
import com.kaihang.rpcClient.RpcClient;
import com.kaihang.rpcClient.impl.NettyRpcClient;
import com.kaihang.serviceCenter.ServiceCenter;
import com.kaihang.serviceCenter.ZKServiceCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    public ClientProxy(String host, int port, int choose) throws InterruptedException {
        switch (choose){
            case 0:
                rpcClient = new NettyRpcClient();
                break;
            case 1:
                rpcClient =  new IOClient(host, port);
        }
    }
    public ClientProxy() throws InterruptedException{
        rpcClient = new NettyRpcClient();
        serviceCenter = new ZKServiceCenter();
    }

    @Override//jdk动态代理
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .parameterTypes(method.getParameterTypes())
                .build();
        //数据传输
//        RpcResponse response = rpcClient.sendRequest(request);
//        assert response != null;
        RpcResponse response;
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            response = new GuavaRetry().sendServiceWithRetry(request,rpcClient);
        } else {
            response = rpcClient.sendRequest(request);
        }
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
