package myrpc.consumer;

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

    public Consumer(Class<?> interfaceClass, Registry registry) {
        this.interfaceClass = interfaceClass;
        this.registry = registry;

        ClientDynamicProxy clientDynamicProxy = new ClientDynamicProxy(registry);

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
