package myrpc.netty;

import myrpc.proxy.ClientDynamicProxy;
import service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

public class NettyClientDemo {

    private static Logger logger = LoggerFactory.getLogger(NettyClientDemo.class);

    public static void main(String[] args){
        ClientDynamicProxy clientDynamicProxy = new ClientDynamicProxy();

        HelloService helloService = (HelloService) Proxy.newProxyInstance(
                clientDynamicProxy.getClass().getClassLoader(),new Class[]{HelloService.class}, clientDynamicProxy);
        String result = helloService.echo("666!");
        logger.info("echo result=" + result);

        Map<String, List<Integer>> resultMap = helloService.testGeneric2();
        logger.info("testGeneric result=" + resultMap);

        logger.info("client demo永久阻塞");
        LockSupport.park();
    }
}
