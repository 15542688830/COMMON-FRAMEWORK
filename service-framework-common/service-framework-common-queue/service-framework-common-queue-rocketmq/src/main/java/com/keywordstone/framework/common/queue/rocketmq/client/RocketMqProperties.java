package com.keywordstone.framework.common.queue.rocketmq.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties("rocketmq")
public class RocketMqProperties {

    private List<String> namesrvaddr;
}
