package com.oymn.rpc03;


import com.oymn.rpc03.common.ServerProvide;
import com.oymn.rpc03.server.SimpleRPCServer;
import com.oymn.rpc03.server.ThreadRPCServer;
import com.oymn.rpc03.service.BlogService;
import com.oymn.rpc03.service.BlogServiceImpl;
import com.oymn.rpc03.service.UserService;
import com.oymn.rpc03.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServerProvide serverProvide = new ServerProvide();
        serverProvide.addServerInterface(userService);
        serverProvide.addServerInterface(blogService);

        //使用简单版本的服务端
        //SimpleRPCServer simpleRPCServer = new SimpleRPCServer(serverProvide);
        //simpleRPCServer.start(8899);

        //使用线程池版本的服务端
        ThreadRPCServer threadRPCServer = new ThreadRPCServer(serverProvide);
        threadRPCServer.start(8899);
    }
}
