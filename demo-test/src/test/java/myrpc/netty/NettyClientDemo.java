package myrpc.netty;

import io.netty.channel.Channel;
import myrpc.common.URLAddress;
import myrpc.netty.client.NettyClient;
import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyClientDemo {

    private static Logger logger = LoggerFactory.getLogger(NettyClientDemo.class);

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;

        NettyClient nettyClient = new NettyClient(new URLAddress(serverAddress,8080));
        nettyClient.init();
        logger.info("client connected addr {} started on port {}", serverAddress, port);
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageFlag(MessageFlagEnums.REQUEST.getCode());
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(true);
        messageHeader.setSerializeType(MessageSerializeType.HESSIAN.getCode());
        messageHeader.setResponseStatus((byte)'a');
        messageHeader.setMessageId(123456789L);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("com.aaa.bcd");
        rpcRequest.setMethodName("echo");
        rpcRequest.setParameterClasses(new Class[]{String.class});
        rpcRequest.setParams(new Object[]{"name1"});
        rpcRequest.setReturnClass(String.class);

        Channel channel = nettyClient.getChannel();
        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>(messageHeader,rpcRequest);
        channel.writeAndFlush(messageProtocol);
        channel.closeFuture().sync();
    }
}
