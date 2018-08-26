//package com.keywordstone.framework.common.queue.rocketmq.client;
//
//import com.keywordstone.framework.common.queue.rocketmq.annotation.RocketConsume;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RocketConsume(topic = "SSS222", tags = "SSS222", clazz = Integer.class)
//public class SSS222 extends RocketMqConsumer<Integer> {
//    @Override
//    public boolean consume(Integer integer) {
//        log.info("接收成功:" + integer);
//        return true;
//    }
//}
