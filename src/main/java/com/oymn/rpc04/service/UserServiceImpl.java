package com.oymn.rpc04.service;


import com.oymn.rpc04.dao.User;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Integer id) {
        System.out.println("客户端查询了" + id + "的用户");
        Random random = new Random();
        User user = User.builder().username(UUID.randomUUID().toString()).id(id).sex(random.nextBoolean()).build();
        return user;
    }

    @Override
    public Integer insertUser(User user) {
        System.out.println("插入用户成功:" + user);
        return user.getId();
    }
}
