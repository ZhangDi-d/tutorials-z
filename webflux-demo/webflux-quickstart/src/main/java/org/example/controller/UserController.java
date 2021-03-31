package org.example.controller;

import org.example.constants.ServiceExceptionEnum;
import org.example.core.exception.ServiceException;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author dizhang
 * @date 2021-03-17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/find")
    public Mono<User> get(@RequestParam("user_id") Integer userId) {
        User user = userService.get(userId);
        return Mono.just(user);
    }

    @GetMapping("/exception-01")
    public User exception01() {
        throw new NullPointerException("nooooo");
    }

    @GetMapping("/exception-02")
    public User exception02() {
        throw new ServiceException(ServiceExceptionEnum.USER_NOT_FOUND);
    }
}
