package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author dizhang
 * @date 2021-03-31
 */
@DataR2dbcTest
public class UserServiceTest2 {

    @Autowired
    DatabaseClient client;

    @Autowired
    UserRepository userRepository;


    @Test
    void test1(){
        Flux<User> all = userRepository.findAll();
        StepVerifier.create(all)
                .consumeNextWith(user -> System.out.println(user.toString()))
                .consumeNextWith(user -> System.out.println(user.toString()))
                .consumeNextWith(user -> System.out.println(user.toString()))
                .consumeNextWith(user -> System.out.println(user.toString()))
                .verifyComplete();


    }


}
