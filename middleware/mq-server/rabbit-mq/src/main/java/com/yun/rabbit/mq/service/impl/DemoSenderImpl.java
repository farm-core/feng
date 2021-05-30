package com.yun.rabbit.mq.service.impl;

import com.yun.rabbit.mq.BaseService;
import com.yun.rabbit.mq.annotation.MQClient;
import com.yun.rabbit.mq.bean.MessageWrapper;
import org.springframework.stereotype.Service;

/**
 * @program: springboot-rabbit
 * @description:
 * @author: wxf
 * @date: 2020-03-03 14:26
 **/
@Service
public class DemoSenderImpl implements BaseService {

    @Override
    @MQClient(exchange = "exchange.admin.direct", routingKey = "test.com.clear.sync.mq", queue = "sender.queue.table")
    public void insert(MessageWrapper wrapper) {
    }

    @Override
    @MQClient(exchange = "exchange.admin.direct", routingKey = "test.mq2", queue = "sender.queue.table")
    public void update(MessageWrapper wrapper) {
    }

    @Override
    @MQClient(exchange = "exchange.admin.direct3", routingKey = "test.mq3", queue = "sender.queue.table", config = "#{'messageclient.queues.queue1'}")
    public void delete(MessageWrapper wrapper) {
    }

    @MQClient(config = "sender.queue.table11")
    public void some(MessageWrapper wrapper) {

    }
}
