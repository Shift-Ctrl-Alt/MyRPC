package com.oymn.zookeeper;

import lombok.SneakyThrows;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributeClient {

    ZooKeeper zkClient = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        //获取zk连接
        DistributeClient client = new DistributeClient();
        client.getConnect();

        //监听servers下的结点的增加和删除
        //ArrayList<String> servers = client.getServerList();
        System.out.println("获取所有有效服务：");
        //System.out.println(servers);

        while (true);
    }

    private ArrayList<String> getServerList() throws InterruptedException, KeeperException {

        ArrayList<String> servers = new ArrayList<>();

        List<String> children = zkClient.getChildren("/servers", true);
        for (String child : children) {
            byte[] data = zkClient.getData("/servers/" + child, false, null);
            servers.add(new String(data));
        }

        return servers;
    }

    private void getConnect() throws IOException {
        zkClient = new ZooKeeper("192.168.10.10:2181", 200000, new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {

                //监听servers下的结点的增加和删除
                ArrayList<String> servers = getServerList();
                System.out.println("获取所有有效服务：");
                System.out.println(servers);
            }
        });
    }
}
