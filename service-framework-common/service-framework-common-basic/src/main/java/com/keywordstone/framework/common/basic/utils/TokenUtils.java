package com.keywordstone.framework.common.basic.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author k
 */
@Component
public class TokenUtils extends OncePerRequestFilter {

    public final static String TOKEN = "token";

    public final static String USER_ID = "userId";

    private static ThreadLocal<String> tokenLocal = new ThreadLocal<>();

    private static ThreadLocal<String> userIdLocal = new ThreadLocal<>();

    public static String getToken() {
        if (null != tokenLocal) {
            return tokenLocal.get();
        }
        return null;
    }

    public static String getUserId() {
        if (null != userIdLocal) {
            return userIdLocal.get();
        }
        return null;
    }

    protected static void userIdReset(String userId) {
        userIdLocal.set(userId);
    }

    protected static void tokenReset(String token) {
        tokenLocal.set(token);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        tokenReset(httpServletRequest.getHeader(TOKEN));
        userIdReset(httpServletRequest.getHeader(USER_ID));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
