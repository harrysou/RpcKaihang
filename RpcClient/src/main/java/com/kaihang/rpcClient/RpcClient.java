package com.kaihang.rpcClient;

import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;

public interface RpcClient {
    //定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
