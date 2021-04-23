package org.example.test;

import org.example.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author dizhang
 * @date 2021-04-23
 */
public class CircularReferenceTest extends AbstractTest {


    @Test
    void circularReferenceTest() {
        try {
            new ClassPathXmlApplicationContext("circularReference.xml");
        } catch (Exception e) {
            //因为要在创建 testC 时抛出；
            Throwable e1 = e.getCause().getCause().getCause();
            try {
                throw e1;
            } catch (Throwable throwable) {

            }
            Exception exception = assertThrows(BeanCurrentlyInCreationException.class, () -> e.getCause().getCause().getCause());
            System.out.println(exception.getMessage());
        }
    }
}
