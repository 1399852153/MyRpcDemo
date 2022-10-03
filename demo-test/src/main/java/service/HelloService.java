package service;

import java.util.List;
import java.util.Map;

public interface HelloService {

    void sayHello();

    String echo(String message);

    Map<String,Integer> testGeneric();

    Map<String, List<Integer>> testGeneric2();
}
