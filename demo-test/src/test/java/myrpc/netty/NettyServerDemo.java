package myrpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import myrpc.netty.message.codec.NettyDecoder;
import myrpc.netty.message.codec.NettyEncoder;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.server.NettyServer;
import myrpc.netty.server.NettyServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo {

    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        EventLoopGroup workerGroup = new NioEventLoopGroup(DEFAULT_IO_THREADS,new DefaultThreadFactory("NettyServerWorker", true));

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                // 编码、解码处理器
                                .addLast("encoder",new NettyEncoder<MessageProtocol<RpcRequest>>())
                                .addLast("decoder",new NettyDecoder())
                                // 心跳处理器
//                                .addLast("server-idle-handler",
//                                        new IdleStateHandler(0, 0, 5, MILLISECONDS))
                                // 实际调用业务方法的处理器
                                .addLast("serverHandler",new NettyServerHandler());
                    }
                });

        int port = 8080;
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        logger.info("server addr {} started on port {}", serverAddress, port);
        ChannelFuture channelFuture = bootstrap.bind(serverAddress, 8080).sync();
        channelFuture.channel().closeFuture().sync();

    }
}
