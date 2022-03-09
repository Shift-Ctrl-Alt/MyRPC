package com.oymn.rpc04.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class RPCRequestMessage extends Message{

    //接口全类名
    private String interfaceName;

    //所调用的方法名
    private String methodName;

    //方法返回类型
    private Class<?> returnType;

    //方法参数类型数组
    private Class[] parameterTypes;

    //方法参数值数组
    private Object[] parameterValues;

    public RPCRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValues = parameterValue;
    }


    //获取请求类型
    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_REQUEST;
    }
}
