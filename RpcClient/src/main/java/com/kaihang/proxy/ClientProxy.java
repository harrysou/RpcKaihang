package com.kaihang.proxy;

import com.kaihang.rpcClient.impl.IOClient;
import com.kaihang.common.RpcRequest;
import com.kaihang.common.RpcResponse;
import com.kaihang.rpcClient.RpcClient;
import com.kaihang.rpcClient.impl.NettyRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;
    public ClientProxy(String host, int port, int choose) {
        switch (choose){
            case 0:
                rpcClient = new NettyRpcClient(host, port);
                break;
            case 1:
                rpcClient =  new IOClient(host, port);
        }
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
        RpcResponse response = rpcClient.sendRequest(request);
        assert response != null;
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}