package myrpc.service.impl;

import myrpc.service.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello() {
        System.out.println("hello!");
    }

    @Override
    public String echo(String message) {
        System.out.println("message=" + message);
        return message;
    }
}
