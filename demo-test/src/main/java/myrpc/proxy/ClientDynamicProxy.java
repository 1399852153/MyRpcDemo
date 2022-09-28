package myrpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端动态代理
 * */
public class ClientDynamicProxy implements InvocationHandler {

    private Object target;

    public ClientDynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("DynamicProxy before: methodName=" + method.getName());
        Object result = method.invoke(target,args);
        System.out.println("DynamicProxy after: methodName=" + method.getName());

        return result;
    }
}
