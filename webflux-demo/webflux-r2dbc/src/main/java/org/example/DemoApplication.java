package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @author dizhang
 * @date 2021-03-26
 */

@SpringBootApplication
@EnableR2dbcRepositories //作用是什么? No qualifying bean of type 'org.example.repository.UserRepository' available
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
