package myrpc.netty.message.util;

import io.netty.buffer.ByteBuf;
import myrpc.common.GlobalConstants;
import myrpc.common.JsonUtil;
import myrpc.netty.message.model.MessageHeader;


public class MessageCodecUtil {

//    public static <T> byte[] messageEncode(MessageHeader<T> message) throws IOException {
//        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream(Message.MESSAGE_HEADER_LENGTH);
//        DataOutputStream dos = new DataOutputStream(byteArrayInputStream);
//
//        // 写入魔数
//        dos.write(Message.MAGIC_HIGH);
//        dos.write(Message.MAGIC_LOW);
//
//        // 写入消息标识
//        dos.writeBoolean(message.getMessageFlag());
//        // 写入单/双向标识
//        dos.writeBoolean(message.getTwoWayFlag());
//        // 写入消息事件标识
//        dos.writeBoolean(message.getEventFlag());
//        // 写入序列化类型
//        for(boolean b : message.getSerializeType()){
//            dos.writeBoolean(b);
//        }
//        // 写入响应状态
//        dos.writeByte(message.getResponseStatus());
//        // 写入消息uuid
//        dos.writeLong(message.getMessageUUId());
//
//        // todo 暂时写死json序列化，后续再抽象
//        String jsonStr = JsonUtil.obj2Str(message.getBizData());
//        byte[] bizMessageBytes = jsonStr.getBytes(GlobalConstants.DEFAULT_CHARSET);
//        // 写入消息正文长度
//        dos.writeInt(bizMessageBytes.length);
//        // 写入消息正文内容
//        dos.write(bizMessageBytes);
//
//        return byteArrayInputStream.toByteArray();
//    }

    /**
     * header头解码
     * */
    public static MessageHeader messageHeaderDecode(ByteBuf byteBuf){
        MessageHeader messageHeader = new MessageHeader();
        // 读取魔数
        messageHeader.setMagicNumber(new byte[]{byteBuf.readByte(),byteBuf.readByte()});
        // 读取消息标识
        messageHeader.setMessageFlag(byteBuf.readBoolean());
        // 读取单/双向标识
        messageHeader.setTwoWayFlag(byteBuf.readBoolean());
        // 读取消息事件标识
        messageHeader.setEventFlag(byteBuf.readBoolean());

        // 读取序列化类型
        Boolean[] serializeTypeBytes = new Boolean[MessageHeader.MESSAGE_SERIALIZE_TYPE_LENGTH];
        for(int i=0; i<MessageHeader.MESSAGE_SERIALIZE_TYPE_LENGTH; i++){
            serializeTypeBytes[i] = byteBuf.readBoolean();
        }
        messageHeader.setSerializeType(serializeTypeBytes);

        // 读取响应状态
        messageHeader.setResponseStatus(byteBuf.readByte());
        // 读取消息uuid
        messageHeader.setMessageUUId(byteBuf.readLong());

        // 读取消息正文长度
        int bizDataLength = byteBuf.readInt();
        messageHeader.setBizDataLength(bizDataLength);

        return messageHeader;
    }

    public static <T> T messageBizDataDecode(ByteBuf byteBuf, int bizDataLength, Class<T> messageBizDataType){
        // 读取消息正文
        byte[] bizDataBytes = new byte[bizDataLength];
        byteBuf.readBytes(bizDataBytes);

        // todo 暂时写死json序列化，后续再抽象
        String jsonStr = new String(bizDataBytes,GlobalConstants.DEFAULT_CHARSET);

        return JsonUtil.json2Obj(jsonStr,messageBizDataType);
    }
}
