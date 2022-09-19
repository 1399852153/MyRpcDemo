package myrpc.service.impl;

import myrpc.service.HelloService;

/**
 * static proxy静态代理
 * 用于举个例子
 * */
public class HelloServiceStaticProxyImpl implements HelloService {

    private final HelloService target;

    public HelloServiceStaticProxyImpl(HelloService target) {
        this.target = target;
    }

    @Override
    public void sayHello() {
        System.out.println("static proxy: before sayHello");
        target.sayHello();
        System.out.println("static proxy: after sayHello");
    }

    @Override
    public String echo(String message) {
        System.out.println("static proxy: before sayHello");
        String result = target.echo(message);
        System.out.println("static proxy: before sayHello");
        return result;
    }
}
