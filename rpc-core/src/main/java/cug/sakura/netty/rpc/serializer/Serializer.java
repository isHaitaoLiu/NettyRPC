package cug.sakura.netty.rpc.serializer;

import cug.sakura.netty.rpc.exception.SerializerException;
/**
 * @program: NettyRPC
 * @description: 序列化器抽象类
 * @author: isHaitaoLiu
 * @create: 2021-12-13 20:58
 **/

public abstract class Serializer {
    public abstract <T> byte[] serialize(T obj) throws SerializerException;

    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz) throws SerializerException;
}
