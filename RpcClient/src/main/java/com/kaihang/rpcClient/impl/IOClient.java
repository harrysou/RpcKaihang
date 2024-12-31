package com.kaihang.rpcClient.impl;

import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;
import com.kaihang.rpcClient.RpcClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient implements RpcClient {
    private String host;
    private int port;
    public IOClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    @Override
    //负责底层与服务器的通信，发送request，返回response
    public RpcResponse sendRequest(RpcRequest request) {
        try{
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(request);
            oos.flush();
            return (RpcResponse) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
