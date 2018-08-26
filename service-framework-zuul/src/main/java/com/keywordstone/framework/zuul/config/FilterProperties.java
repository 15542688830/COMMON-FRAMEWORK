package com.keywordstone.framework.zuul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "ignored.urls")
@Data
public class FilterProperties {

    private List<String> token;
}
