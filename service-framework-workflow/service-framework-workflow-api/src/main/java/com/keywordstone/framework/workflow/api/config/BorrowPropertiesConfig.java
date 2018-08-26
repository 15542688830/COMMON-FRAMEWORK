package com.keywordstone.framework.workflow.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 11:20 2018/5/11
 * @Modified By:
 */

@Data
@Component
@ConfigurationProperties(prefix = "flowable")
public class BorrowPropertiesConfig {

    /**
     * 工作流是否需要重新部署
     */
    private boolean borrowDeploy = false;

    /**
     * 工作流部署历史版本
     */
    private Integer version;

    BorrowPropertiesConfig() {
    }
}