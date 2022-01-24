package cug.sakura.netty.rpc.server.registry;

import cug.sakura.netty.rpc.zookeeper.CuratorClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: NettyRPC
 * @description: 服务注册
 * @author: isHaitaoLiu
 * @create: 2022-01-24 19:30
 **/

public class ServiceRegistry {

    private CuratorClient curatorClient;
    private List<String> pathList = new ArrayList<>();

    public ServiceRegistry(String registryAddress) {
        this.curatorClient = new CuratorClient(registryAddress);
    }

    public void registerService(String host, int port, Map<String, Object> serviceMap){

    }

    public void unregisterService(){

    }
}
