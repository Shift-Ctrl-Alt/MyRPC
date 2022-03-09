package com.oymn.rpc03.client;

import com.oymn.rpc03.vo.Request;
import com.oymn.rpc03.vo.Response;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    private String host;

    private int port;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Request request = Request.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramsTypes(method.getParameterTypes())
                .params(args)
                .build();

        Response response = IOClient.sendRequest(host, port, request);

        return response.getData();
    }

    <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
}
