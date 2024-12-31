package com.kaihang.common.serializer.mySerializer;

//接口下有不同的实现类为静态工厂设计模式
public interface Serializer {
    //序列化为字节数组
    byte[] serialize(Object obj);
    //反序列化为Java对象（根据字节数组和消息类型）
    Object deserialize(byte[] bytes, int messageType);
    int getType();

    static Serializer getSerializer(int code) {
        return switch (code) {
            //Java自带方式
            case 0 -> new ObjectSerializer();
            //Json序列化方式
            case 1 -> new JsonSerializer();
            default -> null;
        };
    }
}
