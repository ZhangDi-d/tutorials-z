package org.example.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author dizhang
 * @date 2021-05-06
 */
@Configuration
@EnableAspectJAutoProxy
public class Config {

    @Bean
    public AspectJTest aspectJTest() {
        return new AspectJTest();
    }

    @Bean
    public TestBean testBean(){
        return new TestBean();
    }
}
