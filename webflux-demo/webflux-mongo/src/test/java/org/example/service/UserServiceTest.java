package org.example.service;

import org.example.AbstractTest;
import org.example.entity.Profile;
import org.example.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author dizhang
 * @date 2021-03-19
 */
class UserServiceTest extends AbstractTest {

    @Resource
    private UserService userService;


    @Test
    void list() {
        userService
                .list()
                .toStream()
                .forEach(user -> System.out.println(user.toString()));
    }

    @Test
    void get() {
        userService.get(BigInteger.valueOf(1)).doOnSuccess(user -> System.out.println(user.toString())).subscribe();
    }

    @Test
    void add() {
        User user = new User( "李白", "123456", new Date(), new Profile("太白", 1));
        Mono<User> userMono = userService.add(user);

        StepVerifier.create(userMono)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }

    @Test
    void update() {
        User user = new User("李白", "12345611111", new Date(), new Profile("太白111111", 1));
        Mono<Boolean> booleanMono = userService.update(user);

        StepVerifier.create(booleanMono)
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void delete() {
        Mono<Boolean> booleanMono = userService.deleteByUsername("zhangsan000");

        StepVerifier.create(booleanMono)
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

    }
}