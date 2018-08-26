package com.keywordstone.framework.zuul.service.impl;

import com.keywordstone.framework.common.cache.redis.client.Redis;
import com.keywordstone.framework.zuul.dto.TokenDTO;
import com.keywordstone.framework.zuul.entity.Token;
import com.keywordstone.framework.zuul.service.FilterService;
import com.keywordstone.framework.zuul.utils.ZuulConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author k
 */
@Service
public class TokenFilSerImpl implements FilterService<Token, TokenDTO> {

    @Value("${token.default.timeout}")
    private int tokenDefaultTimeout;

    @Override
    public TokenDTO generate(Token token) {
        if (StringUtils.isNotEmpty(token.getToken())) {
            // TODO 验证当前token是否为第一次使用 如果是则存储起来
            if (StringUtils.isEmpty(Redis.tokenGet(ZuulConst.REDIS_TOKEN_IP))) {
                Redis.tokenPut(ZuulConst.REDIS_TOKEN_IP, token.getIp());
                Redis.tokenOut(tokenDefaultTimeout);
            }
            TokenDTO dto = new TokenDTO(token.getToken());
            dto.setIp(token.getIp());
            return dto;
        }
        return null;
    }
}
