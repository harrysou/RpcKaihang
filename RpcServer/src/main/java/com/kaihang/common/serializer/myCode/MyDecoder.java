package com.kaihang.common.serializer.myCode;

import com.kaihang.common.message.MessageType;
import com.kaihang.common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short messageType = in.readShort();
        if(messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种数据");
        }
        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializer(serializerType);
        if(serializer == null){
            throw new RuntimeException("不存在对应序列化器");
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        //反序列化
        Object deserialize = serializer.deserialize(bytes, messageType);
        out.add(deserialize);
    }
}
