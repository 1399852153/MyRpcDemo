package myrpc.registry;

import myrpc.common.ServiceInfo;
import myrpc.common.URLAddress;
import myrpc.exception.MyRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 本地文件方式存储数据的，简易版本注册中心
 * （在不额外依赖zk等外部注册中心组件的单机demo运行时使用）
 * */
public class LocalFileRegistry implements Registry{

    private static final Logger logger = LoggerFactory.getLogger(LocalFileRegistry.class);

    private final Map<String, ServiceInfo> registerServiceCache = new HashMap<>();

    private final String userHome = System.getProperty("user.home");
    private final String configHome = userHome + "/myrpc/config.conf";
    private final String lineSeparator = System.getProperty("line.separator");

    @Override
    public synchronized void doRegistry(ServiceInfo serviceInfo) {
        createConfigFileDir();

        File configFile = new File(configHome);
        try (PrintWriter pw = new PrintWriter((new FileOutputStream(configFile, true)))){
            pw.append(serviceInfo.getServiceName()).append("=")
                    .append(serviceInfo.getUrlAddress().getHost())
                    .append(":")
                    .append(String.valueOf(serviceInfo.getUrlAddress().getPort()));
            pw.append(lineSeparator);
            pw.flush();
        } catch (FileNotFoundException e) {
            throw new MyRpcException("LocalFileRegistry doRegistry error",e);
        }
    }

    @Override
    public synchronized void doUnRegistry(ServiceInfo serviceInfo) {
        logger.warn("LocalFileRegistry 暂时不支持 doUnRegistry操作");
    }

    @Override
    public List<ServiceInfo> discovery(String serviceName) {
        if(!registerServiceCache.containsKey(serviceName)){
            readFromLocalFile();
        }

        return Collections.singletonList(registerServiceCache.get(serviceName));
    }

    private void readFromLocalFile(){
        createConfigFileDir();

        File configFile = new File(configHome);
        long length = configFile.length();
        if (length <= 0) {
            throw new MyRpcException("LocalFileRegister Center config is empty, maybe you should start server first!");
        }
        byte[] content = new byte[(int) length];
        try(FileInputStream is = new FileInputStream(configFile)) {
            is.read(content);

            String contentString = new String(content);
            String[] arr = contentString.split(lineSeparator);

            List<String> data = Arrays.asList(arr);

            cacheData(data);
        } catch (java.io.IOException e) {
            throw new MyRpcException("LocalFileRegistry readFromLocalFile error",e);
        }
    }

    private void createConfigFileDir(){
        File file = new File(userHome + "/myrpc");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void cacheData(List<String> data) {
        for (String item : data) {
            String[] arr = item.split("=");
            if (arr.length == 2) {
                String serviceName = arr[0];
                String[] serverUrl = arr[1].split(":");
                URLAddress urlAddress = new URLAddress(serverUrl[0], Integer.parseInt(serverUrl[1]));
                registerServiceCache.put(arr[0],
                        new ServiceInfo(serviceName,urlAddress));
            }
        }
    }
}
