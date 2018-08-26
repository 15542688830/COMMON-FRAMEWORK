package com.keywordstone.framework.common.feign.config;

import com.keywordstone.framework.common.basic.utils.TokenUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TokenUtils.TOKEN, TokenUtils.getToken());
        requestTemplate.header(TokenUtils.USER_ID, TokenUtils.getUserId());
    }
}
