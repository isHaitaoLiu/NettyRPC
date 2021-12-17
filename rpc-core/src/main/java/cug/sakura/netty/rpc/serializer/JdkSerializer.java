package cug.sakura.netty.rpc.serializer;

import cug.sakura.netty.rpc.exception.SerializerException;

import java.io.*;

/**
 * @program: NettyRPC
 * @description: JDK序列化方式
 * @author: isHaitaoLiu
 * @create: 2021-12-17 10:36
 **/

public class JdkSerializer extends Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws SerializerException {
        byte[] bytes;
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(obj);
            bytes = byteArrayOutputStream.toByteArray();
        }catch (IOException ioe){
            throw new SerializerException(ioe.getMessage(), ioe.getCause());
        }
        return bytes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) throws SerializerException{
        T obj;

        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
            Object readObj = objectInputStream.readObject();
            if (!clazz.isInstance(readObj)){
                throw new SerializerException("序列化类型不匹配");
            }else {
                obj = (T) readObj;
            }
        }catch (IOException | ClassNotFoundException ioe){
            throw new SerializerException(ioe.getMessage(), ioe.getCause());
        }
        return obj;
    }
}
