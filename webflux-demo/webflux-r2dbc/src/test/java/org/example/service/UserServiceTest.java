package org.example.service;

import org.example.AbstractTest;
import org.example.controller.UserController;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

/**
 * @author dizhang
 * @date 2021-03-26
 */
class UserServiceTest extends AbstractTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserService userService;
    @Resource
    private UserController userController;


    @Test
    void list() {
        // 报错：Caused by: org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.time.LocalDateTime] to type [java.util.Date]
        //
        Flux<User> list = userService.list();
        list.toStream().forEach(user -> System.out.println(user.toString()));
    }

    @Test
    void get() {
        Mono<User> userMono = userService.get(11);
        StepVerifier.create(userMono).expectNextMatches(user -> user.getUsername().equals("zhaoliu")).verifyComplete();
    }

    @Test
    void add() {
//        Mono<Integer> zhangsan = userService.add(new User("zhangsan", "123456"));
//        Mono<Integer> lisi = userService.add(new User( "lisi", "rewq"));
//
//        Mono<Integer> wangwu = userService.add(new User("wangwu", "123456"));
//        StepVerifier.create(wangwu).expectNext(10).verifyComplete();
//        Mono<Integer> zhaoliu = userService.add(new User("zhaoliu", "123456"));
//        StepVerifier.create(zhaoliu).expectNext(11).verifyComplete();


    }


    @Test
    void update() {
        Mono<Boolean> zhangsan111 = userService.update(new User(9, "zhangsan111", "zhangsan111"));
        StepVerifier.create(zhangsan111).expectNext(true).verifyComplete();
    }

    @Test
    void update1() {
        Mono<Boolean> lisiqqq = userService.update1(new User(8, "lisiqqq", "lisiqqq"));
        StepVerifier.create(lisiqqq).expectNext(true).verifyComplete();
    }

    @Test
    void delete() {
        Mono<Boolean> booleanMono = userService.delete(7);
        StepVerifier.create(booleanMono).expectNext(true).verifyComplete();
    }

    @Test
    void test1() {
        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
                .filter(name -> name.length() == 4)
                .map(String::toUpperCase);

        StepVerifier
                .create(source)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();
    }
}