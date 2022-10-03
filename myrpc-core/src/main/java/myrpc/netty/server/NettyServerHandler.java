package myrpc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import myrpc.common.JsonUtil;
import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.message.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {

    private static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol){
        logger.info("NettyServerHandler channelRead0={}", JsonUtil.obj2Str(rpcRequestMessageProtocol));

        MessageProtocol<RpcResponse> responseMessage = getResponseMessage(rpcRequestMessageProtocol);

        logger.info("NettyServerHandler write responseMessage={}", JsonUtil.obj2Str(responseMessage));


        channelHandlerContext.channel().writeAndFlush(responseMessage);
    }

    public MessageProtocol<RpcResponse> getResponseMessage(MessageProtocol<RpcRequest> rpcRequestMessageProtocol){
        long requestMessageId = rpcRequestMessageProtocol.getMessageHeader().getMessageId();

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageId(requestMessageId);
        messageHeader.setMessageFlag(MessageFlagEnums.RESPONSE.getCode());
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(false);
        messageHeader.setSerializeType(MessageSerializeType.JSON.getCode());
        messageHeader.setResponseStatus((byte)'a');

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setMessageId(requestMessageId);
        // 暂时写死，后续优化为反射调用具体的实现类
        rpcResponse.setReturnValue("server echo");

        return new MessageProtocol<>(messageHeader,rpcResponse);
    }
}
