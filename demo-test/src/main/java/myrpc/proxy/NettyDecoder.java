package myrpc.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import myrpc.netty.message.Message;

import java.util.List;

/**
 * netty 解码器
 * @author shanreng
 */
public class NettyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readable = byteBuf.readableBytes();

        // 读取header头
        byte[] header = new byte[Math.min(readable, Message.MESSAGE_HEADER_LENGTH)];
        byteBuf.readBytes(header);


    }
}
