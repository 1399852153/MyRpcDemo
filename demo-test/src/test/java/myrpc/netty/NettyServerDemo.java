package myrpc.netty;

import myrpc.common.URLAddress;
import myrpc.netty.server.NettyServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo {

    public static void main(String[] args) throws UnknownHostException{
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;

        NettyServer nettyServer = new NettyServer(new URLAddress(serverAddress,port));
        nettyServer.init();
    }
}
