package com.kaihang.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] params;
}