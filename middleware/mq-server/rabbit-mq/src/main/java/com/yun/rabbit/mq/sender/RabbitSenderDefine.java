package com.yun.rabbit.mq.sender;

import com.yun.rabbit.mq.bean.QueueConfig;
import com.yun.rabbit.mq.config.MessageConfiguration;
import com.yun.rabbit.mq.manage.RabbitQueueManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @program: rabbit-sync
 * @description:
 * @author: wxf
 * @create: 2020-03-18 17:01
 **/
@Component
@Slf4j
public class RabbitSenderDefine {
    @Autowired
    private MessageConfiguration configuration;
    @Autowired
    private RabbitQueueManage rabbitQueueManage;

    @PostConstruct
    public void init() {
        Map<String, QueueConfig> queueMap = configuration.getQueueMap();
        queueMap.values().forEach(queueConfig -> {
            rabbitQueueManage.defineQueue(queueConfig);
        });

    }
}
