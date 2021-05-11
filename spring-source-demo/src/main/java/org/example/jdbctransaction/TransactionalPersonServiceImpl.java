package org.example.jdbctransaction;

import org.example.jdbc.Person;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author dizhang
 * @date 2021-05-10
 */
public class TransactionalPersonServiceImpl implements TransactionalPersonService {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Person person) {
        jdbcTemplate.update("insert into person(name,age,sex)values(?,?,?)",
                new Object[]{person.getName(), person.getAge(),
                        person.getSex()}, new int[]{java.sql.Types.VARCHAR,
                        java.sql.Types.INTEGER, java.sql.Types.VARCHAR});
        //事务测试，加上这句代码则数据不会保存到数据库中
        throw new RuntimeException("error");
    }
}
