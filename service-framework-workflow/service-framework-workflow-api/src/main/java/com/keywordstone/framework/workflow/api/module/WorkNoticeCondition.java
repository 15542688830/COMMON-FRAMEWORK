package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.Pageable;
import lombok.Data;

@Data
public class WorkNoticeCondition {

    /**
     * 主键
     */
    private String id;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 已读
     */
    private Boolean read;

    /**
     * 分页器
     */
    private Pageable pageable;
}
