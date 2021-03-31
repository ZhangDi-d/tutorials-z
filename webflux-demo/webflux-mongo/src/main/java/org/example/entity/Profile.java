package org.example.entity;

/**
 * @author dizhang
 * @date 2021-03-18
 */
public class Profile {
    private String nickname;
    private Integer gender;

    public Profile() {
    }

    public Profile(String nickname, Integer gender) {
        this.nickname = nickname;
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
