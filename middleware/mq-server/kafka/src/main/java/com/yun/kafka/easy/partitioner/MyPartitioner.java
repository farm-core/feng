package com.yun.kafka.easy.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @Description: 自定义分区器
 * @Author: zhenxing
 * @Date: 2020/9/5 17:43
 * @Version: 1.0
 */
public class MyPartitioner implements Partitioner {
    //全部分配到分区1
    @Override
    public int partition(String topic, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
        /*** 分区逻辑 start****/
        //分区逻辑代码
        /*** 分区逻辑 end ****/
        return 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
