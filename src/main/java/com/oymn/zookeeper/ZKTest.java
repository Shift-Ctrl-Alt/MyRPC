package com.oymn.zookeeper;

import lombok.SneakyThrows;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ZKTest {

    ZooKeeper zkClient = null;

    @Before
    public void test01() throws IOException, InterruptedException, KeeperException {
        zkClient = new ZooKeeper("192.168.10.10:2181", 200000, new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                System.out.println("-----------------------");
                System.out.println("监听到变化了");
                List<String> children = zkClient.getChildren("/", true);
                for (String child : children) {
                    System.out.println(child);
                }
            }
        });


    }

    @Test
    public void test02() throws InterruptedException, KeeperException {

        String node = zkClient.create("/oymn", "chenhongrong".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.CONTAINER);
    }

    @Test
    public void test03() throws InterruptedException, KeeperException {

        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }

        while(true);
    }

    @Test
    public void test04() throws InterruptedException, KeeperException {
        Stat stat = zkClient.exists("/oymn", false);
        if (stat==null) {
            System.out.println("不存在该结点");
        }else System.out.println("存在该节点");
    }
}
