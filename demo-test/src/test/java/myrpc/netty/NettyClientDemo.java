package myrpc.netty;

import myrpc.balance.RandomLoadBalance;
import myrpc.consumer.Consumer;
import myrpc.consumer.ConsumerBootstrap;
import myrpc.exception.MyRpcTimeoutException;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HelloService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

public class NettyClientDemo {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientDemo.class);

    public static void main(String[] args){
        Registry registry = RegistryFactory.getRegistry(
                new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));

        ConsumerBootstrap consumerBootstrap = new ConsumerBootstrap()
            .registry(registry)
            // 随机调用
            .loadBalance(new RandomLoadBalance())
            .init();

        // 注册消费者
        Consumer<HelloService> consumer = consumerBootstrap.registerConsumer(HelloService.class);
        HelloService helloService = consumer.getProxy();

        String result = helloService.echo("666!");
        logger.info("echo result=" + result);

        Map<String, List<Integer>> resultMap = helloService.testGeneric2();
        logger.info("testGeneric result=" + resultMap);

        try {
            helloService.testTimeout();
        }catch (Exception exception){
            if(exception.getCause().getCause() instanceof MyRpcTimeoutException){
                logger.info("testTimeout success!",exception.getCause().getCause());
            }
        }

        logger.info("client demo永久阻塞");
        LockSupport.park();
    }
}
