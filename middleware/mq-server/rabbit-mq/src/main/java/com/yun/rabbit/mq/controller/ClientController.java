package com.yun.rabbit.mq.controller;

import com.yun.rabbit.mq.bean.MessageWrapper;
import com.yun.rabbit.mq.service.impl.DemoSenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: springboot-rabbit
 * @description:
 * @author: wxf
 * @date: 2020-03-05 10:38
 **/
@RestController
@Slf4j
public class ClientController {
    @Autowired
    DemoSenderImpl sender;
    @Autowired
    private ThreadPoolTaskExecutor brianThreadPool;

    @RequestMapping("/sender")
    public void sender() {
        MessageWrapper<Object> wrapper = new MessageWrapper<>();
        wrapper.setAction(0);
        wrapper.setTag("jkdjskljf");
        sender.insert(wrapper);
    }

    @RequestMapping("/sender2")
    public void sender2() {
        long l = System.currentTimeMillis() + 30 * 1000;

        while (System.currentTimeMillis() < l) {
            MessageWrapper<Object> wrapper = new MessageWrapper<>();
            wrapper.setAction(1);
            wrapper.setTag(System.currentTimeMillis() + "");

            brianThreadPool.submit(() -> {
                sender.update(wrapper);
                log.debug(Thread.currentThread().getName());
//                return Thread.currentThread().getName();

            });

            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

    }

    @RequestMapping("/sender3")
    public void sender3() {
        MessageWrapper<Object> wrapper = new MessageWrapper<>();
        wrapper.setAction(2);
        wrapper.setTag("jkdjskljf");
        sender.delete(wrapper);
    }

    @RequestMapping("/sender4")
    public void sender4() {
        MessageWrapper<Object> wrapper = new MessageWrapper<>();
        wrapper.setAction(2);
        wrapper.setTag("tag" + System.currentTimeMillis());
        sender.some(wrapper);
    }


    @RequestMapping("/sender5")
    public void sender5() {
        AtomicInteger num = new AtomicInteger(0);
        long l = System.currentTimeMillis() + 5 * 60 * 1000;
        while (System.currentTimeMillis() < l) {
            MessageWrapper<Object> wrapper = new MessageWrapper<>();
            wrapper.setAction(1);
            wrapper.setTag(System.currentTimeMillis() + "");
            wrapper.setData(num.addAndGet(1));
            brianThreadPool.submit(() -> {
                sender.some(wrapper);
                log.debug(Thread.currentThread().getName());

            });
        }
    }
}
