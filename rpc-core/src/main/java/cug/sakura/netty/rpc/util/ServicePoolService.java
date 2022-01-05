package cug.sakura.netty.rpc.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: NettyRPC
 * @description: 线程池工具类
 * @author: isHaitaoLiu
 * @create: 2022-01-05 21:41
 **/

public class ServicePoolService {
    public static ThreadPoolExecutor makeServerThreadPool(final String serviceName, int corePoolSize, int maxPoolSize) {

        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                runnable -> new Thread(runnable, "netty-rpc-" + serviceName + "-" + runnable.hashCode()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
