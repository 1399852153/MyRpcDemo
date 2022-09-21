package myrpc.netty.message.enums;

/**
 * 消息序列化方式
 * @author shanreng
 */
public enum MessageSerializeType {
    /**
     * 消息序列化方式
     * */
    JSON(new byte[]{0,0,0,0,1},"json"),
    HESSIAN(new byte[]{0,0,0,0,2},"hessian"),
    ;

    MessageSerializeType(byte[] code, String type) {
        this.code = code;
        this.type = type;
    }

    private byte[] code;
    private String type;
}
