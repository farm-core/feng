package com.yun.rabbit.mq.aop;

import com.yun.rabbit.mq.annotation.MsgRetry;
import com.yun.rabbit.mq.bean.MessageTemplate;
import com.yun.rabbit.mq.bean.MessageWrapper;
import com.yun.rabbit.mq.manage.TemplateManage;
import com.yun.rabbit.mq.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program: rabbit-sync-package
 * @description: 消息失败处理切面
 * @author: wxf
 **/
@Slf4j
@Aspect
@Order(-1)
@Component("retryAspect")
public class RetryAop {
    @Autowired
    TemplateManage templateManage;

    @Pointcut("@within(com.yun.rabbit.mq.annotation.MsgRetry) || @annotation(com.yun.rabbit.mq.annotation.MsgRetry)")
    public void pointCut() {

    }

    @AfterThrowing("pointCut() && @annotation(retry)")
    public void doAfter(JoinPoint point, MsgRetry retry) {
        Object[] args = point.getArgs();
        Message message = ((Message) args[0]);
        MessageWrapper wrapper = MessageUtil.getWrapper(message);
        MessageTemplate template = templateManage.get(wrapper.getQueue());
        template.sendFailMessage(message);
    }


}
