package cug.sakura.config;

/**
 * @program: NettyRPC
 * @description: 配置常量
 * @author: Sakura
 * @create: 2021-12-13 18:42
 **/

public interface Constant {
    int ZK_SESSION_TIMEOUT = 5000;
    int ZK_CONNECTION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    String ZK_NAMESPACE = "netty-rpc";
}