package org.example.factorybean;

import org.example.entity.Person;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author dizhang
 * @date 2021-04-16
 */
public class TestMain {
    public static void main(String[] args) {
        testInterface();
        //testClass();
    }

    private static void testInterface() {
        // 创建IOC容器上下文
        GenericApplicationContext context = new GenericApplicationContext();

        // 定义GenericBeanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.getPropertyValues().add("proxyClassName", Person.class.getName());
        beanDefinition.setBeanClass(ProxyFactoryBean.class);
        // 向容器注入Bean并刷新上下文
        context.registerBeanDefinition("personBean", beanDefinition);
        context.refresh();

        // 从容器中获取代理proxyBean
        Person studentProxyBean = context.getBean(Person.class);

        // 执行代理Bean的方法
        studentProxyBean.sayHi();

    }

    private static void testClass() {
        // 创建IOC容器上下文
        GenericApplicationContext context = new GenericApplicationContext();

        // 定义GenericBeanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.getPropertyValues().add("proxyClassName", Teacher.class.getName());
        beanDefinition.setBeanClass(ProxyFactoryBean.class);
        context.registerBeanDefinition("teacherBean", beanDefinition);
        context.refresh();

        // 从容器中获取代理proxyBean
        Teacher teacherProxyBean = context.getBean(Teacher.class);

        // 执行代理Bean的方法
        teacherProxyBean.sayHi();
    }


}
