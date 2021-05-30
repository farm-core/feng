package com.yun.kafka.easy;

import com.yun.kafka.easy.consumer.KafkaConsumerDemo;
import com.yun.kafka.easy.producer.KafkaProducerDemo;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Arrays;

/**
 * @Description: //TODO
 * @Author: zhenxing
 * @Date: 2020/9/3 9:54
 * @Version: 1.0
 */
public class MainApp {
    private static final String TOPIC = "test-topic";
    // 新建一个新的topic用于回调查看消费情况
    private static final String TOPIC1 = "test-topic-1";

    public static void main(String[] args) {
        // -----------生产者示例 start------------------
        sendMessage();

        //带回调函数的生产者
        //sendMessageByCallBack();
        //指定分区发送消息
        //sendMessageAssignPartitions();

        //自定义分区器发送消息
        //要想看到效果打开com/yelm/kafka/producer/KafkaProducerDemo.java:38的注释
        //sendMessageThoughMyPartitioner();

        //带拦截器的生产者
        //要想看到效果打开com/yelm/kafka/producer/KafkaProducerDemo.java:42的注释
        //sendMessageContainInterceptor();
        // -----------生产者示例 end------------------

        System.out.println("===============生产者结束=======================");

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ---- 消费者示例 -----
        consumeMessage();
        //syncConsumeMessage();
    }

    /**
     * @Description: 不使用回调
     * @Author: zhenxing
     * @Date: 2020/9/5 17:21
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void sendMessage() {
        Producer<String, String> producer = KafkaProducerDemo.createProducer();

        try {
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(TOPIC, "hello kafka ## " + i));
            }
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        //close connect
        producer.close();
    }

    /**
     * @Description: 使用回调发送消息
     * @Author: zhenxing
     * @Date: 2020/9/5 17:26
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void sendMessageByCallBack() {
        Producer<String, String> producer = KafkaProducerDemo.createProducer();
        try {
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(TOPIC1, "szx", "hello kafka ## " + i), (recordMetadata, e) -> {
                    if (e == null) {
                        System.out.println(recordMetadata.partition() + "--" + recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        //close connect
        producer.close();
    }

    /**
     * @Description: 指定分区发送消息
     * @Author: zhenxing
     * @Date: 2020/9/5 17:37
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void sendMessageAssignPartitions() {
        Producer<String, String> producer = KafkaProducerDemo.createProducer();

        try {
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(TOPIC1, 1, "szx", "hello kafka ## " + i), (recordMetadata, e) -> {
                    if (e == null) {
                        System.out.println(recordMetadata.partition() + "--" + recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        //close connect
        producer.close();
    }

    /**
     * @Description: 自定义分区器发送消息
     * @Author: zhenxing
     * @Date: 2020/9/5 17:55
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void sendMessageThoughMyPartitioner() {
        Producer<String, String> producer = KafkaProducerDemo.createProducer();

        try {
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(TOPIC1, "hello kafka ## " + i), (recordMetadata, e) -> {
                    if (e == null) {
                        System.out.println(recordMetadata.partition() + "--" + recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        //close connect
        producer.close();
    }

    /**
     * @Description: 发送消息时，使用拦截器拦截消息
     * @Author: zhenxing
     * @Date: 2020/9/8 17:51
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void sendMessageContainInterceptor() {
        Producer<String, String> producer = KafkaProducerDemo.createProducer();
        for (int i = 0; i < 10; i++) {
            ProducerRecord producerRecord = new ProducerRecord<>(TOPIC, "message" + i);
            producer.send(producerRecord);
        }
        // 关闭 connect
        producer.close();
    }


    /**
     * @Description: 消费者消费数据
     * @Author: zhenxing
     * @Date: 2020/9/5 20:33
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void consumeMessage() {
        Consumer<String, String> consumer = KafkaConsumerDemo.createConsumer();
        //消费消息
        while (true) {
            // subscribe topic and consume message
            consumer.subscribe(Arrays.asList(TOPIC, TOPIC1));
            // retrieve data
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            // parse and print consume data
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("Consumer consume message: " + consumerRecord.key() + "----" + consumerRecord.value());
            }
        }
    }

    /**
     * @Description: 手动提交offset
     * @Author: zhenxing
     * @Date: 2020/9/8 16:38
     * @param: * @param
     * @Version: 1.0
     * @return: void
     */
    static void syncConsumeMessage() {
        Consumer<String, String> consumer = KafkaConsumerDemo.createConsumer();
        //消费消息
        while (true) {
            // subscribe topic and consume message
            consumer.subscribe(Arrays.asList(TOPIC, TOPIC1));
            // retrieve data
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            // parse and print consume data
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("Consumer consume message: " + consumerRecord.key() + "----" + consumerRecord.value());
            }

            //异步提交
            consumer.commitAsync((offsets, exception) -> {
                if (exception != null) {
                    System.out.println("Commit failed for: " + offsets);
                }
            });
        }
    }


}
