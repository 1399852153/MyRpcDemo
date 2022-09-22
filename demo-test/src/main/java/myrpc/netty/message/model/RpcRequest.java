package myrpc.netty.message.model;

import java.lang.reflect.Type;

/**
 * rpc请求对象
 * */
public class RpcRequest {

    /**
     * 接口名
     * */
    private String interfaceName;

    /**
     * 方法名
     * */
    private String methodName;

    /**
     * 参数类型数组(每个参数一项)
     * */
    private Class<?>[] parameterClasses;

    /**
     * 实际参数对象数组(每个参数一项)
     * */
    private Object[] params;

    /**
     * 返回值类型
     * */
    private Class<?> returnClass;

    /**
     * 返回值泛型类型
     * */
    private Type[] returnType;


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public void setParameterClasses(Class<?>[] parameterClasses) {
        this.parameterClasses = parameterClasses;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    public Type[] getReturnType() {
        return returnType;
    }

    public void setReturnType(Type[] returnType) {
        this.returnType = returnType;
    }
}
