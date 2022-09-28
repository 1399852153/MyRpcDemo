package myrpc.netty.client;

import java.util.concurrent.ConcurrentHashMap;

public class NettyClientFactory {

    private static final ConcurrentHashMap<String,NettyClient> nettyClientCache = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    public static NettyClient getNettyClient(String serverAddress, int port){
        NettyClient nettyClient = nettyClientCache.get(serverAddress);
        if(nettyClient != null){
            return nettyClient;
        }else{
            synchronized (LOCK){
                if(nettyClientCache.get(serverAddress) != null){
                    return nettyClientCache.get(serverAddress);
                }

                // 双重检查
                NettyClient newNettyClient = new NettyClient(serverAddress,port);
                newNettyClient.init();
                nettyClientCache.put(serverAddress,newNettyClient);
                return newNettyClient;
            }
        }
    }
}
