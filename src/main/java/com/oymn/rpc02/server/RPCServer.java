package com.oymn.rpc02.server;


import com.oymn.rpc02.dao.User;
import com.oymn.rpc02.service.UserService;
import com.oymn.rpc02.service.UserServiceImpl;
import com.oymn.rpc02.vo.Request;
import com.oymn.rpc02.vo.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

                        //解析request
                        Request request = (Request) objectInputStream.readObject();
                        String interfaceName = request.getInterfaceName();
                        String methodName = request.getMethodName();
                        Object[] params = request.getParams();
                        Class<?>[] paramsTypes = request.getParamsTypes();

                        //反射，调用相应的方法
                        Method method = userService.getClass().getMethod(methodName, paramsTypes);
                        Object invoke = method.invoke(userService, params);

                        //写入response对象，返回给客户端
                        objectOutputStream.writeObject(Response.success(invoke));
                        objectOutputStream.flush();

                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
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
