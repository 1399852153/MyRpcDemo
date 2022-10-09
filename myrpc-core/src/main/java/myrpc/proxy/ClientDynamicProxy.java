package myrpc.proxy;

import io.netty.channel.Channel;
import myrpc.common.JsonUtil;
import myrpc.common.ServiceInfo;
import myrpc.exchange.DefaultFuture;
import myrpc.netty.client.NettyClient;
import myrpc.netty.client.NettyClientFactory;
import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.message.model.RpcResponse;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 客户端动态代理
 * */
public class ClientDynamicProxy implements InvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(ClientDynamicProxy.class);

    private static Registry registry = RegistryFactory.getRegistry(
            new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理toString等对象自带方法，不发起rpc调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(proxy, args);
        }
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            if ("toString".equals(methodName)) {
                return proxy.toString();
            } else if ("hashCode".equals(methodName)) {
                return proxy.hashCode();
            }
        } else if (parameterTypes.length == 1 && "equals".equals(methodName)) {
            return proxy.equals(args[0]);
        }

        logger.info("ClientDynamicProxy before: methodName=" + method.getName());

        String serviceName = method.getDeclaringClass().getName();
        List<ServiceInfo> serviceInfoList = registry.discovery(serviceName);
        // 暂时get(0)写死，后续引入负载均衡
        NettyClient nettyClient = NettyClientFactory.getNettyClient(serviceInfoList.get(0).getUrlAddress());

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterClasses(method.getParameterTypes());
        rpcRequest.setParams(args);

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageFlag(MessageFlagEnums.REQUEST.getCode());
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(true);
        messageHeader.setSerializeType(MessageSerializeType.HESSIAN.getCode());
        messageHeader.setResponseStatus((byte)'a');
        messageHeader.setMessageId(rpcRequest.getMessageId());

        logger.info("ClientDynamicProxy rpcRequest={}", JsonUtil.obj2Str(rpcRequest));

        Channel channel = nettyClient.getChannel();
        // 通过Promise，将netty的异步转为同步,参考dubbo DefaultFuture
        DefaultFuture<RpcResponse> defaultFuture = new DefaultFuture<>(channel,rpcRequest);

        nettyClient.send(new MessageProtocol<>(messageHeader,rpcRequest));

        logger.info("ClientDynamicProxy writeAndFlush success, wait result");

        RpcResponse result = defaultFuture.get();

        logger.info("ClientDynamicProxy defaultFuture.get() result={}",result);

        return result.getReturnValue();
    }
}
