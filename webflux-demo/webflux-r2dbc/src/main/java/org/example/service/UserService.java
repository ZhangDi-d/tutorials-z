package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author dizhang
 * @date 2021-03-26
 */
@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final User USER_NULL = new User();

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    public Flux<User> list() {
        // 返回列表
        return userRepository
                .findAll()
                .map(user -> new User(user.getId(), user.getUsername(), user.getPassword()));
    }

    /**
     * 获得指定用户编号的用户
     *
     * @param id 用户编号
     * @return 用户
     */
    public Mono<User> get(Integer id) {
        // 返回
        return userRepository
                .findById(id)
                .map(user -> new User(user.getId(), user.getUsername(), user.getPassword()));
    }

    /**
     * 添加用户
     *
     * @param queryUser 添加用户信息 DTO
     * @return 添加成功的用户编号
     */
    @Transactional
    public Mono<Integer> add(User queryUser) {
        Mono<User> user = userRepository.findByUsername(queryUser.getUsername());

        // 执行插入
        return user.defaultIfEmpty(USER_NULL)
                .flatMap(new Function<User, Mono<Integer>>() {

                    @Override
                    public Mono<Integer> apply(User user) {
                        if (user != USER_NULL) {
                            // 实际上，一般是抛出 ServiceException 异常。因为这个示例项目里暂时没做全局异常的定义，所以暂时返回 -1 啦
                            return Mono.just(-1);
                        }

                        user = new User(null, queryUser.getUsername(), queryUser.getPassword());
                        // 插入数据库
                        return userRepository.save(user).flatMap(new Function<User, Mono<Integer>>() {
                            @Override
                            public Mono<Integer> apply(User user) {
                                // 如果编号为偶数，抛出异常。
                                if (user.getId() % 2 == 0) {
                                    return Mono.error(new RuntimeException("test exception..."));
                                }

                                // 返回编号
                                return Mono.just(user.getId());
                            }
                        });
                    }

                });
    }

    /**
     * 更新指定用户编号的用户
     *
     * @param updateUser 更新用户信息 DTO
     * @return 是否修改成功
     */
    public Mono<Boolean> update(User updateUser) {
        // 查询用户
        Mono<User> user = userRepository.findById(updateUser.getId());

        // 执行更新
        return user.defaultIfEmpty(USER_NULL)
                .flatMap(new Function<User, Mono<Boolean>>() {

                    @Override
                    public Mono<Boolean> apply(User userDO) {
                        if (userDO == USER_NULL) {
                            return Mono.just(false);
                        }
                        return userRepository.findByUsername(updateUser.getUsername())
                                .defaultIfEmpty(USER_NULL)
                                .flatMap(new Function<User, Mono<? extends Boolean>>() {

                                    @Override
                                    public Mono<? extends Boolean> apply(User usernameUserDO) {
                                        if (usernameUserDO != USER_NULL && !Objects.equals(updateUser.getId(), usernameUserDO.getId())) {
                                            return Mono.just(false);
                                        }
                                        // 执行更新
                                        userDO.setUsername(updateUser.getUsername());
                                        userDO.setPassword(updateUser.getPassword());
                                        return userRepository.save(userDO).map(userDO -> true);
                                    }

                                });
                    }

                });
    }


    public Mono<Boolean> update1(User updateUser) {
        LOGGER.info(updateUser.toString());
        return userRepository.updateById(updateUser.getUsername(), updateUser.getPassword(), updateUser.getId());


    }

    /**
     * 删除指定用户编号的用户
     *
     * @param id 用户编号
     * @return 是否删除成功
     */
    public Mono<Boolean> delete(Integer id) {
        // 查询用户
        Mono<User> user = userRepository.findById(id);

        return user.defaultIfEmpty(USER_NULL)
                .flatMap((Function<User, Mono<Boolean>>) user1 -> {
                    if (user1 == USER_NULL) {
                        LOGGER.info("user not exist...");
                        return Mono.just(false);
                    }
                    return userRepository.deleteById(id).map(aVoid -> true);
                });
    }
}
