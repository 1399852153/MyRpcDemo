package myrpc.netty;

import myrpc.balance.SimpleRoundRobinBalance;
import myrpc.consumer.Consumer;
import myrpc.consumer.ConsumerBootstrap;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HelloService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class NettyClientDemo2 {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientDemo2.class);

    public static void main(String[] args) {
        Registry registry = RegistryFactory.getRegistry(
                new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));

        ConsumerBootstrap consumerBootstrap = new ConsumerBootstrap()
                .registry(registry)
                .loadBalance(new SimpleRoundRobinBalance())
                .init();

        // 注册消费者
        Consumer<HelloService> consumer = consumerBootstrap.registerConsumer(HelloService.class);
        HelloService helloService = consumer.getProxy();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0; i<2; i++) {
            executorService.execute(() -> {
                String result = helloService.echo("666!");
                logger.info("echo result=" + result);
            });
        }

        logger.info("client demo永久阻塞");
        LockSupport.park();
    }
}
