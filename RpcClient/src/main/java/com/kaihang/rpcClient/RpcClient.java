package com.kaihang.rpcClient;

import com.kaihang.common.RpcRequest;
import com.kaihang.common.RpcResponse;

public interface RpcClient {
    //定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
