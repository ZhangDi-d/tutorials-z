package org.example.aop.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author dizhang
 * @date 2021-05-07
 */
public class MyInvocationHandler implements InvocationHandler {


    //1. 构造函数，将代理的对象传入;
    //2. invoke 方法，此方法中实现了 AOP 增强的所有逻辑;
    //3. getProxy 方法，此方法千篇一律，但是必不可少;

    private Object target;

    public MyInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("=====before=====");
        Object result = method.invoke(target, args);
        System.out.println("=====after======");
        return result;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }
}
