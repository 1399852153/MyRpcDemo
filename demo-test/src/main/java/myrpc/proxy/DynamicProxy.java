package myrpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理，相比于静态代理
 * */
public class DynamicProxy implements InvocationHandler {

    private Object target;

    public DynamicProxy(Object target) {
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
