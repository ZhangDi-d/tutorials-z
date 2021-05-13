package org.example.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author dizhang
 * @date 2021-05-13
 */
public class KConsumer {
    private static Consumer<String, String> createConsumer() {
        // 设置 Producer 的属性
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092"); // 设置 Broker 的地址
        properties.put("key.deserializer", StringDeserializer.class.getName()); // 消息的 key 的反序列化方式
        properties.put("value.deserializer", StringDeserializer.class.getName()); // 消息的 value 的反序列化方式

        properties.put("fetch.min.bytes", 1);
        properties.put("max.partition.fetch.bytes", 1048576);
        properties.put("group.id", "k-consumer-group"); // 消费者分组
        properties.put("connections.max.idle.ms", 540000);

        /*
          最早：将偏移量自动重置为最早的偏移量
          最新：自动将偏移量重置为最新偏移量
          none：如果未找到消费者组的先前偏移量，则向消费者抛出异常
          其他：向消费者抛出异常
          设置消费者分组最初的消费进度为 earliest 。可参考博客 https://blog.csdn.net/lishuangzhe7047/article/details/74530417 理解
         */
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", true); // 是否自动提交消费进度
        properties.put("auto.commit.interval.ms", "1000"); // 自动提交消费进度频率


        // 创建 KafkaProducer 对象
        return new KafkaConsumer<>(properties);
    }

    public static void main(String[] args) {
        // 创建 KafkaConsumer 对象
        Consumer<String, String> consumer = createConsumer();

        // 订阅消息
        consumer.subscribe(Collections.singleton("test"));

        // 拉取消息
        while (true) {
            try {
                // 拉取消息。如果拉取不到消息，阻塞等待最多 10 秒，或者等待拉取到消息。
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
                // 遍历处理消息
                records.forEach(record -> System.out.println(record.key() + "\t" + record.value()));
            } catch (Exception e) {
                throw new RuntimeException("read message error");
            }
        }
    }
}
