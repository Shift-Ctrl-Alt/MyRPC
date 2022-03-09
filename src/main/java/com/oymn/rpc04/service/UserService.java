package com.oymn.rpc04.service;


import com.oymn.rpc04.dao.User;

public interface UserService {

    User getUserById(Integer id);

    Integer insertUser(User user);
}
