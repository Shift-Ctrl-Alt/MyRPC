package com.oymn.rpc02.client;
import com.oymn.rpc02.dao.User;
import com.oymn.rpc02.service.UserService;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class RPCClient {

    public static void main(String[] args) {

        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);

        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserById(10);
        System.out.println("查询用户得到：" + user);

        User user2 = User.builder().username("张三").sex(true).id(20).build();
        Integer id = proxy.insertUser(user2);
        System.out.println("向服务端插入数据：" + id);

    }
}
