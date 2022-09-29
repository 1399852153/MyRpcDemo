package myrpc.proxy;

import myrpc.common.URLAddress;
import myrpc.netty.client.NettyClient;
import myrpc.netty.client.NettyClientFactory;
import myrpc.netty.message.model.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * 客户端动态代理
 * */
public class ClientDynamicProxy implements InvocationHandler {

    private Object target;

    public ClientDynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("DynamicProxy before: methodName=" + method.getName());

        // 服务端信息暂时写死，后续从注册中心中获取
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;
        NettyClient nettyClient = NettyClientFactory.getNettyClient(new URLAddress(serverAddress,port));

        RpcRequest rpcRequest = new RpcRequest();
//        rpcRequest.set

//        nettyClient.getChannel()

        Object result = method.invoke(target,args);
        System.out.println("DynamicProxy after: methodName=" + method.getName());

        return result;
    }
}
