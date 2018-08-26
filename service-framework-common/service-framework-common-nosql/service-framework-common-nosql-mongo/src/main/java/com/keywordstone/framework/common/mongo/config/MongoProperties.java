package com.keywordstone.framework.common.mongo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mongodb")
@Data
public class MongoProperties {

    private List<String> hostAndPorts;

    private String username;

    private String password;
}
