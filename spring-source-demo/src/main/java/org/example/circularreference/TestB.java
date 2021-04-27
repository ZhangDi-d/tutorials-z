package org.example.circularreference;

/**
 * @author dizhang
 * @date 2021-04-23
 */
public class TestB {

    private TestC c;

    public TestB() {
    }

    public TestB(TestC c) {
        this.c = c;
    }

    public TestC getC() {
        return c;
    }

    public void setC(TestC c) {
        this.c = c;
    }
}
