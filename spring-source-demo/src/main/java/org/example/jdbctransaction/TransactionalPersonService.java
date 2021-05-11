package org.example.jdbctransaction;

import org.example.jdbc.Person;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dizhang
 * @date 2021-05-10
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public interface TransactionalPersonService {

    void save(Person person);
}
