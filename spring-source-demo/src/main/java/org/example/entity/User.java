package org.example.entity;

import lombok.Data;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-04-07
 */

@Data

public class User {
    private String name;
    private List<String> habits;

    public User(String name, List<String> habits) {
        this.name = name;
        this.habits = habits;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHabits() {
        return habits;
    }

    public void setHabits(List<String> habits) {
        this.habits = habits;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", habits=" + habits +
                '}';
    }
}
