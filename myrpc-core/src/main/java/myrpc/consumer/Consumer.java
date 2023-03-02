package myrpc.consumer;

import myrpc.balance.LoadBalance;
import myrpc.proxy.ClientDynamicProxy;
import myrpc.registry.Registry;

import java.lang.reflect.Proxy;

/**
 * consumer
 * @author shanreng
 */
public class Consumer<T> {

    private Class<?> interfaceClass;
    private T proxy;
    private Registry registry;


    public Consumer(Class<?> interfaceClass, Registry registry, LoadBalance loadBalance) {
        this.interfaceClass = interfaceClass;
        this.registry = registry;

        ClientDynamicProxy clientDynamicProxy = new ClientDynamicProxy(registry,loadBalance);

        this.proxy = (T) Proxy.newProxyInstance(
                clientDynamicProxy.getClass().getClassLoader(),
                new Class[]{interfaceClass},
                clientDynamicProxy);
    }

    public T getProxy() {
        return proxy;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public Registry getRegistry() {
        return registry;
    }
}
