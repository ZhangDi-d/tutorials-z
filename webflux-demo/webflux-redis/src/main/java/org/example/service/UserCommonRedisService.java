package org.example.service;

import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author dizhang
 * @date 2021-03-19
 */
@Service
public class UserCommonRedisService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserCommonRedisService.class);

    @Resource
    private ReactiveRedisTemplate<String, Object> commonRedisTemplate;

    public Mono<User> getCommon(Integer id) {
        String key = genKeyCommon(id);
        return commonRedisTemplate.opsForValue().get(key).map(o->(User)o);
    }

    public Mono<Boolean> setCommon(User user) {
        String key = genKeyCommon(user.getId());
        try {
            return commonRedisTemplate.opsForValue().set(key, user);
        } catch (Exception e) {
            LOGGER.error("set common error", e);
            return Mono.just(false);
        }


    }

    private static String genKeyCommon(Integer id) {
        return "user:common:" + id;
    }
}
