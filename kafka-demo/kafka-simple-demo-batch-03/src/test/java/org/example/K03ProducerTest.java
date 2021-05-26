package org.example;

import lombok.extern.log4j.Log4j2;
import org.example.producer.K03Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;

/**
 * @author dizhang
 * @date 2021-05-26
 */
@Log4j2
public class K03ProducerTest extends AbstractTest{

    @Autowired
    private K03Producer producer;

    @Test
    public void testASyncSend() throws InterruptedException {
        log.info("[testASyncSend][开始执行]");

        for (int i = 0; i < 3; i++) {
            int id = (int) (System.currentTimeMillis() / 1000);
            producer.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onFailure(Throwable e) {
                    log.info("[testASyncSend][发送编号：[{}] 发送异常]]", id, e);
                }

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("[testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", id, result);
                }

            });

            // 故意每条消息之间，隔离 10 秒
            Thread.sleep(10 * 1000L);
        }

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }
}
