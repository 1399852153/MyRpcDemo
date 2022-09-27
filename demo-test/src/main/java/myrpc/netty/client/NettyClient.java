package myrpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import myrpc.netty.message.codec.NettyEncoder;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
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
                                .addLast("decoder",new NettyEncoder<MessageProtocol<RpcRequest>>())
                                .addLast("encoder",new NettyEncoder<>())
                                // 心跳处理器
//                                .addLast("server-idle-handler",
//                                        new IdleStateHandler(0, 0, 5, MILLISECONDS))
                                // 实际调用业务方法的处理器
//                                .addLast("clientHandler",null)
                        ;
                    }
                });

        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;
        ChannelFuture channelFuture = bootstrap.connect(serverAddress, 8080).sync();
        logger.info("client connected addr {} started on port {}", serverAddress, port);
        channelFuture.channel().closeFuture().sync();

    }
}
