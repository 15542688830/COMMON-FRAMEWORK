package com.keywordstone.framework.workflow.api.module;

import lombok.Data;

@Data
public class WorkLogCondition {

    /**
     * 挂载数据ID
     */
    private String entityId;

    /**
     * 操作名称等于
     */
    private String lineNameEquals;

    /**
     * 操作名称不等于
     */
    private String lineNameNotEquals;
}
