package org.example.service;

import org.example.entity.User;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author dizhang
 * @date 2021-03-19
 */
@Service
public class UserReactiveRedisService {

    @Resource
    private ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    public Mono<User> getReactive(Integer id) {
        String key = genKeyReactive(id);
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> setReactive(User user) {
        String key = genKeyReactive(user.getId());
        return reactiveRedisTemplate.opsForValue().set(key, user);
    }

    private static String genKeyReactive(Integer id) {
        return "user:reactive:" + id;
    }

}
