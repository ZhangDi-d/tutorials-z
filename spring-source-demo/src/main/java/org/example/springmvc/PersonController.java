package org.example.springmvc;

import org.example.jdbc.Person;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dizhang
 * @date 2021-05-11
 */
public class PersonController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        List<Person> personList = new ArrayList<>();
        Person personA = new Person();
        Person personB = new Person();
        personA.setName("张三");
        personA.setAge(27);
        personB.setName("李四");
        personB.setAge(37);
        personList.add(personA);
        personList.add(personB);
        return new ModelAndView("personlist", "persons", personList);
    }
}

