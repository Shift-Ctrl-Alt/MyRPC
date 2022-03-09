package com.oymn.rpc04.server;

import com.oymn.rpc04.handler.RPCRequestMessageHandler;
import com.oymn.rpc04.handler.RPCResponseMessageHandler;
import com.oymn.rpc04.protocol.MessageCodec;
import com.oymn.rpc04.service.ServiceFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceFactory serviceFactory;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //解决黏包半包问题
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0));
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        //编码和解码
        ch.pipeline().addLast(new MessageCodec());
        //处理RPC请求
        ch.pipeline().addLast(new RPCRequestMessageHandler(serviceFactory));
    }



}
