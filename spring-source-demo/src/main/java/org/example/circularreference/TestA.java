package org.example.circularreference;

/**
 * @author dizhang
 * @date 2021-04-23
 */
public class TestA {

    private TestB b;

    public TestA(TestB b) {
        this.b = b;
    }

    public TestB getB() {
        return b;
    }

    public void setB(TestB b) {
        this.b = b;
    }
}
