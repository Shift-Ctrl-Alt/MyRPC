package com.oymn.rpc04.register;

import java.net.InetSocketAddress;

//注册服务的接口
public interface ServiceRegister {

    //注册服务：保存服务和地址
    void register(String serviceName, InetSocketAddress serverAddress);

    //服务发现：根据服务名查找地址
    InetSocketAddress serviceDiscovery(String serviceName);


}
