package myrpc.netty;

import myrpc.common.URLAddress;
import myrpc.netty.server.NettyServer;
import myrpc.provider.Provider;
import myrpc.registry.LocalFileRegistry;
import myrpc.registry.Registry;
import service.HelloService;
import service.HelloServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo {

    public static void main(String[] args) throws UnknownHostException{
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;

        URLAddress providerURLAddress = new URLAddress(serverAddress,port);

        Registry localFileRegistry = new LocalFileRegistry();

        Provider<HelloServiceImpl> provider = new Provider<>();
        provider.setInterfaceClass(HelloService.class);
        HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
        provider.setRef(helloServiceImpl);
        provider.setUrlAddress(providerURLAddress);
        provider.setRegistry(localFileRegistry);

        provider.export();

        NettyServer nettyServer = new NettyServer(providerURLAddress);
        nettyServer.init();
    }
}
