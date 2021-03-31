package org.example.entity;

import org.example.sequence.IncIdEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@Document
public class User extends IncIdEntity<BigInteger> {

    private String username;

    private String password;

    private Date createTime;

    private Profile profile;

    public User() {
    }

    public User(String username, String password, Date createTime, Profile profile) {

        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.profile = profile;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", profile=" + profile +
                '}';
    }
}
