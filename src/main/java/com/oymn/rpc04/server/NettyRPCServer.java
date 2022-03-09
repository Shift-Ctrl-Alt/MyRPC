package com.oymn.rpc04.server;

import com.oymn.rpc04.register.ZKServiceRegister;
import com.oymn.rpc04.service.ServiceFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyRPCServer implements RPCServer{

    private ServiceFactory serviceFactory;

    public static void main(String[] args) {
        ZKServiceRegister zkServiceRegister = new ZKServiceRegister("MYRPC");
        ServiceFactory serviceFactory = new ServiceFactory("192.168.10.10", 2181, zkServiceRegister);
        new NettyRPCServer(serviceFactory).start(9090);
    }

    @Override
    public void start(int port) {
        //boss负责连接，worker负责具体的请求
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceFactory))
                    .bind(port)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
