package org.example.circularreference;

/**
 * @author dizhang
 * @date 2021-04-23
 */
public class TestC {

    private TestA a;

    public TestC(TestA a) {
        this.a = a;
    }

    public TestA getA() {
        return a;
    }

    public void setA(TestA a) {
        this.a = a;
    }
}
