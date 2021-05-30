package com.yun.rabbit.mq.receiver;

import com.yun.rabbit.mq.BaseService;
import com.yun.rabbit.mq.bean.MessageTemplate;
import com.yun.rabbit.mq.bean.QueueConfig;
import com.yun.rabbit.mq.config.MessageConfiguration;
import com.yun.rabbit.mq.manage.ConnectionManage;
import com.yun.rabbit.mq.manage.TemplateManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: springboot-rabbit - 副本
 * @description: 队列监听器注册
 * @author: wxf
 * @date: 2020-03-13 16:30
 **/
@Slf4j
@Component
public class RabbitListenerRegister implements ApplicationContextAware, Ordered {
    private ApplicationContext applicationContext;

//    CachingConnectionFactory connectionFactory;
    @Autowired
    private ConnectionManage connectionManage;
    @Autowired
    private MessageConfiguration configuration;
    @Autowired
    private TemplateManage templateManage;
    @Autowired
    private RabbitProperties rabbitProperties;

    private DirectMessageListenerContainer createMessageContainer(AbstractMessageListener messageListener, CachingConnectionFactory connectionFactory) throws AmqpException {
        DirectMessageListenerContainer container = new DirectMessageListenerContainer(connectionFactory);
        container.setQueueNames(messageListener.getQueueConfig().getName());
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(messageListener);
        return container;
    }

    @PostConstruct
    public void postProcessBeanFactory() throws BeansException {
        if (!configuration.getModel().equals("client")) {
            log.info("sync客户端模式为{}", configuration.getModel());
            return;
        }
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        ConfigurableListableBeanFactory beanFactory = configurableApplicationContext.getBeanFactory();
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        configuration.getExchanges().forEach(exchangeConfig -> exchangeConfig.getQueues().forEach(config -> {
                    CachingConnectionFactory connectionFactory = connectionManage.getConnection(config.getExchange());
                    // 注册监听器实例
//                    for (int i = 0; i < exchangeConfig.getListener(); i++) {
                    // 注册数据传输队列监听器
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DirectMessageListenerContainer.class, () -> {
                        DirectMessageListenerContainer container = null;
                        Class<?> aClass;
                        try {
                            aClass = Class.forName(config.getClazz());
                            BaseService service = (BaseService) this.applicationContext.getBean(aClass);
                            MessageTemplate template = templateManage.get(config);
                            if (template == null) {

                                template = new MessageTemplate(connectionFactory, config);
                                templateManage.add(config.getName(), template);
                            }
                            SimpleMessageListener listener = new SimpleMessageListener(config, service, template);
                            container = createMessageContainer(listener, connectionFactory);
                            Integer prefetch = rabbitProperties.getListener().getSimple().getPrefetch();
                            if (prefetch == null || prefetch < 0) {
                                prefetch = 1;
                            }
                            container.setPrefetchCount(prefetch);
                            container.setConsumersPerQueue(exchangeConfig.getListener());
                            container.setTaskExecutor(createThreadPool(exchangeConfig.getListener()));
                        } catch (AmqpException e) {
                            log.error("初始化队列监听失败 {}", config.getName());
                        } catch (ClassNotFoundException e) {
                            log.error("未找到对应bean {}", config.getClazz());
                        }
                        return container;
                    }).getBeanDefinition();
                    listableBeanFactory.registerBeanDefinition(config.getName(), beanDefinition);
                    log.info("注册队列监听器 {}", config.getName());
//                    }

                    // 注册死信消息队列监听器
                    QueueConfig deadQueue = config.toDeadQueue();
                    AbstractBeanDefinition deadBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DirectMessageListenerContainer.class, () -> {
                        DirectMessageListenerContainer container = null;
                        try {
                            DeadMessageListener listener = new DeadMessageListener(deadQueue);
                            container = createMessageContainer(listener, connectionFactory);
                        } catch (AmqpException e) {
                            log.error("初始化队列监听失败 {}", deadQueue.getName());
                        }
                        return container;
                    }).getBeanDefinition();

                    listableBeanFactory.registerBeanDefinition(deadQueue.getName(), deadBeanDefinition);
                }
        ));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    private ThreadPoolTaskExecutor createThreadPool(Integer size) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(size);
        //最大线程数
        executor.setMaxPoolSize(size + 10);
        //队列中最大的数
        executor.setQueueCapacity(10);
        //线程名称前缀
        executor.setThreadNamePrefix("listener_");
        //rejectionPolicy：当pool已经达到max的时候，如何处理新任务
        //callerRuns：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后最大的存活时间
        executor.setKeepAliveSeconds(60);
        //初始化加载
        executor.initialize();
        return executor;
    }
}
