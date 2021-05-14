package org.example.producer;

import org.example.constants.Constants;
import org.example.message.DemoMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author dizhang
 * @date 2021-05-13
 */
@Component
public class KProducer {
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult<Object, Object> syncSend(Integer id) throws ExecutionException, InterruptedException {
        DemoMessage message = new DemoMessage();
        message.setId(id);
        return kafkaTemplate.send(Constants.TOPIC, message).get();
    }

    public ListenableFuture<SendResult<Object, Object>> asyncSend(Integer id) {
        DemoMessage message = new DemoMessage();
        message.setId(id);
        return kafkaTemplate.send(Constants.TOPIC, message);
    }
}


