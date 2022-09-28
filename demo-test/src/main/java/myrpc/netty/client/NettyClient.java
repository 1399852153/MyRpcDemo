package myrpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import myrpc.netty.message.codec.NettyEncoder;
import myrpc.netty.message.enums.MessageFlagEnums;
import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    private final String serverAddress;
    private final int port;
    private Bootstrap bootstrap;

    public NettyClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void init(){
        if(this.bootstrap == null) {
            Bootstrap bootstrap = new Bootstrap();
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup(DEFAULT_IO_THREADS,
                    new DefaultThreadFactory("NettyClientWorker", true));

            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    // 编码、解码处理器
                                    .addLast("decoder", new NettyEncoder<MessageProtocol<RpcRequest>>())
                                    .addLast("encoder", new NettyEncoder<>())
                                    // 心跳处理器
//                                .addLast("server-idle-handler",
//                                        new IdleStateHandler(0, 0, 5, MILLISECONDS))
                                    // 实际调用业务方法的处理器
                                    .addLast("clientHandler", new NettyRpcResponseHandler())
                            ;
                        }
                    });

            this.bootstrap = bootstrap;
        }
    }

    public ChannelFuture connectAsync(){
        return bootstrap.connect(serverAddress, port);
    }

    public Channel connectSync() throws InterruptedException {
        return bootstrap.connect(serverAddress, port).sync().channel();
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;

        NettyClient nettyClient = new NettyClient(serverAddress,8080);
        nettyClient.init();
        ChannelFuture channelFuture = nettyClient.connectAsync().sync();
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

        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>(messageHeader,rpcRequest);
        channelFuture.channel().writeAndFlush(messageProtocol);
        channelFuture.channel().closeFuture().sync();
    }
}
