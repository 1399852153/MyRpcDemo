package myrpc.netty;

import myrpc.common.URLAddress;
import myrpc.netty.server.NettyServer;
import myrpc.provider.Provider;
import myrpc.registry.LocalFileRegistry;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
import service.HelloService;
import service.HelloServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo {

    public static void main(String[] args) throws UnknownHostException{
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8081;

        URLAddress providerURLAddress = new URLAddress(serverAddress,port);

        Registry localFileRegistry = RegistryFactory.getRegistry(
                new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));

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
