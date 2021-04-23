package org.example.test;

import org.example.AbstractTest;
import org.example.entity.Car;
import org.example.entity.MyTestBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author dizhang
 * @date 2021-04-23
 */


public class SpringBeanTest extends AbstractTest {

    @Test
    @SuppressWarnings("deprecation")
    void test1() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml")); // bean 解析
        MyTestBean bean = (MyTestBean) bf.getBean("myTestBean"); // bean 加载 //、、。。
        assertEquals("testStr", bean.getTestStr());
    }

    @Test
    @SuppressWarnings("deprecation")
    void factoryBeanTest() {
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
        //bean from FactoryBean
        Car car = (Car) bf.getBean("car");
        System.out.println(car.toString());
        //bean from Bean
        Car car1 = (Car) bf.getBean("car1");
        System.out.println(car1.toString());
    }
}
