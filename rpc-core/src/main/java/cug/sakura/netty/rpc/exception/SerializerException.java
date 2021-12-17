package cug.sakura.netty.rpc.exception;

/**
 * @program: NettyRPC
 * @description: 序列化异常
 * @author: isHaitaoLiu
 * @create: 2021-12-17 10:39
 **/

public class SerializerException extends Exception{
    public SerializerException() {
        super();
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }

    protected SerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
