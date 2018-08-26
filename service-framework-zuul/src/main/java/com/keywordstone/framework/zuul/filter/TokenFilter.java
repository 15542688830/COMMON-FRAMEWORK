package com.keywordstone.framework.zuul.filter;

import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.common.basic.validator.AbstractValidator;
import com.keywordstone.framework.common.security.server.utils.SecurityUtils;
import com.keywordstone.framework.zuul.config.FilterProperties;
import com.keywordstone.framework.zuul.dto.TokenDTO;
import com.keywordstone.framework.zuul.entity.Token;
import com.keywordstone.framework.zuul.service.FilterService;
import com.keywordstone.framework.zuul.utils.ResponseUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author k
 */
@Component
@Slf4j
@Transactional
public class TokenFilter extends ZuulFilter {

    @Autowired
    private FilterService<Token, TokenDTO> filterService;
    @Autowired
    private AbstractValidator<TokenDTO> validator;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        String requestURI = context.getRequest().getRequestURI();
        if (filterProperties.getToken().stream()
                .anyMatch(s -> requestURI.startsWith(s))) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = request.getHeader(TokenUtils.TOKEN);
        log.info("token: " + token + " uri: " + request.getRequestURI());
        String ip = null;// TODO
        Token entity = new Token();
        entity.setToken(token);
        entity.setIp(ip);
        String validateResult = validator.validate(filterService.generate(entity));
        if (StringUtils.isNotEmpty(validateResult)) {
            log.error(validateResult + ": " + token);
            ResponseUtils.setData(ResultDTO.failure(ResultError.error(validateResult)), context);
        }
        String userId = SecurityUtils.getUserId();
        log.info("userId: " + userId + " uri: " + request.getRequestURI());
        if (StringUtils.isNotEmpty(userId)) {
            context.getZuulRequestHeaders().put(TokenUtils.USER_ID, userId);
        }
        return null;
    }
}
