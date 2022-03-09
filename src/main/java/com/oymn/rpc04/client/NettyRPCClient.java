package com.oymn.rpc04.client;

import com.oymn.rpc04.common.Message;
import com.oymn.rpc04.common.RPCRequestMessage;
import com.oymn.rpc04.common.RPCResponseMessage;
import com.oymn.rpc04.handler.RPCResponseMessageHandler;
import com.oymn.rpc04.loadbalance.RandomLoadBalance;
import com.oymn.rpc04.register.ServiceRegister;
import com.oymn.rpc04.register.ZKServiceRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@AllArgsConstructor
@Slf4j
public class NettyRPCClient implements RPCClient {

    private String host;
    private int port;

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private ServiceRegister serviceRegister = new ZKServiceRegister("MYRPC",new RandomLoadBalance());


    //客户端初始化
    static {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }


    //发送请求并获取结果
    @Override
    public Object sendRequest(Message message) {
        try {
            RPCRequestMessage requestMessage = (RPCRequestMessage) message;
            //通过接口名来选择一个服务器，获取其地址和端口号
            InetSocketAddress address = serviceRegister.serviceDiscovery(requestMessage.getInterfaceName());
            Channel channel = bootstrap.connect(address.getAddress(), address.getPort()).sync().channel();

            //异步监听连接，当关闭后就执行以下代码
            channel.closeFuture().addListener(future -> {
                eventLoopGroup.shutdownGracefully();
            });

            //准备一个空的Promise对象，来接受结果
            DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
            RPCResponseMessageHandler.PROMISES.put(message.getSequenceId(), promise);

            //将请求发送出去
            channel.writeAndFlush(message);


            //等待promise结果
            promise.await();

            if (promise.isSuccess()) {
                log.debug("promise成功了，获取方法返回值");
                //调用正常
                return promise.getNow();
            } else {
                log.debug("promise失败了，抛异常");
                //调用失败
                throw new RuntimeException(promise.cause());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
