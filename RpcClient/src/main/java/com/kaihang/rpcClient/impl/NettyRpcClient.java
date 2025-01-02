package com.kaihang.rpcClient.impl;

import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;
import com.kaihang.netty.nettyInitializer.NettyClientInitializer;
import com.kaihang.rpcClient.RpcClient;
import com.kaihang.serviceCenter.ServiceCenter;
import com.kaihang.serviceCenter.ZKServiceCenter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private ServiceCenter serviceCenter;

    public NettyRpcClient() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenter();
    }
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        //获取请求地址的代码
        InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInterfaceName());
        String host = address.getHostName();
        int port = address.getPort();
        try{
            //创建一个channelFuture对象，代表这一个操作事件，sync堵塞直到connect完成
            //bootstrap为netty的启动类
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            //返回结果后才关闭channelFuture
            channel.closeFuture().sync();
            //通过AttributeKey获取RpcRequest对象， AttributeKey用于在Channel中存储和检索特定的数据
            //通过key获取
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
            //获取储存在channel里的RpcResponse
            RpcResponse response = channel.attr(key).get();
            System.out.println(response);
            return response;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
