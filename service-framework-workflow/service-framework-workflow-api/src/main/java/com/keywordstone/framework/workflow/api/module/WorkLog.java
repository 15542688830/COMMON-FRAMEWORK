package com.keywordstone.framework.workflow.api.module;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkLog implements Serializable{

    /**
     * 工作流日志ID
     */
    private String id;

    /**
     * 挂载数据ID
     */
    private String entityId;

    /**
     * 操作者ID
     */
    private String operatorId;

    /**
     * 任务ID
     */
    private String workId;

    /**
     * 任务Key
     */
    private String workKey;

    /**
     * 任务名称
     */
    private String workName;

    /**
     * 操作Key
     */
    private String lineKey;

    /**
     * 操作名称
     */
    private String lineName;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 备注
     */
    private String memo;

    /**
     * 执行ID TODO
     */
    private String variableId;
}
