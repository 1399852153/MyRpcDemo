package service;

import myrpc.exchange.DefaultFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloServiceImpl implements HelloService{

    private static Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public void sayHello() {
        logger.info("HelloService sayHello!");
    }

    @Override
    public String echo(String message) {
        System.out.println("HelloService echo!");
        return "server echo: " + message;
    }

    @Override
    public Map<String, Integer> testGeneric() {
        HashMap<String,Integer> resultMap = new HashMap<>();
        resultMap.put("aaa",111);
        resultMap.put("bbb",222);
        resultMap.put("ccc",333);
        return resultMap;
    }

    @Override
    public Map<String, List<Integer>> testGeneric2() {
        HashMap<String,List<Integer>> resultMap = new HashMap<>();
        resultMap.put("aaa", Arrays.asList(1,2,3));
        resultMap.put("bbb",Arrays.asList(2,2,2));
        resultMap.put("ccc",Arrays.asList(3,2,3));
        return resultMap;
    }

    @Override
    public void testTimeout() {
        logger.info("server testTimeout by sleep start");
        try {
            // 休眠，让客户端超时
            Thread.sleep(DefaultFuture.DEFAULT_TIME_OUT * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("server testTimeout by sleep end");
    }
}
