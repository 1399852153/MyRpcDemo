package myrpc.registry;

import myrpc.common.ServiceInfo;

import java.util.List;

public interface Registry {

    void doRegistry(ServiceInfo serviceInfo);

    void doUnRegistry(ServiceInfo serviceInfo);

    List<ServiceInfo> discovery(String serviceName);
}
