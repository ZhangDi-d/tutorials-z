package org.example.controller;

import org.example.AbstractTest;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author dizhang
 * @date 2021-03-31
 */
class UserControllerTest extends AbstractTest {

    @Resource
    UserController userController;
    @Test
    void add() {
        Mono<Integer> test = userController.add(new User("test", "123456"));
        StepVerifier.create(test).expectNext(18).verifyComplete();
    }
}