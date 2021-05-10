package org.example.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author dizhang
 * @date 2021-05-08
 */
public class PersonServiceImpl implements PersonService {


    private JdbcTemplate jdbcTemplate;

    // 设置数据源
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Person person) {
        jdbcTemplate.update("insert into person(name,age,sex)values(?,?,?)",
                new Object[]{person.getName(), person.getAge(),
                        person.getSex()}, new int[]{java.sql.Types.VARCHAR,
                        java.sql.Types.INTEGER, java.sql.Types.VARCHAR});
    }

    @Override
    public List<Person> getPersons() {
        return jdbcTemplate.query("select * from person", new PersonRowMapper());
    }
}
