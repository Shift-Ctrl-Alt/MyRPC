package com.oymn.rpc03.server;

import com.oymn.rpc03.common.ServerProvide;
import com.oymn.rpc03.common.WorkThread;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

@AllArgsConstructor
public class SimpleRPCServer implements RPCServer{

    //本server所能提供的所有服务
    private ServerProvide serverProvide;

    @Override
    public void start(int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动了");

            while(true){
                Socket socket = serverSocket.accept();

                //开启线程处理，处理部分放在了WorkThread中
                new Thread(new WorkThread(socket, serverProvide)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
