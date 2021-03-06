package org.example.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-05-08
 */
public class SpringJDBCTest {
    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("jdbc/bean.xml");
        PersonService personService = (PersonService) act.getBean("personService");
        Person person = new Person();
        person.setName("张三");
        person.setAge(20);
        person.setSex("男");
        // 保存一条记录
        personService.save(person);
        List<Person> person1 = personService.getPersons();
        System.out.println("++++++++得到所有 person");
        for (Person person2 : person1) {
            System.out.println(person2.getId() + " " + person2.getName()
                    + " " + person2.getAge() + " " + person2.getSex());
        }
    }
}
