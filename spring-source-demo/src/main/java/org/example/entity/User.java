package org.example.entity;

import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
