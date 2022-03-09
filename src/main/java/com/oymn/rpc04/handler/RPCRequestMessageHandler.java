package com.oymn.rpc04.handler;

import ch.qos.logback.classic.Logger;
import com.oymn.rpc04.common.RPCRequestMessage;
import com.oymn.rpc04.common.RPCResponseMessage;
import com.oymn.rpc04.register.ServiceRegister;
import com.oymn.rpc04.service.ServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//处理请求信息
@Slf4j
@AllArgsConstructor
public class RPCRequestMessageHandler extends SimpleChannelInboundHandler<RPCRequestMessage> {

    private ServiceFactory serviceFactory ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequestMessage msg) throws Exception {
        //响应消息
        RPCResponseMessage responseMessage = new RPCResponseMessage();

        log.debug("进入requestHandler");

        try {
            //获取服务
            Class<?> interfaceClass = Class.forName(msg.getInterfaceName());
            Object service = serviceFactory.getService(interfaceClass);
            //反射出所调用方法
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValues());

            log.debug("处理请求的handler中，得出方法返回值");
            //响应消息
            responseMessage.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getCause().getMessage();
            responseMessage.setExceptionValue(new Exception("远程调用错误：" + errorMessage));
        }

        log.debug("发送方法的返回值");

        ctx.writeAndFlush(responseMessage);


    }
}
