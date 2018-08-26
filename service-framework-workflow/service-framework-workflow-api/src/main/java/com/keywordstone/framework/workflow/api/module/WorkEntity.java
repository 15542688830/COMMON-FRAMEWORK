package com.keywordstone.framework.workflow.api.module;

import lombok.Data;

import java.util.List;
@Data
public class WorkEntity {

    /**
     * 所有在线任务
     */
    private List<Work> workList;

    /**
     * 流程图（base64）
     */
    private String base64;
}
