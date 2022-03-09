package com.oymn.rpc03.server;

import com.oymn.rpc03.common.ServerProvide;
import com.oymn.rpc03.common.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadRPCServer implements RPCServer {

    private ServerProvide serverProvide;

    //线程池，必须要初始化，不然会报错
    private final ThreadPoolExecutor threadPool;

    public ThreadRPCServer(ServerProvide serverProvide) {
        this.serverProvide = serverProvide;

        this.threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
    }

    public ThreadRPCServer(ServerProvide serverProvide, int corePoolSize,
                           int maximumPoolSize, long keepAliveTime,
                           TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue){

        this.serverProvide = serverProvide;

        this.threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, blockingQueue);
    }


    @Override
    public void start(int port) {
        System.out.println("线程池版本的服务端启动了");

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while(true){
                Socket socket = serverSocket.accept();

                new Thread(new WorkThread(socket,serverProvide)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
