package com.oymn.rpc02.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class Request implements Serializable {

    //接口名
    private String interfaceName;

    //方法名
    private String methodName;

    //参数列表
    private Object[] params;

    //参数类型
    private Class<?>[] paramsTypes;
}
