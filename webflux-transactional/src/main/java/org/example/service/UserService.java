package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.function.Function;

/**
 * @author dizhang
 * @date 2021-03-26
 */
@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final static String EXCEPTION = "exception";

    @Resource
    R2dbcEntityTemplate r2dbcEntityTemplate;
    @Resource
    UserRepository userRepository;


    @Transactional(rollbackFor = Exception.class)
    public Mono<Integer> add1(User queryUser) {

        return this.r2dbcEntityTemplate.insert(User.class)
                .using(queryUser)
                .doOnSuccess(user -> {
                    if (!user.getUsername().contains(EXCEPTION)) {
                        LOGGER.info("=====================add normal=================");
                    } else {
                        LOGGER.error("=====================add exception=================");
                        throw new RuntimeException("add1 exception test............");
                    }

                })
                .map(User::getId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Integer> add2(User queryUser) {

        return userRepository.save(queryUser).flatMap((Function<User, Mono<Integer>>) user -> {
            if (user.getUsername().contains(EXCEPTION)) {
                LOGGER.error("=====================add2 exception=================");
                throw new RuntimeException("test exception...");
            }
            return Mono.just(user.getId());
        });
    }
}
