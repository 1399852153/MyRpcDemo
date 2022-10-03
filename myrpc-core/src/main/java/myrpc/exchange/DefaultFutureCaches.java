package myrpc.exchange;

import myrpc.netty.message.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFutureCaches {

    public static final Map<Long,DefaultFuture> defaultFutureCache = new ConcurrentHashMap<>();

    public static void received(RpcResponse rpcResponse){
        Long messageId = rpcResponse.getMessageId();

        DefaultFuture defaultFuture = defaultFutureCache.remove(messageId);

        if(defaultFuture != null){
            defaultFuture.complete(rpcResponse);
        }
    }
}
