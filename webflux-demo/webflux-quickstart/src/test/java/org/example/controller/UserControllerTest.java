package org.example.controller;

import org.example.DemoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author dizhang
 * @date 2021-03-17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureWebTestClient
class UserControllerTest {


    @Resource
    private WebTestClient webTestClient;

    @Test
    void get() {
        webTestClient.get().uri("/user/find?user_id=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(e -> System.out.println(new String(Objects.requireNonNull(e.getResponseBody()))));
    }
}