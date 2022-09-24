package myrpc.netty.message.model;

import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;

public class MessageHeader {

    public static final int MESSAGE_HEADER_LENGTH = 16;
    public static final int MESSAGE_SERIALIZE_TYPE_LENGTH = 5;
    public static final byte MAGIC_HIGH = (byte)0x22;
    public static final byte MAGIC_LOW = (byte)0x33;
    public static final byte[] MAGIC = new byte[]{MAGIC_HIGH,MAGIC_LOW};


    // ================================ 消息头 =================================
    /**
     * 魔数(占2字节)
     * */
    private byte[] magicNumber = MAGIC;

    /**
     * 消息标识(0代表请求事件；1代表响应事件， 占1位)
     * @see MessageFlagEnums
     * */
    private boolean messageFlag;

    /**
     * 是否是双向请求(0代表oneWay请求；1代表twoWay请求）
     * （双向代表客户端会等待服务端的响应，单向则请求发送完成后即向上层返回成功)
     * */
    private boolean twoWayFlag;

    /**
     * 是否是心跳消息(0代表正常消息；1代表心跳消息， 占1位)
     * */
    private boolean eventFlag;

    /**
     * 消息体序列化类型(占5位，即所支持的序列化类型不得超过2的5次方，32种)
     * @see MessageSerializeType
     * */
    private Boolean[] serializeType;

    /**
     * 响应状态(占1字节)
     * */
    private byte responseStatus;

    /**
     * 消息的唯一id（占8字节）
     * */
    private long messageUUId;

    /**
     * 业务数据长度（占4字节）
     * */
    private int bizDataLength;

    public byte[] getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(byte[] magicNumber) {
        this.magicNumber = magicNumber;
    }

    public boolean isMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(boolean messageFlag) {
        this.messageFlag = messageFlag;
    }

    public boolean isTwoWayFlag() {
        return twoWayFlag;
    }

    public void setTwoWayFlag(boolean twoWayFlag) {
        this.twoWayFlag = twoWayFlag;
    }

    public boolean isEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(boolean eventFlag) {
        this.eventFlag = eventFlag;
    }

    public Boolean[] getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(Boolean[] serializeType) {
        this.serializeType = serializeType;
    }

    public byte getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(byte responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getMessageUUId() {
        return messageUUId;
    }

    public void setMessageUUId(long messageUUId) {
        this.messageUUId = messageUUId;
    }

    public int getBizDataLength() {
        return bizDataLength;
    }

    public void setBizDataLength(int bizDataLength) {
        this.bizDataLength = bizDataLength;
    }
}
