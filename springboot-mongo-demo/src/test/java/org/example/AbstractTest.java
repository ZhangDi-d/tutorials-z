package org.example;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author dizhang
 * @date 2021-03-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = DemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class AbstractTest {
}
