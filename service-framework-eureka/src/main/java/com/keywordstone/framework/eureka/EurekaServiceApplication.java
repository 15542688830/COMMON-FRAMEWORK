package com.keywordstone.framework.eureka;

import com.keywordstone.framework.common.container.jetty.JettyFrameWorkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class EurekaServiceApplication extends JettyFrameWorkServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
    }
}
