package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Service
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final User USER_NULL = new User();

    @Resource
    private UserRepository userRepository;

    public Flux<User> list() {
        return userRepository.findAll();
    }

    public Mono<User> get(BigInteger id) {
        return userRepository.findById(id);
    }


    public Mono<User> add(User user) {
        return userRepository.findByUsername(user.getUsername())  //如果已经存在不会添加，会返回已经存在的User
                .switchIfEmpty(Mono.just(user))
                .flatMap(new Function<User, Mono<? extends User>>() {
                    @Override
                    public Mono<? extends User> apply(User user) {
                        LOGGER.info("add user..." + user.toString());
                        return userRepository.save(user);
                    }
                });
    }

    public Mono<Boolean> update(User user) {
        return Mono.defer(new Supplier<Mono<User>>() {
            @Override
            public Mono<User> get() {
                return userRepository.findByUsername(user.getUsername());
            }
        }).switchIfEmpty(Mono.error(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                LOGGER.info("待修改的用户不存在...");
                return null;
            }
        }))
                .flatMap((Function<User, Mono<Boolean>>) user1 -> {
                    LOGGER.info("exist user1 " + user1.toString());
                    LOGGER.info("update user " + user.toString());
                    Disposable subscribe = userRepository.deleteByUsername(user1.getUsername()).subscribe();  //注意此处 https://stackoverflow.com/questions/60783389/save-in-reactivecrudrepository-not-inserting-or-updating-records
                    return userRepository.save(user).map(a -> true);
                })
                ;
    }


    public Mono<Boolean> deleteByUsername(String userName) {
        return Mono.defer(new Supplier<Mono<User>>() {
            @Override
            public Mono<User> get() {
                return userRepository.findByUsername(userName);
            }
        }).switchIfEmpty(Mono.just(USER_NULL))  //https://stackoverflow.com/questions/54373920/mono-switchifempty-is-always-called
                .log("UserService update user userName: " + userName)
                .flatMap(new Function<User, Mono<Boolean>>() {
                    @Override
                    public Mono<Boolean> apply(User user) {
                        if (StringUtils.isEmpty(user.getUsername())) {
                            LOGGER.info("user is not exist ");
                            return Mono.just(false);
                        }
                        return userRepository.deleteByUsername(userName).map(a -> true);
                    }
                });
    }
}

