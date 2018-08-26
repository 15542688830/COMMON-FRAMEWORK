package com.keywordstone.framework.common.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author k
 */
@Component
public class MongoConfig {

    private static MongoDatabase database;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private MongoProperties properties;

    public static MongoDatabase getDatabase() {
        return database;
    }

    @Bean
    protected MongoClient client() {
        List<String> hostAndPorts = properties.getHostAndPorts();
        if (null != hostAndPorts) {
            List<ServerAddress> serverAddressList = hostAndPorts.stream()
                    .filter(hostAndPort -> StringUtils.isNotEmpty(hostAndPort)
                            && hostAndPort.contains(":"))
                    .map(hostAndPort -> new ServerAddress(
                            hostAndPort.split(":")[0],
                            Integer.valueOf(hostAndPort.split(":")[1])))
                    .collect(Collectors.toList());
            String username = properties.getUsername();
            String password = properties.getPassword();
            MongoClient client;
            if (StringUtils.isNoneEmpty(username, password)) {
                MongoCredential credential = MongoCredential.createCredential(
                        properties.getUsername(),
                        "admin",
                        properties.getPassword().toCharArray());
                client = new MongoClient(serverAddressList,
                        Arrays.asList(credential),
                        MongoClientOptions.builder().build());
            } else {
                client = new MongoClient(serverAddressList);
            }
            database = client.getDatabase(appName);
            return client;
        }
        return null;
    }
}
