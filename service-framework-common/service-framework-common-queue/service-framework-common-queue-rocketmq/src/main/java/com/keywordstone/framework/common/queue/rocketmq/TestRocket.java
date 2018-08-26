//package com.keywordstone.framework.common.queue.rocketmq;
//
//import com.alibaba.rocketmq.client.ClientConfig;
//import com.keywordstone.framework.common.queue.rocketmq.client.RocketMqProducer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//@SpringBootApplication
//@Slf4j
//@RestController
//public class TestRocket {
//
//    public static void main(String[] args) {
//        SpringApplication.run(TestRocket.class, args);
//    }
//
//    @RequestMapping(value = "test", method = RequestMethod.POST)
//    public void test() {
////        RocketMqProducer.send("SSS222", "SSS222", 222);
//        RocketMqProducer.send("SSS", "SSS", "SSS");
////        RocketMqProducer.send("TTTAAA", "TTTAAA", new ClientConfig());
//    }
//}
