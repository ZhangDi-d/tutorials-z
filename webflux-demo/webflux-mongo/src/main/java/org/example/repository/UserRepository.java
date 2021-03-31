package org.example.repository;

import org.example.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

/**
 * @author dizhang
 * @date 2021-03-19
 */
public interface UserRepository extends ReactiveMongoRepository<User, BigInteger> {

    Mono<User> findByUsername(String name);

    Mono<Void> deleteByUsername(String name);
}
