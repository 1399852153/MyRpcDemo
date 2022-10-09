package myrpc.registry;

import myrpc.common.ServiceInfo;
import myrpc.common.URLAddress;

import java.util.List;

public interface Registry {

    void doRegistry(ServiceInfo serviceInfo);

    void doUnRegistry(ServiceInfo serviceInfo);

    void doSubscribe();

    List<ServiceInfo> discovery(String serviceName);
}
