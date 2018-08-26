package com.keywordstone.framework.common.feign.config;

import feign.RequestInterceptor;
import org.springframework.cloud.netflix.feign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FeignConfig {

    @Bean
    protected RequestInterceptor interceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    @Primary
    protected FeignClientProperties properties() {
        FeignClientProperties properties = new FeignClientProperties();
        FeignClientProperties.FeignClientConfiguration configuration
                = new FeignClientProperties.FeignClientConfiguration();
        configuration.setConnectTimeout(5000);
        configuration.setReadTimeout(5000);
        Map map = new HashMap();
        map.put("default", configuration);
        properties.setConfig(map);
        return properties;
    }
}
