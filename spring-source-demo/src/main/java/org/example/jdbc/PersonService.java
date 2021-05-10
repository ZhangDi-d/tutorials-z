package org.example.jdbc;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-05-08
 */
public interface PersonService {
    public void save(Person person);
    public List<Person> getPersons();
}
