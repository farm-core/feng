package com.yun.rabbit.mq.receiver;

import com.yun.rabbit.mq.BaseService;
import com.yun.rabbit.mq.bean.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: springboot-rabbit - 副本
 * @description:
 * @author: wxf
 * @date: 2020-03-13 17:49
 **/
@Service
@Slf4j
public class DefaultDataServiceImpl implements BaseService {

    @Override
    public void insert(MessageWrapper wrapper) {
        System.out.println("insert");
        System.out.println(wrapper);
    }

    @Override
    public void update(MessageWrapper wrapper) {
        System.out.println("update");
        System.out.println(wrapper);
    }

    @Override
    public void delete(MessageWrapper wrapper) {
        System.out.println("delete");
        System.out.println(wrapper);
    }
}
