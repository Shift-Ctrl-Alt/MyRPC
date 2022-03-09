package com.oymn.rpc03.service;


import com.oymn.rpc03.dao.User;

public interface UserService {

    User getUserById(Integer id);

    Integer insertUser(User user);
}
