package com.kaihang.retry;

import com.github.rholder.retry.*;
import com.kaihang.common.message.RpcRequest;
import com.kaihang.common.message.RpcResponse;
import com.kaihang.rpcClient.RpcClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {
    private RpcClient rpcClient;

    public RpcResponse sendServiceWithRetry(RpcRequest request, RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfException()
                //状态码为500时执行
                .retryIfResult(response -> Objects.equals(response.getCode(), 500))
                //每次重试固定等待2秒
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //最大重试次数为3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener:第" + attempt.getAttemptNumber() + "次调用");
                    }
                })
                .build();
        try{
            return retryer.call(() -> this.rpcClient.sendRequest(request));
        }catch (Exception e){
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}
