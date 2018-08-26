//package com.keywordstone.framework.common.security.client.interceptor;
//
//import com.baomidou.kisso.SSOHelper;
//import com.baomidou.kisso.common.util.HttpUtil;
//import com.baomidou.kisso.security.token.SSOToken;
//import com.baomidou.kisso.web.handler.KissoDefaultHandler;
//import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;
//import com.keywordstone.framework.common.security.client.annotation.SkipLogin;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.util.logging.Logger;
//
//@Component
//@Slf4j
//public class SkipLoginInterceptor extends HandlerInterceptorAdapter {
//    private static final Logger logger = Logger.getLogger("SSOInterceptor");
//    private SSOHandlerInterceptor handlerInterceptor;
//
//    public SkipLoginInterceptor() {
//    }
//
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            SkipLogin skipLogin = method.getAnnotation(SkipLogin.class);
//            if (skipLogin != null) {
//                return true;
//            }
//
//            SSOToken ssoToken = SSOHelper.getSSOToken(request);
//            if (ssoToken == null) {
//                if (HttpUtil.isAjax(request)) {
//                    this.getHandlerInterceptor().preTokenIsNullAjax(request, response);
//                    return false;
//                }
//
//                if (this.getHandlerInterceptor().preTokenIsNull(request, response)) {
//                    logger.fine("logout. request url:" + request.getRequestURL());
//                    SSOHelper.clearRedirectLogin(request, response);
//                }
//
//                return false;
//            }
//
//            request.setAttribute("kissoTokenAttr", ssoToken);
//        }
//
//        return true;
//    }
//
//    public SSOHandlerInterceptor getHandlerInterceptor() {
//        return this.handlerInterceptor == null ? KissoDefaultHandler.getInstance() : this.handlerInterceptor;
//    }
//
//    public void setHandlerInterceptor(SSOHandlerInterceptor handlerInterceptor) {
//        this.handlerInterceptor = handlerInterceptor;
//    }
//}
