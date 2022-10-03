package myrpc.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderManager {

    public static Map<String,Provider> providerMapCache = new ConcurrentHashMap<>();

    public static Provider getProvider(String name) {
        return providerMapCache.get(name);
    }

    public static void putProvider(String name, Provider luProvider) {
        providerMapCache.put(name, luProvider);
    }
}
