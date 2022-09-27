package myrpc.netty.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    private static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("NettyServerHandler channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        logger.info("NettyServerHandler read");
        super.read(ctx);
    }
}
