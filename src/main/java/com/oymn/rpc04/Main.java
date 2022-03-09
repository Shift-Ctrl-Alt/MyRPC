package com.oymn.rpc04;

import com.oymn.rpc04.client.NettyRPCClient;
import com.oymn.rpc04.client.NettyRPCClientProxy;
import com.oymn.rpc04.dao.User;
import com.oymn.rpc04.service.UserService;

public class Main {

    public static void main(String[] args) {

        NettyRPCClient nettyRPCClient = new NettyRPCClient("localhost", 9090);
        NettyRPCClientProxy clientProxy = new NettyRPCClientProxy(nettyRPCClient);

        UserService userService = clientProxy.getProxy(UserService.class);
        User user = userService.getUserById(2);
        System.out.println(user);

    }
}
