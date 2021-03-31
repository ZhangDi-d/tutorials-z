package org.example.service;

import org.example.AbstractTest;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author dizhang
 * @date 2021-03-19
 */
class UserReactiveRedisServiceTest extends AbstractTest {

    @Resource
    private UserReactiveRedisService userReactiveRedisService;

    @Test
    void getReactive() {
        Mono<User> userMono = userReactiveRedisService.getReactive(1);
        System.out.println(Objects.requireNonNull(userMono.block()));
    }

    @Test
    void setReactive() {
        User user = new User(1, "reactiveUser", 1);
        Mono<Boolean> booleanMono = userReactiveRedisService.setReactive(user);  //user 需要实现 Serializable
        booleanMono.subscribe(System.out::println);
    }
}