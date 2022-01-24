package cug.sakura.netty.rpc.server.core;

import cug.sakura.netty.rpc.codec.*;
import cug.sakura.netty.rpc.serializer.ProtostuffSerializer;
import cug.sakura.netty.rpc.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: NettyRPC
 * @description: 服务初始化处理器
 * @author: isHaitaoLiu
 * @create: 2022-01-24 23:28
 **/

public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private Map<String, Object> handlerMap;
    private ThreadPoolExecutor threadPoolExecutor;

    public RpcServerInitializer(Map<String, Object> handlerMap, ThreadPoolExecutor threadPoolExecutor){
        this.handlerMap = handlerMap;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        Serializer serializer = ProtostuffSerializer.class.newInstance();
        ChannelPipeline cp = socketChannel.pipeline();
        //心跳检测处理器，IdleStateHandler心跳检测主要是通过向线程任务队列中添加定时任务，
        //判断channelRead()方法或write()方法是否调用空闲超时，如果超时则触发超时事件执行自定义userEventTrigger()方法；
        cp.addLast(new IdleStateHandler(0, 0, RpcBeat.BEAT_TIMEOUT, TimeUnit.SECONDS));
        //拆包处理器，参数（最大帧长度，长度域偏移，长度域字节数，数据长度修正，跳过的字节数）
        cp.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        //解码器
        cp.addLast(new RpcDecoder(RpcRequest.class, serializer));
        //编码器
        cp.addLast(new RpcEncoder(RpcResponse.class, serializer));
        //服务器处理器
        cp.addLast(new RpcServerHandler(this.handlerMap, this.threadPoolExecutor));
    }
}
