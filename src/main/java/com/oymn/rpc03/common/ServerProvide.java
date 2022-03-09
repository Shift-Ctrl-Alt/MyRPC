package com.oymn.rpc03.common;


import java.util.HashMap;
import java.util.Map;

public class ServerProvide {

    private Map<String, Object> interfaceProvide;

    public ServerProvide(){
        this.interfaceProvide = new HashMap<>();
    }

    public void addServerInterface(Object service){

        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> clazz :interfaces){
            interfaceProvide.put(clazz.getName(), service);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvide.get(interfaceName);
    }

    /*public static void main(String[] args) {
        System.out.println(UserService.class);
        System.out.println(UserServiceImpl.class);
        UserService userService = new UserServiceImpl();
        System.out.println(userService.getClass());

    }*/
}
