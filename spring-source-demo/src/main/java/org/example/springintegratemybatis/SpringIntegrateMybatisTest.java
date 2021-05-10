package org.example.springintegratemybatis;

import org.example.independentmybatis.StudentMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dizhang
 * @date 2021-05-10
 */
public class SpringIntegrateMybatisTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("springintegratemybatis/spring-mybatis-config.xml");
        StudentMapper StudentDao = (StudentMapper) context.getBean("studentMapper");
        System.out.println(StudentDao.getStudent(1));
    }
}
