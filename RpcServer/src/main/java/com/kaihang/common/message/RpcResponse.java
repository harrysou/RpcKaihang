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
public class RpcResponse implements Serializable {
    private int code;
    private String msg;
    private Object data;
    private Class<?> dataType;
    public static RpcResponse success(Object data) {
        return RpcResponse.builder()
                .code(200)
                .dataType(data.getClass())
                .data(data)
                .build();
    }
    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).msg("服务器发生错误").build();
    }
}
