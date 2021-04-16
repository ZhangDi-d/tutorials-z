package org.example.factorybean;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author dizhang
 * @date 2021-04-16
 *
 * 通过CGLIB代理实现如下：
 *
 * 首先实现一个MethodInterceptor，方法调用会被转发到该类的intercept()方法。
 * 然后在需要使用HelloConcrete的时候，通过CGLIB动态代理获取代理对象。
 */
public class MethodInterceptorImpl implements MethodInterceptor {

    /**
     * 被代理类的所有非final方法都会被转交给intercept处理
     * o：cglib生成的代理对象
     * method：被代理对象方法
     * objects：方法入参
     * methodProxy: 代理方法
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("MethodInterceptorImpl:" + method.getName());
        return methodProxy.invokeSuper(o, objects);
    }

}
/*
    cglib包里也存在一个MethodInterceptor，它的主要作用是CGLIB内部使用，一般是和Enhancer一起来使用而创建一个动态代理对象。
    而本处我们讲到的 org.aopalliance.intercept.MethodInterceptor，那些@AspectJ定义的通知们（增强器们），
    或者是自己实现的MethodBeforeAdvice、AfterReturningAdvice…(总是都是org.aopalliance.aop.Advice一个通知器)，
    最终都会被包装成一个org.aopalliance.intercept.MethodInterceptor，最终交给MethodInvocation（其子类ReflectiveMethodInvocation）去执行，
    它会把你所有的增强器都给执行了，这就是我们面向切面编程的核心思路过程。
*/
