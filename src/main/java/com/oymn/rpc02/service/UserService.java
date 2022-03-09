package com.oymn.rpc02.service;


import com.oymn.rpc02.dao.User;

public interface UserService {

    User getUserById(Integer id);

    Integer insertUser(User user);
}
