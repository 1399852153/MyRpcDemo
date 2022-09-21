package myrpc.netty.message.util;

import myrpc.common.GlobalConstants;
import myrpc.common.JsonUtil;
import myrpc.netty.message.Message;

import java.io.*;


public class MessageCodecUtil {

    public static byte[] messageEncode(Message message) throws IOException {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream(Message.MESSAGE_HEADER_LENGTH);
        DataOutputStream dos = new DataOutputStream(byteArrayInputStream);

        // 写入魔数
        dos.write(Message.MAGIC_HIGH);
        dos.write(Message.MAGIC_LOW);

        // 写入消息标识
        dos.writeBoolean(message.getMessageFlag());
        // 写入单/双向标识
        dos.writeBoolean(message.getTwoWayFlag());
        // 写入消息事件标识
        dos.writeBoolean(message.getEventFlag());
        // 写入序列化类型
        for(boolean b : message.getSerializeType()){
            dos.writeBoolean(b);
        }
        // 写入响应状态
        dos.writeByte(message.getResponseStatus());
        // 写入消息uuid
        dos.writeLong(message.getMessageUUId());

        // todo 暂时写死json序列化，后续再抽象
        byte[] bizMessageBytes = JsonUtil.obj2Str(message.getBizData()).getBytes(GlobalConstants.DEFAULT_CHARSET);
        // 写入消息正文长度
        dos.write(bizMessageBytes.length);
        // 写入消息正文内容
        dos.write(bizMessageBytes);

        return byteArrayInputStream.toByteArray();
    }

    public static Message messageDecode(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(byteArrayInputStream);

        Message message = new Message();
        // 读取魔数
        message.setMagicNumber(new byte[]{dis.readByte(),dis.readByte()});
        // 读取消息标识
        message.setMessageFlag(dis.readBoolean());
        // 读取单/双向标识
        message.setTwoWayFlag(dis.readBoolean());
        // 读取消息事件标识
        message.setEventFlag(dis.readBoolean());

        // 读取序列化类型
        Boolean[] serializeTypeBytes = new Boolean[Message.MESSAGE_SERIALIZE_TYPE_LENGTH];
        for(int i=0; i<Message.MESSAGE_SERIALIZE_TYPE_LENGTH; i++){
            serializeTypeBytes[i] = dis.readBoolean();
        }
        message.setSerializeType(serializeTypeBytes);

        // 读取响应状态
        message.setResponseStatus(dis.readByte());
        // 读取消息uuid
        message.setMessageUUId(dis.readLong());

        // 读取消息正文
//        Object

        return null;
    }
}
