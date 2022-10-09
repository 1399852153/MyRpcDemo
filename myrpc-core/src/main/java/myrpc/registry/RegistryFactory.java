package myrpc.registry;

import myrpc.exception.MyRpcException;
import myrpc.registry.enums.RegistryCenterTypeEnum;

public class RegistryFactory {

    public static Registry getRegistry(RegistryConfig registryConfig){
        RegistryCenterTypeEnum registryCenterTypeEnum = RegistryCenterTypeEnum
                .getByCode(registryConfig.getRegistryCenterType());

        switch (registryCenterTypeEnum){
            case ZOOKEEPER:
                return new ZookeeperRegistry(registryConfig.getRegistryAddress());
            case LOCAL_FILE:
                return new LocalFileRegistry();
            default:
                throw new MyRpcException("getRegistry type illegal: type=" + registryConfig.getRegistryCenterType());
        }
    }
}
