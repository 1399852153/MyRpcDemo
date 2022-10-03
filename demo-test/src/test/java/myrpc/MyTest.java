package myrpc;

import myrpc.proxy.ClientDynamicProxy;
import service.HelloService;
import org.junit.Test;

import java.lang.reflect.Proxy;

public class MyTest {

    @Test
    public void testDynamicProxy(){
        ClientDynamicProxy clientDynamicProxy = new ClientDynamicProxy();

        HelloService helloService = (HelloService) Proxy.newProxyInstance(
                clientDynamicProxy.getClass().getClassLoader(),new Class[]{HelloService.class}, clientDynamicProxy);
        helloService.sayHello();
        String result = helloService.echo("666!");
        System.out.println("result=" + result);
    }
}
