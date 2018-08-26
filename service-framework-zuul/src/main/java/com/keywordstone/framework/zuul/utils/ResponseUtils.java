package com.keywordstone.framework.zuul.utils;

import com.alibaba.fastjson.JSON;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class ResponseUtils {

    public static void setData(ResultDTO data,
                               RequestContext context) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(HttpServletResponse.SC_FORBIDDEN);
        context.setResponseBody(JSON.toJSONString(data));
        context.getResponse().setContentType(ContentType.APPLICATION_JSON.toString());
    }
}
