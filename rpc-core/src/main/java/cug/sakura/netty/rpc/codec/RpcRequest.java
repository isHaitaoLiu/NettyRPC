package cug.sakura.netty.rpc.codec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: NettyRPC
 * @description: RPC请求
 * @author: isHaitaoLiu
 * @create: 2021-12-17 11:24
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -2466532190561258126L;
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String version;
}
