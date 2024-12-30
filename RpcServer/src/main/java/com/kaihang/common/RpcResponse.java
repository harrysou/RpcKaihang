package com.kaihang.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcResponse implements Serializable {
    private int code;
    private String msg;
    private Object data;
    public static RpcResponse success(Object data) {
        return RpcResponse.builder().code(200).data(data).build();
    }
    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).msg("服务器发生错误").build();
    }
}
