package com.kaihang.netty.nettyInitializer;

import com.kaihang.common.serializer.myCode.MyDecoder;
import com.kaihang.common.serializer.myCode.MyEncoder;
import com.kaihang.common.serializer.mySerializer.JsonSerializer;
import com.kaihang.netty.handler.NettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

//解决沾包问题
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
//无自定义解码器和编码器前的沾包如何解决
//        //消息格式【长度】【消息体】，解决沾包问题//计算当前待发送消息的长度，写入到前4个字节
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//        //使用Java序列化方式，netty的自带的解码编码支持传输这种结构
//        pipeline.addLast(new LengthFieldPrepender(4));
//        //将字节流解码为Java对象
//        pipeline.addLast(new ObjectEncoder());
//        //解析对象类名并加载相应的类
//        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
//            @Override
//            public Class<?> resolve(String s) throws ClassNotFoundException {
//                return Class.forName(s);
//            }
//        }));
}
