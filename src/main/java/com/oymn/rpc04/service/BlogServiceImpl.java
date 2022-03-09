package com.oymn.rpc04.service;

import com.oymn.rpc04.dao.Blog;

import java.util.Random;

public class BlogServiceImpl implements BlogService {

    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").userId(new Random().nextInt()).build();
        System.out.println("客户端查询了" + id + "博客");
        return blog;
    }
}
