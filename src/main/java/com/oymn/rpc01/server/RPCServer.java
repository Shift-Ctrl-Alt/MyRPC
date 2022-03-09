package com.oymn.rpc01.server;

import com.oymn.rpc01.dao.User;
import com.oymn.rpc01.service.UserService;
import com.oymn.rpc01.service.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {


    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端开启了");

            //BIO的方式监听socket
            while(true){
                Socket socket = serverSocket.accept();

                new Thread(()->{
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                        int id = objectInputStream.readInt();

                        User user = userService.getUserById(id);
                        objectOutputStream.writeObject(user);
                        objectOutputStream.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                }).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务启动失败");
        }

    }
}
