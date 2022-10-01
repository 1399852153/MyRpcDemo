package myrpc.exchange;

import io.netty.channel.Channel;
import myrpc.netty.message.model.RpcRequest;

import java.util.concurrent.CompletableFuture;

/**
 * 模仿dubbo DefaultFuture
 * 注意：暂时不考虑timeout等异常场景
 * */
public class DefaultFuture<T> extends CompletableFuture<T> {

    private Channel channel;
    private RpcRequest rpcRequest;

    public DefaultFuture(Channel channel, RpcRequest rpcRequest) {
        this.channel = channel;
        this.rpcRequest = rpcRequest;

        // 把当前future放入全局缓存中
        DefaultFutureCaches.defaultFutureCache.put(rpcRequest.getMessageId(),this);
    }
}
