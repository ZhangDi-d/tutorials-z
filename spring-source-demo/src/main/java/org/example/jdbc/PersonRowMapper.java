package org.example.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dizhang
 * @date 2021-05-08
 */
public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet set, int i) throws SQLException {
        Person person = new Person(set.getInt("id"), set.getString("name"), set
                .getInt("age"), set.getString("sex"));
        return person;
    }
}
