package cug.sakura.netty.rpc.codec;

import cug.sakura.netty.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: NettyRPC
 * @description: 解码器
 * @author: isHaitaoLiu
 * @create: 2021-12-17 16:11
 **/

@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    private final Class<?> genericClass;
    private final Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer){
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readInt();
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        Object obj;
        try {
            obj = serializer.deserialize(data, genericClass);
            list.add(obj);
        } catch (Exception ex) {
            log.error("Decode error: " + ex.toString());
        }
    }
}
