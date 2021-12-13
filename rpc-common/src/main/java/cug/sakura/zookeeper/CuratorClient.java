package cug.sakura.zookeeper;

import cug.sakura.config.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * @program: NettyRPC
 * @description: zk客户端
 * @author: isHaitaoLiu
 * @create: 2021-12-13 17:22
 **/


public class CuratorClient {
    private final CuratorFramework curatorClient;

    public CuratorClient(String connectString, String namespace, int sessionTimeout, int connectionTimeout){
        curatorClient = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .namespace(namespace)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000,10))
                .build();
        curatorClient.start();
    }

    public CuratorClient(String connectString) {
        this(connectString, Constant.ZK_NAMESPACE, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }

    public CuratorFramework getCuratorClient(){
        return curatorClient;
    }

    /*
     * @Author isHaitaoLiu
     * @Description 创建带序号的临时节点
     * @Date 2021/12/13
     * @Param [path, data]
     * @return java.lang.String
     **/
    public String createPathDataEphemeralSequential(String path, byte[] data) throws Exception{
        return curatorClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path, data);
    }

    public void deletePath(String path) throws Exception {
        curatorClient.delete().forPath(path);
    }

    public void updatePathData(String path, byte[] data) throws Exception {
        curatorClient.setData().forPath(path, data);
    }


    public byte[] getPathData(String path) throws Exception{
        return curatorClient.getData().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return curatorClient.getChildren().forPath(path);
    }

    /*
     * @Author isHaitaoLiu
     * @Description 连接状态监听器设置
     * @Date 2021/12/13
     * @Param [connectionStateListener]
     * @return void
     **/
    public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
        curatorClient.getConnectionStateListenable().addListener(connectionStateListener);
    }


    public void watchCachedNode(String path, NodeCacheListener listener) throws Exception {
        //创建节点数据缓存
        CuratorCache curatorCache = CuratorCache.builder(curatorClient, path).withOptions(CuratorCache.Options.SINGLE_NODE_CACHE).build();
        //监听器转换
        CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder().forNodeCache(listener).build();
        //绑定监听器
        curatorCache.listenable().addListener(curatorCacheListener);
        //开启监听
        curatorCache.start();
    }

    public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
        //创建节点数据缓存
        CuratorCache curatorCache = CuratorCache.builder(curatorClient, path).build();
        //监听器转换
        CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder().forPathChildrenCache(path, curatorClient, listener).build();
        //绑定监听器
        curatorCache.listenable().addListener(curatorCacheListener);
        //开启监听
        curatorCache.start();
    }


    public void watchTreeNode(String path, TreeCacheListener listener) {
        //创建节点数据缓存
        CuratorCache curatorCache = CuratorCache.builder(curatorClient, path).build();
        //监听器转换
        CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder().forTreeCache(curatorClient, listener).build();
        //绑定监听器
        curatorCache.listenable().addListener(curatorCacheListener);
        //开启监听
        curatorCache.start();
    }

    public void close() {
        curatorClient.close();
    }
}
