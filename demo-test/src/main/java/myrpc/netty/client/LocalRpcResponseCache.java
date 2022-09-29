package myrpc.netty.client;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Promise;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  请求和响应映射对象
 * @Author: changjiu.wang
 * @Date: 2021/7/25 15:17
 */
public class LocalRpcResponseCache {

    private static Map<Long, Promise<MessageProtocol<RpcResponse>>> requestResponseCache = new ConcurrentHashMap<>();

    public static final ExecutorService executorService = Executors.newCachedThreadPool(
            new DefaultThreadFactory("client-response-promise", true));

    /**
     *  添加请求和响应的映射关系
     * @param reqId
     * @param promise
     */
    public static void add(long reqId, Promise<MessageProtocol<RpcResponse>> promise){
        requestResponseCache.put(reqId, promise);
    }

    /**
     *  设置响应数据
     * @param reqId
     * @param messageProtocol
     */
    public static void fillResponse(long reqId, MessageProtocol<RpcResponse> messageProtocol){
        // 获取缓存中的 future
        Promise<MessageProtocol<RpcResponse>> promise = requestResponseCache.get(reqId);

        // 设置数据
        promise.setSuccess(messageProtocol);

        // 移除缓存
        requestResponseCache.remove(reqId);
    }
}
