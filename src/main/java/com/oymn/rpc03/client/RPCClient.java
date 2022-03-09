package com.oymn.rpc03.client;
import com.oymn.rpc03.dao.Blog;
import com.oymn.rpc03.dao.User;
import com.oymn.rpc03.service.BlogService;
import com.oymn.rpc03.service.UserService;

public class RPCClient {

    public static void main(String[] args) {

        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);

        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserById(10);
        System.out.println("查询用户得到：" + user);

        User user2 = User.builder().username("张三").sex(true).id(20).build();
        Integer id = proxy.insertUser(user2);
        System.out.println("向服务端插入数据：" + id);

        BlogService blogService = clientProxy.getProxy(BlogService.class);
        Blog blog = blogService.getBlogById(200);
        System.out.println(blog);

    }
}
