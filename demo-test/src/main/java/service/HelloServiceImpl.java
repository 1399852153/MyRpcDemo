package service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloServiceImpl implements HelloService{

    @Override
    public void sayHello() {
        System.out.println("HelloService sayHello!");
    }

    @Override
    public String echo(String message) {
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
}
