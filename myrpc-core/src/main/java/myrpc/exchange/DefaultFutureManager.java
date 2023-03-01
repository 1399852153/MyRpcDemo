package myrpc.exchange;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.message.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DefaultFutureManager {

    public static final Map<Long,DefaultFuture> DEFAULT_FUTURE_CACHE = new ConcurrentHashMap<>();
    public static final HashedWheelTimer TIMER = new HashedWheelTimer();

    public static void received(RpcResponse rpcResponse){
        Long messageId = rpcResponse.getMessageId();

        DefaultFuture defaultFuture = DEFAULT_FUTURE_CACHE.remove(messageId);

        if(defaultFuture != null){
            if(rpcResponse.getExceptionValue() != null){
                // 异常处理
                defaultFuture.completeExceptionally(rpcResponse.getExceptionValue());
            }else{
                // 正常返回
                defaultFuture.complete(rpcResponse);
            }
        }
    }

    public static DefaultFuture createNewFuture(Channel channel, RpcRequest rpcRequest){
        DefaultFuture defaultFuture = new DefaultFuture(channel,rpcRequest);
        // 增加超时处理的逻辑
        newTimeoutCheck(defaultFuture);

        return defaultFuture;
    }

    public static DefaultFuture getFuture(long messageId){
        return DEFAULT_FUTURE_CACHE.get(messageId);
    }

    /**
     * 增加请求超时的检查任务
     * */
    public static void newTimeoutCheck(DefaultFuture defaultFuture){
        TimeoutCheckTask timeoutCheckTask = new TimeoutCheckTask(defaultFuture.getMessageId());
        TIMER.newTimeout(timeoutCheckTask,defaultFuture.getTimeout(),TimeUnit.MILLISECONDS);
    }
}
