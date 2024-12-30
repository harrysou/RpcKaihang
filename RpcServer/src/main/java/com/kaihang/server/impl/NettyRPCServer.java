package com.kaihang.server.impl;

import com.kaihang.netty.nettyInitializer.NettyServerInitializer;
import com.kaihang.provider.ServiceProvider;
import com.kaihang.server.ServerInterface;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyRPCServer implements ServerInterface {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();//建立连接
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//负责具体的请求
        System.out.println("netty服务端启动了");
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
          bossGroup.shutdownGracefully();
          workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
