package myrpc.balance;

import myrpc.common.ServiceInfo;

import java.util.List;

public interface LoadBalance {

    ServiceInfo select(List<ServiceInfo> serviceInfoList);
}
