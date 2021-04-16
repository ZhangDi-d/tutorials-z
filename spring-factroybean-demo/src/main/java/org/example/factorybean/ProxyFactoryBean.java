package org.example.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author dizhang
 * @date 2021-04-16
 */
public class ProxyFactoryBean<T> implements FactoryBean<T> {

    // 被代理的类className
    private String proxyClassName;

    public void setProxyClassName(String proxyClassName) {
        this.proxyClassName = proxyClassName;
    }

    // 返回代理类
    public T getObject() throws Exception {
        Class innerClass = Class.forName(proxyClassName);
        // 如果是接口则返回JDK动态代理
        if (innerClass.isInterface()) {
            System.out.println("innerClass.isInterface() true " + innerClass);
            return (T) InterfaceProxy.newInstance(innerClass);
        } else {
            // 返回基于CGLIB的动态代理
            System.out.println("innerClass.isInterface() false " + innerClass);
            return (T) ClassProxy.newInstance(innerClass);
        }
    }

    // 返回实际的被代理对象类型，当调用getBean(xxx.class)时进行类型匹配
    public Class<?> getObjectType() {
        try {
            return Class.forName(proxyClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 默认是单例
    public boolean isSingleton() {
        return true;
    }

}