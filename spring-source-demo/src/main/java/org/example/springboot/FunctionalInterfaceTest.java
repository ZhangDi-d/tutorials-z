package org.example.springboot;

/**
 * @author dizhang
 * @date 2021-05-12
 */
public class FunctionalInterfaceTest {

    static HelloInterface aDefault = HelloInterface.DEFAULT;

    public static void main(String[] args) {
        String aDefaultMessage = aDefault.msg("hello");
        System.out.println(aDefaultMessage);
    }
}
