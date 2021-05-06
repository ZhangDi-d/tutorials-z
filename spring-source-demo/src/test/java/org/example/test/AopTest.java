package org.example.test;

import org.example.AbstractTest;
import org.example.aop.TestBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author dizhang
 * @date 2021-05-06
 */
public class AopTest extends AbstractTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void test1() {
        TestBean bean = (TestBean) applicationContext.getBean("testBean");
        bean.test();
    }

}
