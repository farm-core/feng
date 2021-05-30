package com.yun.kafka.easy.producer;

import com.yun.kafka.easy.consts.KafkaConstants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @Description: //TODO
 * @Author: zhenxing
 * @Date: 2020/9/2 11:35
 * @Version: 1.0
 */
public class KafkaProducerDemo {

    public static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BROKER_LIST);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        //重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        //批次大小16k
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        //等待时间
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        //RecordAccumulator 缓冲区大小33554432(32M)
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        //自定义分区器
        //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.yelm.kafka.partitioner.MyPartitioner");

        //Interceptor 构建拦截链 【配合方法MainAPP的sendMessageContainInterceptor使用】 start
 /*       List<String> interceptors = new ArrayList<>();
        interceptors.add("com.yelm.kafka.interceptor.TimeInterceptor");
        interceptors.add("com.yelm.kafka.interceptor.CountInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);*/
        // 构建拦截链 【配合方法MainAPP的sendMessageContainInterceptor使用】 end

        return new KafkaProducer(props);
    }

}
