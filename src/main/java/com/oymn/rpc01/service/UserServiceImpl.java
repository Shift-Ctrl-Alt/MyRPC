package com.oymn.rpc01.service;

import com.oymn.rpc01.dao.User;

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
}
