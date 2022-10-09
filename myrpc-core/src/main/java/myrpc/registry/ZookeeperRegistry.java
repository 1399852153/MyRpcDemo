package myrpc.registry;

import myrpc.common.ServiceInfo;
import myrpc.common.URLAddress;
import myrpc.exception.MyRpcException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ZookeeperRegistry implements Registry{

    public static final String ZK_BASE_PATH = "/myrpc";
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 2;

    private final ServiceDiscovery<ServiceInfo> serviceDiscovery;

    public ZookeeperRegistry(String zkServerAddress) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(zkServerAddress, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            client.start();

            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(serializer)
                    .basePath(ZK_BASE_PATH)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperClient init error",e);
        }
    }

    @Override
    public void doRegistry(ServiceInfo serviceInfo){
        try {
            ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                    .name(serviceInfo.getServiceName())
                    .address(serviceInfo.getUrlAddress().getHost())
                    .port(serviceInfo.getUrlAddress().getPort())
                    .payload(serviceInfo)
                    .build();
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperRegistry doRegistry error",e);
        }
    }

    @Override
    public void doUnRegistry(ServiceInfo serviceInfo){
        try {
            ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                    .name(serviceInfo.getServiceName())
                    .address(serviceInfo.getUrlAddress().getHost())
                    .port(serviceInfo.getUrlAddress().getPort())
                    .payload(serviceInfo)
                    .build();

            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperRegistry doUnRegistry error",e);
        }
    }

    @Override
    public void doSubscribe() {
        // TODO 待实现
    }

    @Override
    public List<ServiceInfo> discovery(String serviceName){
        try {
            Collection<ServiceInstance<ServiceInfo>> serviceInstanceCollection =
                    serviceDiscovery.queryForInstances(serviceName);

            if(!serviceInstanceCollection.isEmpty()){
                return serviceInstanceCollection.stream()
                        .map(ServiceInstance::getPayload)
                        .collect(Collectors.toList());
            }else{
                // 无provider
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperRegistry discovery error",e);
        }
    }
}
