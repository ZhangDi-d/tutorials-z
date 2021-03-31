package org.example.repository;

import org.example.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author dizhang
 * @date 2021-03-26
 */
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

}
