package myrpc;

import myrpc.proxy.ClientDynamicProxy;
import myrpc.service.HelloService;
import myrpc.service.impl.HelloServiceImpl;
import myrpc.service.impl.HelloServiceStaticProxyImpl;
import org.junit.Test;

import java.lang.reflect.Proxy;

public class MyTest {

    @Test
    public void testStaticProxy(){
        HelloService helloService = new HelloServiceStaticProxyImpl(new HelloServiceImpl());
        helloService.sayHello();
        String result = helloService.echo("666!");
        System.out.println("result=" + result);
    }

    @Test
    public void testDynamicProxy(){
        ClientDynamicProxy clientDynamicProxy = new ClientDynamicProxy(new HelloServiceImpl());

        HelloService helloService = (HelloService) Proxy.newProxyInstance(
                clientDynamicProxy.getClass().getClassLoader(),HelloServiceImpl.class.getInterfaces(), clientDynamicProxy);
        helloService.sayHello();
        String result = helloService.echo("666!");
        System.out.println("result=" + result);
    }
}
