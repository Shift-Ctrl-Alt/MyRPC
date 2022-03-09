package com.oymn.rpc01.client;

import com.oymn.rpc01.dao.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class RPCClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            //传给服务器id
            objectOutputStream.writeInt(new Random().nextInt());
            objectOutputStream.flush();

            //接受服务器的返回值
            User user = (User) objectInputStream.readObject();
            System.out.println("服务端返回的User：" + user);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
