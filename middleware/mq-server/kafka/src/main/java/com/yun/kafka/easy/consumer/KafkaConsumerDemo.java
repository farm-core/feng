package com.yun.kafka.easy.consumer;

import com.yun.kafka.easy.consts.KafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

/**
 * @Description: //TODO
 * @Author: zhenxing
 * @Date: 2020/9/2 11:35
 * @Version: 1.0
 */
public class KafkaConsumerDemo {
    public static Consumer<String, String> createConsumer() {
        //创建消费者配置信息
        Properties properties = new Properties();

        // 给配置信息选择消费节点
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER_LIST);

        // ------ 自动提交offset start ---
        // 开启自动提交
        //properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        // 自动提交的延时
        //properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // ------ 自动提交offset end ---

        // -------手动提交offset start-------------
        //关闭自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        // -------手动提交offset end---------------



        // key value 反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConstants.GROUP_ID_CONFIG);

        return new KafkaConsumer(properties);
    }
}
