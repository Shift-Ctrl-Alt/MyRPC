package com.oymn.rpc04.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    //消息序号
    private int sequenceId;

    //消息类型
    private int messageType;

    //请求类型的byte值
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;

    //响应类型的byte值
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 102;

    //获取消息类型
    public abstract int getMessageType();

    //存放消息类型和消息class之间的关系
    private static final Map<Integer,Class<? extends Message>> messageClass = new HashMap<>();

    static {
        messageClass.put(RPC_MESSAGE_TYPE_REQUEST,RPCRequestMessage.class);
        messageClass.put(RPC_MESSAGE_TYPE_RESPONSE,RPCResponseMessage.class);
    }

    //根据消息类型获取消息的class
    public static Class<? extends Message> getMessageClass(int messageType){
        return messageClass.get(messageType);
    }

}
