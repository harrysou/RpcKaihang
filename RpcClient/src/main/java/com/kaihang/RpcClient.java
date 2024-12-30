package com.kaihang;

import com.kaihang.model.User;
import com.kaihang.proxy.ClientProxy;
import com.kaihang.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class RpcClient {
    public static void main( String[] args ) {
        ClientProxy clientProxy = new ClientProxy();
//        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 9999,0);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user=" + user.toString());

        User u = User.builder().id(100).userName("skh").sex(true).build();
        Integer id = proxy.insertUser(u);
        System.out.println("向服务端插入user的id" + id);
    }
}
