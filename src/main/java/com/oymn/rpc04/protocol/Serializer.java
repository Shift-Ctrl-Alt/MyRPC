package com.oymn.rpc04.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Serializer {

    //序列化
    <T> byte[] serialize(T object);

    //反序列化
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    enum Algorithm implements Serializer{

        //使用原生jdk序列化方式
        JAVA{
            @Override
            public <T> byte[] serialize(T object) {

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败",e);
                }

            }

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {

                try {
                    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(in);
                    return (T) ois.readObject();

                } catch (Exception e) {
                    throw new RuntimeException("反序列化失败",e);
                }
            }
        },
        //基于JSON序列化方式（fastjson）
        JSON{
            @Override
            public <T> byte[] serialize(T object) {

                String json = com.alibaba.fastjson.JSON.toJSONString(object);
                return json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {

                String json = new String(bytes, StandardCharsets.UTF_8);
                return com.alibaba.fastjson.JSON.parseObject(json,clazz);
            }
        }
    }

}
