package example.entity;

import java.util.List;

/**
 * @author dizhang
 * @date 2021-04-18
 */
public class User {
    private String name;
    private List<String> habits;

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
