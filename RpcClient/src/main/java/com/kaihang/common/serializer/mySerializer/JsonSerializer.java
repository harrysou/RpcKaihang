package com.kaihang.common.serializer.mySerializer;

import com.alibaba.fastjson.JSONObject;
import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        //序列化只需直接转换为字节数组
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                //字节数组转为request对象
                RpcRequest request = JSONObject.parseObject(bytes, RpcRequest.class);
                Object[] objects = new Object[request.getParameterTypes().length];
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramsType = request.getParameterTypes()[i];
                    //如果类型兼容直接赋值否则进行类型转换（硬转换）
                    if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i], request.getParameterTypes()[i]);
                    }else {
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                RpcResponse response = JSONObject.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getDataType();
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(), dataType));
                }
                //这里返回的object是单一类型如User不需要objects数组
                obj = response;
                break;
            default:
                System.out.println("暂时不支持此类消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
