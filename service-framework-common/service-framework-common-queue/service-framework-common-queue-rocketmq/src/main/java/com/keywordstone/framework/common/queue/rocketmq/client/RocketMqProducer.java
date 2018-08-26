package com.keywordstone.framework.common.queue.rocketmq.client;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RocketMqProducer {

    public final static String DEFAULT_TAG = "*";
    private final static String LOG_SUCCESS = "发送成功：";
    private final static String LOG_FAILURE = "发送失败：";

    private RocketMqProducer() {
    }

    public static <T> void send(String topic,
                                T t) {
        send(topic, DEFAULT_TAG, t);
    }

    public static <T> void send(String topic,
                                String tag,
                                T t) {
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConfig.appName);
        producer.setNamesrvAddr(RocketMqConfig.namesrvaddr);
        try {
            producer.start();
            String json=new Gson().toJson(t);
            Message message = new Message(topic, tag, json.getBytes());
            SendResult result = producer.send(message);
            log.info(LOG_SUCCESS + result.getSendStatus());
        } catch (Exception e) {
            log.error(LOG_FAILURE + e.getMessage(), e);
        } finally {
            producer.shutdown();
        }
    }
}
