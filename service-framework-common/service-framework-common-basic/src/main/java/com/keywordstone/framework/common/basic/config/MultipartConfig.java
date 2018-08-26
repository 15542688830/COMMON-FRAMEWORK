package com.keywordstone.framework.common.basic.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;

@Component
public class MultipartConfig {

    @Bean
    protected MultipartConfigElement element() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(40 * 1024L * 1024L);
        factory.setMaxRequestSize(40 * 1024L * 1024L);
        return factory.createMultipartConfig();
    }
}
