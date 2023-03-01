package myrpc.netty;

import myrpc.consumer.Consumer;
import myrpc.exception.MyRpcTimeoutException;
import myrpc.proxy.ClientDynamicProxy;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
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
        Registry registry = RegistryFactory.getRegistry(
                new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));

        Consumer<HelloService> consumer = new Consumer<>(HelloService.class,registry);
        HelloService helloService = consumer.getProxy();

        String result = helloService.echo("666!");
        logger.info("echo result=" + result);

        Map<String, List<Integer>> resultMap = helloService.testGeneric2();
        logger.info("testGeneric result=" + resultMap);

        try {
            helloService.testTimeout();
        }catch (Exception exception){
            if(exception.getCause().getCause() instanceof MyRpcTimeoutException){
                logger.info("testTimeout success!");
            }
        }

        logger.info("client demo永久阻塞");
        LockSupport.park();
    }
}
