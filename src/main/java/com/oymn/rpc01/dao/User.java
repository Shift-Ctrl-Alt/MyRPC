package com.oymn.rpc01.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    //用户id
    private Integer id;

    //用户名
    private String username;


    private Boolean sex;
}
