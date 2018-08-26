package com.keywordstone.framework.zuul;

import com.keywordstone.framework.common.container.jetty.JettyFrameWorkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author k
 */
@SpringBootApplication
@EnableZuulProxy
@Slf4j
public class ZuulApplication extends JettyFrameWorkServer {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
