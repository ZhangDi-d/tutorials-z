package org.example.controller;

import org.example.DemoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Resource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author dizhang
 * @date 2021-03-17
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureWebTestClient

//@RunWith(SpringRunner.class)  两种都可以
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRouteTest {

    @Resource
    private WebTestClient webTestClient;

    @Test
    void getRoute() {

        webTestClient.get().uri("/user2/find?user_id=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(e -> System.out.println(new String(Objects.requireNonNull(e.getResponseBody()))));
    }
}