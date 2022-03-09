package com.oymn.rpc03.client;

import com.oymn.rpc03.vo.Request;
import com.oymn.rpc03.vo.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//将底层通信过程解耦出来
public class IOClient {

    public static Response sendRequest(String host, int port, Request request){

        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(request);

            Response response = (Response) objectInputStream.readObject();
            objectOutputStream.flush();

            return response;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
