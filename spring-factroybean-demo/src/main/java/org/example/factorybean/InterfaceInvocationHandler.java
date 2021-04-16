package org.example.factorybean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author dizhang
 * @date 2021-04-16
 * 用来处理接口方法调用的InvocationHandler实例  ,完全的拆开代码，方便理解
 */
public class InterfaceInvocationHandler implements InvocationHandler {

    private final Object target;

    public InterfaceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("ObjectProxy execute:" + method.getName());
        return method.invoke(target, args);
    }
}
