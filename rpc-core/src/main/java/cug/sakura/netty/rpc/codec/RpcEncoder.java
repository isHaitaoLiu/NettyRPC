package cug.sakura.netty.rpc.codec;

import cug.sakura.netty.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: NettyRPC
 * @description: 编码器
 * @author: isHaitaoLiu
 * @create: 2021-12-17 15:54
 **/

@Slf4j
public class RpcEncoder extends MessageToByteEncoder<Object> {
    private final Class<?> genericClass;
    private final Serializer serializer;

    public RpcEncoder(Class<?> genericClass, Serializer serializer){
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(object)){
            try {
                byte[] data = serializer.serialize(object);
                byteBuf.writeInt(data.length);
                byteBuf.writeBytes(data);
            } catch (Exception ex) {
                log.error("Encode error: " + ex.toString());
            }
        }
    }
}
