package org.example.producer;

import org.example.constants.Constants;
import org.example.message.DemoMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

/**
 * @author dizhang
 * @date 2021-05-14
 */
@Component
public class K03Producer {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public ListenableFuture<SendResult<Object, Object>> asyncSend(Integer id) {
        // 创建 Demo02Message 消息
        DemoMessage message = new DemoMessage();
        message.setId(id);
        // 异步发送消息
        return kafkaTemplate.send(Constants.TOPIC, message);
    }
}
