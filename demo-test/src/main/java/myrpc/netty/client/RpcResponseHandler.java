package myrpc.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import myrpc.common.JsonUtil;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> rpcResponseMessageProtocol) throws Exception {
        logger.info("channelRead0={}",JsonUtil.obj2Str(rpcResponseMessageProtocol));
    }
}
