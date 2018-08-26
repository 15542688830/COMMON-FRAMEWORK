//package com.keywordstone.framework.common.queue.rocketmq.client;
//
//import com.alibaba.rocketmq.client.ClientConfig;
//import com.keywordstone.framework.common.queue.rocketmq.annotation.RocketConsume;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RocketConsume(topic = "TTTAAA", tags = "TTTAAA", clazz = ClientConfig.class)
//public class TTTAAA extends RocketMqConsumer<ClientConfig> {
//    @Override
//    public boolean consume(ClientConfig clientConfig) {
//        log.info("接收成功：" + clientConfig);
//        return true;
//    }
//}
