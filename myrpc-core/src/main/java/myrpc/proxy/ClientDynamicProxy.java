package myrpc.proxy;

import io.netty.channel.Channel;
import myrpc.balance.LoadBalance;
import myrpc.common.JsonUtil;
import myrpc.common.ServiceInfo;
import myrpc.exchange.DefaultFuture;
import myrpc.exchange.DefaultFutureManager;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 客户端动态代理
 * */
public class ClientDynamicProxy implements InvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(ClientDynamicProxy.class);

    private Registry registry;
    private LoadBalance loadBalance;

    public ClientDynamicProxy(Registry registry) {
        this.registry = registry;
    }

    public ClientDynamicProxy(Registry registry, LoadBalance loadBalance) {
        this.registry = registry;
        this.loadBalance = loadBalance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object localMethodResult = processLocalMethod(proxy,method,args);
        if(localMethodResult != null){
            // 处理toString等对象自带方法，不发起rpc调用
            return localMethodResult;
        }

        logger.info("ClientDynamicProxy before: methodName=" + method.getName());

        String serviceName = method.getDeclaringClass().getName();
        List<ServiceInfo> serviceInfoList = registry.discovery(serviceName);
        logger.info("serviceInfoList.size={},serviceInfoList={}",serviceInfoList.size(),JsonUtil.obj2Str(serviceInfoList));

        // 负载均衡获得调用的服务端
        ServiceInfo selectedServiceInfo = loadBalance.select(serviceInfoList);
        NettyClient nettyClient = NettyClientFactory.getNettyClient(selectedServiceInfo.getUrlAddress());

        // 构造请求和协议头
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterClasses(method.getParameterTypes());
        rpcRequest.setParams(args);

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageFlag(MessageFlagEnums.REQUEST.getCode());
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(true);
        messageHeader.setSerializeType(MessageSerializeType.JSON.getCode());
        messageHeader.setResponseStatus((byte)'a');
        messageHeader.setMessageId(rpcRequest.getMessageId());

        logger.info("ClientDynamicProxy rpcRequest={}", JsonUtil.obj2Str(rpcRequest));

        Channel channel = nettyClient.getChannel();
        // 通过Promise，将netty的异步转为同步,参考dubbo DefaultFuture
        DefaultFuture<RpcResponse> defaultFuture = DefaultFutureManager.createNewFuture(channel,rpcRequest);

        nettyClient.send(new MessageProtocol<>(messageHeader,rpcRequest));

        logger.info("ClientDynamicProxy writeAndFlush success, wait result");

        // 调用方阻塞在这里
        RpcResponse result = defaultFuture.get();

        logger.info("ClientDynamicProxy defaultFuture.get() result={}",result);

        return result.getReturnValue();
    }

    private Object processLocalMethod(Object proxy, Method method, Object[] args) throws Exception {
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

        // 返回null标识非本地方法，需要进行rpc调用
        return null;
    }
}
