package com.oymn.rpc04.config;

import com.oymn.rpc04.protocol.Serializer;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static Properties properties;

    static{
        try {
            //加载配置文件
            InputStream in = Config.class.getResourceAsStream("/application.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取配置文件中的序列化方式
    public static Serializer.Algorithm getSerializerAlgorithm(){
        String serializer = properties.getProperty("serializer.algorithm");

        //默认是JDK方式
        if(serializer == null){
            return Serializer.Algorithm.JAVA;
        }else{
            return Serializer.Algorithm.valueOf(serializer);
        }
    }
}
