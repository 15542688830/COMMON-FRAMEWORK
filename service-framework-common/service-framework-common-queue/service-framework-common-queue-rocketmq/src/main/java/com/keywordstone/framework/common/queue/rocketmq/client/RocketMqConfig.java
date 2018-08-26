package com.keywordstone.framework.common.queue.rocketmq.client;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.keywordstone.framework.common.queue.rocketmq.annotation.RocketConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RocketMqConfig extends WebApplicationObjectSupport {

    private final static String COMMA = ",";
    private final static String SPACE_OR_SPACE = " || ";

    protected static String appName;
    protected static String namesrvaddr;

    @Autowired
    private RocketMqProperties properties;

    @Value("${spring.application.name}")
    private void setAppName(String appName) {
        RocketMqConfig.appName = appName;
    }

    @Bean
    protected CommandLineRunner pushConsumer() {
        if (null != properties) {
            List<String> namesrvaddr = properties.getNamesrvaddr();
            if (null != namesrvaddr
                    && !namesrvaddr.isEmpty()) {
                RocketMqConfig.namesrvaddr = namesrvaddr.stream().collect(Collectors.joining(COMMA));
                List<RocketMqConsumer> rocketMqConsumerList = getRocketConsumers();
                if (null != rocketMqConsumerList
                        && !rocketMqConsumerList.isEmpty()) {
                    try {
                        RocketConsume rocketConsume;
                        for (RocketMqConsumer rocketMqConsumer : rocketMqConsumerList) {
                            rocketConsume = rocketMqConsumer.getClass().getAnnotation(RocketConsume.class);
                            if (null != rocketConsume) {
                                String topic = rocketConsume.topic();
                                String tags = Arrays.stream(rocketConsume.tags())
                                        .collect(Collectors.joining(SPACE_OR_SPACE));
                                Class clazz = rocketConsume.clazz();
                                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
                                consumer.setNamesrvAddr(RocketMqConfig.namesrvaddr);
                                consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                                consumer.setConsumerGroup(topic);
                                consumer.subscribe(topic, tags);
                                rocketMqConsumer.setTClass(clazz);
                                consumer.setMessageListener(rocketMqConsumer);
                                consumer.setMessageModel(MessageModel.CLUSTERING);
                                consumer.start();
                            }
                        }
                    } catch (MQClientException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return null;
    }

    public List<RocketMqConsumer> getRocketConsumers() {
        ApplicationContext context = getWebApplicationContext();
        Map<String, RocketMqConsumer> rocketMqConsumerMap = context.getBeansOfType(RocketMqConsumer.class);
        if (null != rocketMqConsumerMap && !rocketMqConsumerMap.isEmpty()) {
            Collection<RocketMqConsumer> rocketMqConsumerCollection = rocketMqConsumerMap.values();
            if (null != rocketMqConsumerCollection && !rocketMqConsumerCollection.isEmpty()) {
                return rocketMqConsumerCollection.stream()
                        .filter(rocketMqConsumer -> null != rocketMqConsumer
                                && rocketMqConsumer.getClass().isAnnotationPresent(RocketConsume.class))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
