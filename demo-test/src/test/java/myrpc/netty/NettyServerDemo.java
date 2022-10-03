package myrpc.netty;

import myrpc.common.URLAddress;
import myrpc.netty.server.NettyServer;
import myrpc.provider.Provider;
import myrpc.provider.ProviderManager;
import service.HelloService;
import service.HelloServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo {

    public static void main(String[] args) throws UnknownHostException{
        Provider<HelloServiceImpl> provider = new Provider<>();
        provider.setInterfaceClass(HelloService.class);
        HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
        provider.setRef(helloServiceImpl);

        ProviderManager.putProvider(provider.getInterfaceClass().getName(),provider);


        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8080;

        NettyServer nettyServer = new NettyServer(new URLAddress(serverAddress,port));
        nettyServer.init();
    }
}
