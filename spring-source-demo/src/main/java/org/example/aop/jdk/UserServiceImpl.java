package org.example.aop.jdk;

/**
 * @author dizhang
 * @date 2021-05-07
 */
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("========add=========");
    }
}
