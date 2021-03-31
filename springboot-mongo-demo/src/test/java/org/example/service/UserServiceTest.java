package org.example.service;

import org.example.AbstractTest;
import org.example.entity.Profile;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author dizhang
 * @date 2021-03-18
 */
class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService userService;

    @Test
    void insert() {
//        User user = new User(1, "zhangsan", "123456", new Date(), new Profile("san", 1));
//        userService.insert(user);
//
//        User user1 = new User(2, "lisi", "123456", new Date(), new Profile("sisi", 1));
//        userService.insert(user1);
//
//        User user2 = new User(3, "王五", "123456", new Date(), new Profile("五五", 1));
//        userService.insert(user2);
//
//        User user3 = new User(4, "赵六", "123456", new Date(), new Profile("六六", 1));
//        userService.insert(user3);

        User user4 = new User(7, "zhangsan000", "123456", new Date(), new Profile("san000", 1));
        userService.insert(user4);
    }

    @Test
    void updateById() {
        User user1 = new User(2, "lisi111", "1234561", new Date(), new Profile("sisi1", 1));
        userService.updateById(user1);

    }

    @Test
    void deleteById() {
        userService.deleteById(2);
    }

    @Test
    void findById() {
        User user = userService.findById(1);
        System.out.println(user.toString());
    }

    @Test
    void findByUsername() {
        User zhangsan = userService.findByUsername("zhangsan");
        System.out.println(zhangsan.toString());
    }

    @Test
    void findAllById() {
        List<User> allById = userService.findAllById(new ArrayList<>(Collections.singletonList(1)));
        for (User user : allById) {
            System.out.println(user.toString());
        }
    }

    @Test
    void findByUsernameLike() {
        Page<User> page = userService.findByUsernameLike("zhangsan", PageRequest.of(1, 1));
        for (User user : page) {
            System.out.println(user.toString() );
        }
    }

    @Test
    void findByUsername01(){
        User zhangsan = userService.findByUsername01("zhangsan");
        System.out.println(zhangsan.toString());
    }

    @Test
    void findByUsernameLike01(){
        List<User> users = userService.findByUsernameLike01("zhangsan");
        for (User user : users) {
            System.out.println(user.toString() );
        }
    }
}