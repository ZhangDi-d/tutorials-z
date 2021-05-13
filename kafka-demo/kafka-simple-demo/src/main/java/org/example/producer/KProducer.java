package org.example.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author dizhang
 * @date 2021-05-13
 */
public class KProducer {

    private static Producer<String, String> createProducer() {
        // 设置 KProducer 的属性  http://kafka.apachecn.org/documentation.html#producerconfigs
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092"); // 设置 Broker 的地址
        properties.put("key.serializer", StringSerializer.class.getName()); // 消息的 key 的序列化方式
        properties.put("value.serializer", StringSerializer.class.getName()); // 消息的 value 的序列化方式
        properties.put("acks", "1"); // 0-不应答;1-leader 应答;all-所有 leader 和 follower 应答;
        properties.put("retries", 3); // 发送失败时，重试发送的次数

        properties.put("buffer.memory", 1024);
        properties.put("compression.type", "lz4");
        properties.put("batch.size", 1024);  //Attempt to allocate 16384 bytes, but there is a hard limit of 1024 on memory allocations.
        properties.put("client.id", "KProducer-001");
        properties.put("connections.max.idle.ms", 540000);
        properties.put("linger.ms", 1);
        properties.put("max.block.ms", 60000);

        // 创建 KProducer 对象
        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建 KProducer 对象
        Producer<String, String> producer = createProducer();

        // 创建消息。传入的三个参数，分别是 Topic ，消息的 key ，消息的 message 。
        ProducerRecord<String, String> message = new ProducerRecord<>("test", "key", "法外狂徒-刘备");

        // 同步发送消息
        Future<RecordMetadata> sendResultFuture = producer.send(message);
        RecordMetadata result = sendResultFuture.get();
        System.out.println("message sent to " + result.topic() + ", partition " + result.partition() + ", offset " + result.offset());
    }
}
