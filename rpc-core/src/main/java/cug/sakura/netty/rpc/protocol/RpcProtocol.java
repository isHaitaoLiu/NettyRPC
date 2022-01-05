package cug.sakura.netty.rpc.protocol;

import cug.sakura.netty.rpc.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @program: NettyRPC
 * @description: rpc协议
 * @author: isHaitaoLiu
 * @create: 2022-01-05 20:49
 **/

public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = -4213505649707151418L;
    //服务主机
    @Setter
    @Getter
    private String host;
    //服务端口
    @Setter
    @Getter
    private int port;
    //服务信息列表
    @Setter
    @Getter
    private List<RpcServiceInfo> serviceInfoList;

    public String toJson(){
        return JsonUtil.objectToJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RpcProtocol that = (RpcProtocol) o;

        if (port != that.port) return false;
        if (!Objects.equals(host, that.host)) return false;
        return Objects.equals(serviceInfoList, that.serviceInfoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, serviceInfoList.hashCode());
    }

    private boolean isListEquals(List<RpcServiceInfo> thisList, List<RpcServiceInfo> thatList) {
        if (thisList == null && thatList == null) {
            return true;
        }
        if (thisList == null || thatList == null || thisList.size() != thatList.size()) {
            return false;
        }
        return thisList.containsAll(thatList) && thatList.containsAll(thisList);
    }

}
