package com.yun.rabbit.mq.aop;

import com.yun.rabbit.mq.annotation.MQClient;
import com.yun.rabbit.mq.bean.MessageTemplate;
import com.yun.rabbit.mq.bean.MessageWrapper;
import com.yun.rabbit.mq.bean.QueueConfig;
import com.yun.rabbit.mq.config.MessageConfiguration;
import com.yun.rabbit.mq.manage.RabbitQueueManage;
import com.yun.rabbit.mq.manage.TemplateManage;
import com.yun.rabbit.mq.sender.AbstractSenderService;
import com.yun.rabbit.mq.util.CorrelationUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.*;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @program: springboot-rabbit
 * @description:
 * @author: wxf
 * @date: 2020-03-02 15:11
 **/
@Slf4j
@Aspect
@Order(-1)
@Component("mqAspect")
public class MQAop implements BeanPostProcessor, BeanFactoryAware, BeanClassLoaderAware {
    @Autowired
    private TemplateManage templateManage;
    @Autowired
    private RabbitQueueManage rabbitQueueManage;
    @Autowired
    private MessageConfiguration configuration;

    private BeanFactory beanFactory;
    private BeanExpressionResolver resolver;
    private BeanExpressionContext expressionContext;
    private HashMap<String, Boolean> bindingMap = new HashMap<>();

    @Pointcut("@within(com.yun.rabbit.mq.annotation.MQClient) || @annotation(com.yun.rabbit.mq.annotation.MQClient)")
    public void pointCut() {

    }

    @After("pointCut() && @annotation(client)")
    public void doAfter(JoinPoint point, MQClient client) {
        QueueConfig queueConfig;
        String config = client.config();
        if (StringUtils.isEmpty(config)) {
            queueConfig = new QueueConfig(client.queue(), client.exchange(), client.routingKey());
        } else {
            queueConfig = configuration.getQueue(config);
        }

        Object aThis = point.getThis();
        AbstractSenderService service = aThis instanceof AbstractSenderService ? ((AbstractSenderService) aThis) : null;

        Object[] args = point.getArgs();
        MessageWrapper wrapper = ((MessageWrapper) args[0]);

        if (bindingMap.get(queueConfig.getName()) == null || !bindingMap.get(queueConfig.getName())) {
            rabbitQueueManage.defineQueue(queueConfig);
            bindingMap.put(queueConfig.getName(), true);
        }

        if (service == null || !service.templateExist()) {
            // TODO: 2020/3/2  跟据queue名称获取对应的messageTemplate
            MessageTemplate template = templateManage.get(queueConfig);
            template.sendMessage(wrapper);
        } else {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(CorrelationUtil.encode(wrapper, queueConfig.getName()));
            RabbitTemplate template = service.getRabbitTemplate();
            template.convertAndSend(queueConfig.getExchange(), queueConfig.getRoutingKey(), wrapper, correlationData);
        }

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.resolver = new StandardBeanExpressionResolver(classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory, null);
        }
    }

    /**
     * 解析 SPEL
     *
     * @param value spel表达式
     * @return obj
     */
    private Object resolveExpression(String value) {
        String resolvedValue = resolve(value);
        if (!(resolvedValue.startsWith("#{") && value.endsWith("}"))) {
            return resolvedValue;
        }
        return this.resolver.evaluate(resolvedValue, this.expressionContext);
    }

    /**
     * 解析 ${}
     *
     * @param value
     * @return
     */
    private String resolve(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }
}
