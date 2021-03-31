package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author dizhang
 * @date 2021-03-31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 测试 事务  注释 DatabaseConfiguration  事务生效了
     * @param user
     * @return
     */

    @PostMapping("/add1")
    public Mono<Integer> add1(@RequestBody User user){
        return userService.add1(user);
    }
}
