package com.kaihang.common.serializer.mySerializer;

import java.io.*;

public class ObjectSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        //创建字节数组输出流，可改变大小
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //把对象转为二进制数据，并写入字节数组
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            //转为字节数组
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        //字节数组存入输入流
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try{
            //转为类输入流
            ObjectInputStream ois = new ObjectInputStream(bis);
            //读取序列化对象
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
}
