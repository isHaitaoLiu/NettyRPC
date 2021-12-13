package cug.sakura.serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: NettyRPC
 * @description: protostuff序列化器
 * @author: isHaitaoLiu
 * @create: 2021-12-13 21:00
 **/

public class ProtostuffSerializer extends Serializer{
    //Schema是序列化对象的缓存组织结构
    private final Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    //设置缓冲区，避免每次序列化都重新申请Buffer空间
    private static final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    /*
     * @Author sakura
     * @Description 获取序列化对象的组织结构
     * @Date 2021/12/13
     * @Param [cls]
     * @return com.dyuproject.protostuff.Schema<T>
     **/
    @SuppressWarnings("unchecked")
    private <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema != null) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] serialize(T obj) {
        //解析类型，得到其缓存的组织结构
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try {
            //将其序列化
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        //解析类型，得到其缓存的组织结构
        Schema<T> schema = getSchema(clazz);
        //创建对象
        T obj = schema.newMessage();
        //反序列化
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}
