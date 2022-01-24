package cug.sakura.netty.rpc.server.core;

import cug.sakura.netty.rpc.codec.RpcBeat;
import cug.sakura.netty.rpc.codec.RpcRequest;
import cug.sakura.netty.rpc.codec.RpcResponse;
import cug.sakura.netty.rpc.util.ServiceUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @program: NettyRPC
 * @description: rpc请求处理器
 * @author: isHaitaoLiu
 * @create: 2022-01-24 23:50
 **/
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor serverHandlerPool;

    public RpcServerHandler(Map<String, Object> handlerMap, final ThreadPoolExecutor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.serverHandlerPool = threadPoolExecutor;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            //Channel关闭，打出警告语句
            ctx.channel().close();
            log.warn("Channel idle in last {} seconds, close it", RpcBeat.BEAT_TIMEOUT);
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        if (RpcBeat.BEAT_ID.equalsIgnoreCase(rpcRequest.getRequestId())){
            log.info("Server read heartbeat ping");
            return;
        }
        serverHandlerPool.execute(new Runnable() {
            @Override
            public void run() {
                log.info("Receive request " + rpcRequest.getRequestId());
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                try {
                    //处理rpc请求
                    Object result = handle(rpcRequest);
                    rpcResponse.setCode(RpcResponse.SUCCESS);
                    rpcResponse.setResult(result);
                }catch (Throwable t){
                    //出现服务端错误
                    rpcResponse.setCode(RpcResponse.SERVER_ERROR);
                    rpcResponse.setResult(null);
                    rpcResponse.setMessage(t.getMessage());
                    log.error("RPC Server handle request error", t);
                }
                //发送回复
                channelHandlerContext.writeAndFlush(rpcResponse).addListener((ChannelFutureListener) channelFuture -> {
                            log.info("Send response for request " + rpcRequest.getRequestId());});
            }
        });
    }

    private Object handle(RpcRequest rpcRequest) throws Throwable{
        String className = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        String serviceKey = ServiceUtil.makeServiceKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        //查询服务是否存在
        if (serviceBean == null) {
            log.error("Can not find service implement with interface name: {} and version: {}", className, version);
            return null;
        }
        Class<?> serviceClass = serviceBean.getClass();
        //获取方法名
        String methodName = rpcRequest.getMethodName();
        //获取参数类型
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        //获取参数内容
        Object[] parameters = rpcRequest.getParameters();

        log.debug(serviceClass.getName());
        log.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            log.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            log.debug(parameters[i].toString());
        }
        //生成增强类实现invoke方法
        FastClass serviceFastClass = FastClass.create(serviceClass);
        //解析参数索引
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        //调用方法
        return serviceFastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
