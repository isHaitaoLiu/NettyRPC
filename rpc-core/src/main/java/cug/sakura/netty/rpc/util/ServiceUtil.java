package cug.sakura.netty.rpc.util;

/**
 * @program: NettyRPC
 * @description: 服务工具类
 * @author: isHaitaoLiu
 * @create: 2022-01-05 21:40
 **/

public class ServiceUtil {
    public static final String SERVICE_CONCAT_TOKEN = "#";

    /*
     * @Author sakura
     * @Description 构造服务的key
     * @Date 2022/1/5
     * @Param [interfaceName, version]
     * @return java.lang.String
     **/
    public static String makeServiceKey(String interfaceName, String version) {
        String serviceKey = interfaceName;
        if (version != null && version.trim().length() > 0) {
            serviceKey += SERVICE_CONCAT_TOKEN.concat(version);
        }
        return serviceKey;
    }
}
