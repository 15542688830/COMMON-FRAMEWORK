package com.keywordstone.framework.workflow.api;

import com.keywordstone.framework.common.container.jetty.JettyFrameWorkServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author k
 */
@SpringCloudApplication
@EnableEurekaClient
@EnableCaching
public class WorkflowApplication extends JettyFrameWorkServer {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }
}
