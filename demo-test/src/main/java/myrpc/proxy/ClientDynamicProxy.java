package myrpc.proxy;

import io.netty.channel.Channel;
import myrpc.common.JsonUtil;
import myrpc.common.URLAddress;
import myrpc.exchange.DefaultFuture;
import myrpc.netty.client.NettyClient;
import myrpc.netty.client.NettyClientFactory;
import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
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
        logger.info("ClientDynamicProxy before: methodName=" + method.getName());

        // 服务端信息暂时写死，后续从注册中心中获取
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;
        NettyClient nettyClient = NettyClientFactory.getNettyClient(new URLAddress(serverAddress,port));

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageFlag(MessageFlagEnums.REQUEST.getCode());
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(true);
        messageHeader.setSerializeType(MessageSerializeType.HESSIAN.getCode());
        messageHeader.setResponseStatus((byte)'a');
        messageHeader.setMessageId(123456789L);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterClasses(method.getParameterTypes());
        rpcRequest.setParams(args);

        logger.info("ClientDynamicProxy rpcRequest={}", JsonUtil.obj2Str(rpcRequest));

        Channel channel = nettyClient.getChannel();
        // 通过Promise，将netty的异步转为同步,参考dubbo DefaultFuture
        DefaultFuture defaultFuture = new DefaultFuture(channel,rpcRequest);

        channel.writeAndFlush(new MessageProtocol<>(messageHeader,rpcRequest)).sync();

        logger.info("ClientDynamicProxy writeAndFlush success, wait result");

        Object result = defaultFuture.get();

        logger.info("ClientDynamicProxy defaultFuture.get() result={}",result);

        return result;
    }
}
