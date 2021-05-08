package org.example.test;

import org.example.aop.jdk.MyInvocationHandler;
import org.example.aop.jdk.UserService;
import org.example.aop.jdk.UserServiceImpl;
import org.junit.jupiter.api.Test;

/**
 * @author dizhang
 * @date 2021-05-07
 */
public class JdkProxyTest {
    @Test
    void test1() {
        UserServiceImpl userService = new UserServiceImpl();
        MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) invocationHandler.getProxy();
        proxy.add();
    }
}
