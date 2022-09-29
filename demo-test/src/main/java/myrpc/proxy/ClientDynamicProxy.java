package myrpc.proxy;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import myrpc.common.URLAddress;
import myrpc.netty.client.LocalRpcResponseCache;
import myrpc.netty.client.NettyClient;
import myrpc.netty.client.NettyClientFactory;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.message.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * 客户端动态代理
 * */
public class ClientDynamicProxy implements InvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(ClientDynamicProxy.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("DynamicProxy before: methodName=" + method.getName());

        // 服务端信息暂时写死，后续从注册中心中获取
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;
        NettyClient nettyClient = NettyClientFactory.getNettyClient(new URLAddress(serverAddress,port));

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterClasses(method.getParameterTypes());
        rpcRequest.setParams(args);

        // 通过Promise，将netty的异步转为同步
//        Promise<MessageProtocol<RpcResponse>> responsePromise = new DefaultPromise<>(LocalRpcResponseCache.executorService);

        nettyClient.getChannel().writeAndFlush(rpcRequest).sync();

        System.out.println("DynamicProxy after: methodName=" + method.getName());

//        MessageProtocol<RpcResponse> response = responsePromise.get();
        return null;
    }
}
