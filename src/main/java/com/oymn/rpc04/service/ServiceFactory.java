package com.oymn.rpc04.service;

import com.oymn.rpc04.config.Config;
import com.oymn.rpc04.register.ServiceRegister;
import com.oymn.rpc04.register.ZKServiceRegister;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {

    private Properties properties = Config.properties;

    private Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private ServiceRegister serviceRegister;

    private String host;
    private int port;


    public ServiceFactory(String host, int port, ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
        this.host = host;
        this.port = port;
    }

    public ServiceFactory initServiceFactory() {
        try {
            Set<String> propertyNames = properties.stringPropertyNames();
            for (String interfaceName : propertyNames) {
                //以Service结尾
                if (interfaceName.endsWith("Service")) {
                    //获取接口的Class
                    Class<?> interfaceClass = Class.forName(interfaceName);
                    //获取接口实现类的Class
                    Class<?> instanceClass = Class.forName(properties.getProperty(interfaceName));
                    //放入map中
                    services.put(interfaceClass, instanceClass.newInstance());

                    //向Zookeeper中注册服务
                    serviceRegister.register(interfaceName, new InetSocketAddress(host,port));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;

    }

    //根据接口获取服务
    public <T> T getService(Class<T> interfaceClass) {
        return (T) services.get(interfaceClass);
    }
}
