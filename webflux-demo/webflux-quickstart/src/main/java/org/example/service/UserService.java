package org.example.service;

import org.example.model.User;
import org.springframework.stereotype.Service;

/**
 * @author dizhang
 * @date 2021-03-17
 */
@Service
public class UserService {

    public User get(Integer userId) {
        User user = new User(userId, "name" + userId, "password" + userId);

        return user;
    }

}
