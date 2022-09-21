package myrpc.netty.message.util;

import myrpc.netty.message.Message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
//        dos.writeBoolean();

        return null;
    }

    public static Message messageDecode(byte[] bytes){
        return null;
    }
}
