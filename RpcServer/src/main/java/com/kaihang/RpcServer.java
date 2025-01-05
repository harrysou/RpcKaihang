package com.kaihang;

import com.kaihang.server.ServerInterface;
import com.kaihang.server.impl.NettyRPCServer;
import com.kaihang.server.impl.SimpleRPCRPCServer;
import com.kaihang.provider.ServiceProvider;
import com.kaihang.service.UserService;
import com.kaihang.service.serviceImpl.UserServiceImpl;

/**
 * Hello world!
 *
 */
public class RpcServer {
    public static void main( String[] args ) {
        UserService userService = new UserServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 9999);
        serviceProvider.provideServiceInterface(userService, true);
        ServerInterface rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
