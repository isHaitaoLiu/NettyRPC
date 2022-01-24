package cug.sakura.netty.rpc.server.core;

import cug.sakura.netty.rpc.server.registry.ServiceRegistry;
import cug.sakura.netty.rpc.util.ServiceUtil;
import cug.sakura.netty.rpc.util.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @program: NettyRPC
 * @description: netty服务器
 * @author: isHaitaoLiu
 * @create: 2022-01-24 19:27
 **/

@Slf4j
public class NettyServer {

    private Thread thread;
    private String serverAddress;
    private ServiceRegistry serviceRegistry;
    private Map<String, Object> serviceMap = new HashMap<>();


    public NettyServer(String serverAddress, ServiceRegistry serviceRegistry){
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    public void addService(String interfaceName, String version, Object serviceBean){
        log.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        String key = ServiceUtil.makeServiceKey(interfaceName, version);
        serviceMap.put(key, serviceBean);
    }


    public void start(){
        thread = new Thread(new Runnable() {
            ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.makeServerThreadPool(
                    NettyServer.class.getSimpleName(), 16, 32);

            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();  //连接事件循环组
                EventLoopGroup workerGroup = new NioEventLoopGroup();  //处理事件循环组
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new RpcServerInitializer(serviceMap, threadPoolExecutor))
                            .option(ChannelOption.SO_BACKLOG, 128)   //可连接队列数，三次握手最大连接数
                            .childOption(ChannelOption.SO_KEEPALIVE, true);  //服务端自动探测客户端连接情况
                    //解析服务器地址， ip:port
                    String[] array = serverAddress.split(":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    //绑定ip地址和端口号
                    ChannelFuture future = bootstrap.bind(host, port).sync();
                    //服务注册
                    if (serviceRegistry != null) {
                        serviceRegistry.registerService(host, port, serviceMap);
                    }
                    log.info("Server started on port {}", port);
                    //通道关闭
                    future.channel().closeFuture().sync();
                }catch (Exception e){
                    if (e instanceof InterruptedException){
                        log.info("Rpc server remoting server stop");
                    }else {
                        log.error("Rpc server remoting server error", e);
                    }
                }finally {
                    try {
                        serviceRegistry.unregisterService();
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
        thread.start();
    }


    public void stop() {
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
