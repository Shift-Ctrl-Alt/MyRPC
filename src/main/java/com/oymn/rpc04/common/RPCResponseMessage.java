package com.oymn.rpc04.common;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class RPCResponseMessage extends Message{

    //返回值
    private Object returnValue;

    //异常值
    private Exception exceptionValue;


    //获取请求类型
    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
