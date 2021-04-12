package org.example.controller;


import org.example.entity.User;

/**
 * @author dizhang
 * @date 2021-04-08
 */
public interface TestInter {

    default void addUser(User user){
        user.getHabits().add("test11");
    }
}
