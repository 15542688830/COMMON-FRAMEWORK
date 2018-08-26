package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.workflow.sdk.enums.LineStatusEnum;
import com.keywordstone.framework.workflow.sdk.enums.WorkStatusEnum;
import lombok.Data;

@Data
public class WorkCondition extends BasicEntity{

    /**
     * 工作状态
     */
    private WorkStatusEnum workStatus;

    /**
     * 工作流业务主键
     */
    private String flowKey;

    /**
     * 工作流载体主键
     */
    private String entityId;

    /**
     * 日期区间
     */
    private DateRange dateRange;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 分页器
     */
    private Pageable pageable;

    /**
     * 操作选项
     */
    private LineStatusEnum lineStatus;
}
