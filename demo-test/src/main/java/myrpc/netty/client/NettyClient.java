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
import myrpc.common.URLAddress;
import myrpc.exception.MyRpcRemotingException;
import myrpc.netty.message.codec.NettyEncoder;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;

public class NettyClient {

    private static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    private final URLAddress urlAddress;
    private Bootstrap bootstrap;
    private Channel channel;

    public NettyClient(URLAddress urlAddress) {
        this.urlAddress = urlAddress;
    }

    public void init() {
        try {
            doOpen();
            doConnect();
        }catch (Exception e){
            throw new MyRpcRemotingException("NettyClient init error",e);
        }
    }

    public Channel getChannel(){
        return this.channel;
    }

    public void send(RpcRequest rpcRequest) throws InterruptedException {
        // 很多case没考虑到，可以参考dubbo的NettyChannel.send方法
        ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest);
        channelFuture.sync();

        Throwable cause = channelFuture.cause();
        if (cause != null) {
            throw new MyRpcRemotingException("NettyClient send error",cause);
        }
    }

    private void doOpen(){
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

    private void doConnect() throws InterruptedException {
        ChannelFuture future = bootstrap.connect(urlAddress.getHost(), urlAddress.getPort());

        // 写的很简单，异常case都没考虑（可以参考NettyClient.doConnect实现）
        this.channel = future.sync().channel();
    }
}
