package org.example.jdbctransaction;

import org.example.jdbc.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dizhang
 * @date 2021-05-11
 */
public class TransactionalPersonTest {
    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("jdbctransaction/bean-transaction.xml");
        TransactionalPersonService personService = (TransactionalPersonService) act.getBean("transactionalPersonService");
        Person person = new Person();
        person.setName("张三 transactional");
        person.setAge(20);
        person.setSex("男");
        personService.save(person);
    }
}
