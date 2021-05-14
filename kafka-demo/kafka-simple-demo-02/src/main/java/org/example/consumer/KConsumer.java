package org.example.consumer;

import lombok.extern.log4j.Log4j2;
import org.example.constants.Constants;
import org.example.message.DemoMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author dizhang
 * @date 2021-05-13
 */
@Log4j2
@Component
public class KConsumer {

    @KafkaListener(topics = Constants.TOPIC, groupId = "k-consumer-group-002-" + Constants.TOPIC)
    public void onMessage(DemoMessage message) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
