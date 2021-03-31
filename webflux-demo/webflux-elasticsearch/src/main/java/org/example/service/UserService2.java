package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author dizhang
 * @date 2021-03-26
 */
@Service
public class UserService2 {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();

    }
}
