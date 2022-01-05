package cug.sakura.netty.rpc.protocol;

import cug.sakura.netty.rpc.util.JsonUtil;
import lombok.Data;

import java.util.Objects;

/**
 * @program: NettyRPC
 * @description: rpc服务信息列表
 * @author: isHaitaoLiu
 * @create: 2022-01-05 20:51
 **/


@Data
public class RpcServiceInfo {
    //服务名
    private String serviceName;
    //服务版本号
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RpcServiceInfo that = (RpcServiceInfo) o;

        if (!Objects.equals(serviceName, that.serviceName)) return false;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    public String toJson() {
        return JsonUtil.objectToJson(this);
    }
}
