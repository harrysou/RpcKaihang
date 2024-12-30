package com.kaihang.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {//序列化设置
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] params;
}
