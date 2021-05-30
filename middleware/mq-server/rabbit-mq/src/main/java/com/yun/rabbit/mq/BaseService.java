package com.yun.rabbit.mq;


import com.yun.rabbit.mq.bean.MessageWrapper;

/**
 * 基础service
 *
 * @author wxf
 * @date 2020/3/9
 */
public interface BaseService {
    void insert(MessageWrapper wrapper);

    void update(MessageWrapper wrapper);

    void delete(MessageWrapper wrapper);
}

