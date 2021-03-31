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
class UserCommonRedisServiceTest extends AbstractTest {

    @Resource
    private UserCommonRedisService userCommonRedisService;

    @Test
    void getCommon() {
        Mono<User> userMono = userCommonRedisService.getCommon(2);
        System.out.println(Objects.requireNonNull(userMono.block()));
    }

    @Test
    void setCommon() {
        Mono<Boolean> commonUser = userCommonRedisService.setCommon(new User(2, "commonUser", 2));
        commonUser.subscribe(System.out::println);
    }
}