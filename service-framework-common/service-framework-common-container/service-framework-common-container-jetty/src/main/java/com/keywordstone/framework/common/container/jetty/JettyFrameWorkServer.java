package com.keywordstone.framework.common.container.jetty;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class JettyFrameWorkServer extends SpringBootServletInitializer {

    @Bean
    @Primary
    public JettyFrameWorkServer server() {
        JettyFrameWorkServer server = new JettyFrameWorkServer();
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
//        builder.logStartupInfo(true)
//                .build();
        server.configure(builder);
        return server;
    }
}
