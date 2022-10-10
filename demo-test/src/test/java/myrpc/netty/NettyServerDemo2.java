package myrpc.netty;

import myrpc.common.URLAddress;
import myrpc.netty.server.NettyServer;
import myrpc.provider.Provider;
import myrpc.registry.Registry;
import myrpc.registry.RegistryConfig;
import myrpc.registry.RegistryFactory;
import myrpc.registry.enums.RegistryCenterTypeEnum;
import service.HelloService;
import service.HelloServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServerDemo2 {

    public static void main(String[] args) throws UnknownHostException{
        String serverAddress = InetAddress.getLocalHost().getHostAddress();
        int port = 8082;

        URLAddress providerURLAddress = new URLAddress(serverAddress,port);

        Registry registry = RegistryFactory.getRegistry(
                new RegistryConfig(RegistryCenterTypeEnum.ZOOKEEPER.getCode(), "127.0.0.1:2181"));

        Provider<HelloServiceImpl> provider = new Provider<>();
        provider.setInterfaceClass(HelloService.class);
        HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
        provider.setRef(helloServiceImpl);
        provider.setUrlAddress(providerURLAddress);
        provider.setRegistry(registry);

        provider.export();

        NettyServer nettyServer = new NettyServer(providerURLAddress);
        nettyServer.init();
    }
}
