package com.keywordstone.framework.common.security.server.utils;

import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.common.cache.redis.client.Redis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static void login(String userId) {
        Redis.tokenPut(TokenUtils.USER_ID, userId);
    }

    public static void logout() {
        Redis.tokenDel(TokenUtils.USER_ID);
    }

    public static boolean checkLogin() {
        return StringUtils.isNotEmpty(Redis.tokenGet(TokenUtils.USER_ID));
    }

    public static String getUserId() {
        return Redis.tokenGet(TokenUtils.USER_ID);
    }
}
