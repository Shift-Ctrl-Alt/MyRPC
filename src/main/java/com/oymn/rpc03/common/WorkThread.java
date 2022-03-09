package com.oymn.rpc03.common;

import com.oymn.rpc03.vo.Request;
import com.oymn.rpc03.vo.Response;
import jdk.nashorn.internal.ir.RuntimeNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

@AllArgsConstructor
public class WorkThread implements Runnable {

    private Socket socket;

    private ServerProvide serverProvide;

    @Override
    public void run() {

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            Request request = (Request) objectInputStream.readObject();
            Response response = getResponse(request);

            objectOutputStream.writeObject(response);
            objectOutputStream.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Response getResponse(Request request) {
        String interfaceName = request.getInterfaceName();
        String methodName = request.getMethodName();
        Class<?>[] paramsTypes = request.getParamsTypes();
        Object[] params = request.getParams();

        try {
            Object server = serverProvide.getService(interfaceName);
            Method method = server.getClass().getMethod(methodName, paramsTypes);
            Object invoke = method.invoke(server, params);

            return Response.success(invoke);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return Response.fail();
    }
}
