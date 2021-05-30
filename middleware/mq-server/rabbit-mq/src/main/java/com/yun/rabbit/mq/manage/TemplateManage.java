package com.yun.rabbit.mq.manage;

import com.yun.rabbit.mq.bean.MessageTemplate;
import com.yun.rabbit.mq.bean.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: springboot-rabbit
 * @description: rabbitmq客户端容器
 * @author: wxf
 * @date: 2020-03-05 15:08
 **/
@Slf4j
@Component
public class TemplateManage {
    @Autowired
    private ConnectionManage connectionManage;

    private ConcurrentHashMap<String, MessageTemplate> templateMap = new ConcurrentHashMap<>();

    public boolean add(String queue, MessageTemplate template) {
        if (templateMap.containsKey(queue)) {
            return true;
        }
        templateMap.put(queue, template);
        return true;
    }

    public boolean remove(String queue) {
        templateMap.remove(queue);
        return true;
    }

    public MessageTemplate get(QueueConfig config) {
        MessageTemplate template = templateMap.get(config.getName());
        if (Objects.isNull(template)) {
            template = defaultTemplate(config);
            add(config.getName(), template);
        }
        return template;
    }

    public MessageTemplate get(String queue) {
        return templateMap.get(queue);
    }


    private MessageTemplate defaultTemplate(QueueConfig config) {
        CachingConnectionFactory connectionFactory = connectionManage.getConnection(config.getExchange());
        MessageTemplate template = new MessageTemplate(connectionFactory, config);
        log.debug("创建新的默认template {}", template);
        return template;
    }

//    @Lookup
//    protected abstract DefaultCallBack defaultCallBack();


}
