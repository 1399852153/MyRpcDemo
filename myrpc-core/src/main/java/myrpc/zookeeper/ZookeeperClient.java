package myrpc.zookeeper;

import myrpc.exception.MyRpcException;
import myrpc.proxy.ClientDynamicProxy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * ZooKeeper客户端
 */
public class ZookeeperClient {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    public static final String ZK_BASE_PATH = "/myrpc";
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;

    private CuratorFramework client;

    public ZookeeperClient(String zkServerAddress) {
        try {
            this.client = CuratorFrameworkFactory.newClient(
                    zkServerAddress, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            this.client.start();

            boolean connected = this.client.blockUntilConnected(3000, TimeUnit.MILLISECONDS);

            if (!connected) {
                throw new MyRpcException("zookeeper not connected");
            }
        } catch (InterruptedException e) {
            throw new MyRpcException("ZookeeperClient init error",e);
        }
    }

    /**
     * 创建永久节点
     * */
    public void createPersistent(String path) {
        try {
            client.create().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists.", e);
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperClient createPersistent error", e);
        }
    }

    /**
     * 创建临时节点
     * */
    public void createEphemeral(String path) {
        try {
            client.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            logger.warn("ZNode " + path + " already exists, since we will only try to recreate a node on a session expiration" +
                    ", this duplication might be caused by a delete delay from the zk server, which means the old expired session" +
                    " may still holds this ZNode and the server just hasn't got time to do the deletion. In this case, " +
                    "we can just try to delete and create again.", e);
            deletePath(path);
            createEphemeral(path);
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperClient createEphemeral error", e);
        }
    }

    /**
     * 删除节点
     * */
    public void deletePath(String path) {
        try {
            client.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(path);
        } catch (KeeperException.NoNodeException ignored) {
        } catch (Exception e) {
            throw new MyRpcException("ZookeeperClient deletePath error", e);
        }
    }
}
