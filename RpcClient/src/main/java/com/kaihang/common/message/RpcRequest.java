package com.kaihang.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor//fastjson需要获取构造方法
public class RpcRequest implements Serializable {//序列化设置
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] params;
}
