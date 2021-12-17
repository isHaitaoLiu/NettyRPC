package cug.sakura.netty.rpc.codec;

/**
 * @program: NettyRPC
 * @description: 心跳消息
 * @author: isHaitaoLiu
 * @create: 2021-12-17 11:02
 **/

public class RpcBeat{
    public static final int BEAT_INTERVAL = 30;
    public static final int BEAT_TIMEOUT = 3 * BEAT_INTERVAL;
    public static final String BEAT_ID = "BEAT_PING_PONG";

    public static RpcRequest BEAT_PING;

    static {
        BEAT_PING = new RpcRequest() {};
        BEAT_PING.setRequestId(BEAT_ID);
    }

}
