package com.oymn.rpc04.handler;

import com.oymn.rpc04.common.Message;
import com.oymn.rpc04.common.RPCResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//客户端处理响应的handler
@Slf4j
public class RPCResponseMessageHandler extends SimpleChannelInboundHandler<RPCResponseMessage> {

    //用来存放对客户端的响应信息
    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponseMessage msg) throws Exception {
        log.debug("哈哈哈哈哈{}", msg);
        // 拿到空的 promise
        Promise<Object> promise = PROMISES.remove(msg.getSequenceId());
        if (promise != null) {
            Object returnValue = msg.getReturnValue();
            Exception exceptionValue = msg.getExceptionValue();
            if(exceptionValue != null) {
                promise.setFailure(exceptionValue);
            } else {
                promise.setSuccess(returnValue);
            }
        }
    }
}
