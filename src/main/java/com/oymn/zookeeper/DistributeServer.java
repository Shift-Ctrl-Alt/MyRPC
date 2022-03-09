package com.oymn.zookeeper;

import lombok.SneakyThrows;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DistributeServer {

    private ZooKeeper zkClient = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeServer server = new DistributeServer();

        //获取zk连接
        server.getConnect();

        //注册服务器到zk中
        server.registerServer(args[0]);

        while (true);

    }

    //注册服务器
    public void registerServer(String hostname) throws InterruptedException, KeeperException {
        zkClient.create("/servers/" + hostname,hostname.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(hostname + "已经上线了");
    }

    private void getConnect() throws IOException {

        zkClient = new ZooKeeper("192.168.10.10:2181", 200000, new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                System.out.println("-----------------------");
                System.out.println("监听到变化了");
                List<String> children = zkClient.getChildren("/servers", true);
                for (String child : children) {
                    System.out.println(child);
                }
            }
        });
    }
}
