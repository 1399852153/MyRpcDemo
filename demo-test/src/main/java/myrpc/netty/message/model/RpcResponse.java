package myrpc.netty.message.model;

public class RpcResponse {

    /**
     * 消息的唯一id（占8字节）
     * */
    private long messageId;

    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;
}
