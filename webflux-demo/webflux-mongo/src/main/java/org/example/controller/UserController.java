package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @author dizhang
 * @date 2021-03-19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/list")
    public Flux<User> list() {
        return userService.list();
    }

    @GetMapping("/get")
    public Mono<User> get(@RequestParam("id") BigInteger id) {
        return userService.get(id);
    }


    @PostMapping("/add")
    public Mono<User> add(User user) {
        return userService.add(user);
    }


    @PostMapping("/update")
    public Mono<Boolean> update(User user) {
        return userService.update(user);
    }


    @PostMapping("/delete")
    public Mono<Boolean> delete(@RequestParam("username") String username) {
        return userService.deleteByUsername(username);
    }

}
