package com.oymn.rpc04.register;

import com.oymn.rpc04.loadbalance.LoadBalance;
import com.oymn.rpc04.loadbalance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.security.Policy;
import java.util.List;

@Slf4j
public class ZKServiceRegister implements ServiceRegister{

    //curator提供的zookeeper客户端
    private CuratorFramework client;

    //zookeeper根路径结点
    private final String ROOT_PATH ;

    //初始化负载均衡算法，默认是随即算法
    private LoadBalance loadBalance = new RandomLoadBalance();

    //初始化zookeeper客户端
    public ZKServiceRegister(String ROOT_PATH){
        this.ROOT_PATH = ROOT_PATH;

        //指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);

        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        //使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder().connectString("192.168.10.10:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();

        log.debug("zookeeper 连接成功");
    }

    //初始化zookeeper客户端
    public ZKServiceRegister(String ROOT_PATH, LoadBalance loadBalance){

        this(ROOT_PATH);
        this.loadBalance = loadBalance;
    }

    //注册服务
    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            /*节点路径格式：
                    /ROOT_PATH
                              /serviceName
                                          /192.168.10.10
                                          /192.168.10.11
                                          /xxx.xxx.xxx.xxx
            */

            //如果serviceName结点不存在，则创建
            if(client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }

            String path = "/" + serviceName + "/" + getServiceAddress(serverAddress);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            log.debug("服务已存在");
        }
    }

    //服务发现
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {

        try {
            List<String> services = client.getChildren().forPath("/" + serviceName);

            //使用负载均衡进行选择
            String address = loadBalance.balance(services);
            return parseAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //将字符串解析成地址
    private InetSocketAddress parseAddress(String address) {

        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }


    // 地址 -> XXX.XXX.XXX.XXX:port
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

}
