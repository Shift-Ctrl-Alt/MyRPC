package com.oymn.rpc04.client;

import com.oymn.rpc04.common.Message;
import com.oymn.rpc04.common.RPCRequestMessage;
import com.oymn.rpc04.common.RPCResponseMessage;
import com.oymn.rpc04.common.SequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

@AllArgsConstructor
@Slf4j
public class NettyRPCClientProxy {

    private NettyRPCClient nettyRPCClient;

    //获取代理类
    public <T> T getProxy(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        //获得代理类
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 1. 将方法调用转换为 消息对象
            int sequenceId = SequenceIdGenerator.nextId();

            //请求消息
            RPCRequestMessage msg = new RPCRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );

            //将消息发送给服务器，并获得方法返回值
            log.debug("getProxy()方法：发送请求");
            return nettyRPCClient.sendRequest(msg);

        });

        return (T) o;
    }
}
