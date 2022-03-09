package com.oymn.rpc04.client;

import com.oymn.rpc04.common.Message;

//客户端统一接口
public interface RPCClient {

    Object sendRequest(Message message);
}
