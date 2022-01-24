package cug.sakura.netty.rpc.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: NettyRPC
 * @description: RPC响应
 * @author: isHaitaoLiu
 * @create: 2021-12-17 11:24
 **/

@Data
public class RpcResponse implements Serializable {
    public static final Integer SUCCESS = 200;
    public static final Integer SERVER_ERROR = 500;
    public static final Integer CLIENT_ERROR = 400;
    public static final Integer SEND_FAILED = 300;

    private static final long serialVersionUID = 2047916088543420953L;
    private String requestId;
    private Integer code;
    private String message;
    private Object result;
}
