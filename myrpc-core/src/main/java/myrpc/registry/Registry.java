package myrpc.registry;

import myrpc.common.URLAddress;

import java.util.List;

public interface Registry {

    void doRegistry(String serviceName, URLAddress serverUrlAddress);

    void doUnRegistry(String serviceName, URLAddress serverUrlAddress);

    void doSubscribe();

    List<URLAddress> getURLAddress(String serviceName);
}
