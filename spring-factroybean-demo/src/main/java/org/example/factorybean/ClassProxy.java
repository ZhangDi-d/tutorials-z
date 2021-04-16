package org.example.factorybean;

import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author dizhang
 * @date 2021-04-16
 * cglib dynamic proxy
 */
public class ClassProxy {

    public static <T> T newInstance(Class<T> innerClass) {
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(innerClass);
        // 设置类名生成策略
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        // 设置enhancer的回调对象
        enhancer.setCallback(new MethodInterceptorImpl());
        // 创建代理对象
        return (T) enhancer.create();
    }
}
