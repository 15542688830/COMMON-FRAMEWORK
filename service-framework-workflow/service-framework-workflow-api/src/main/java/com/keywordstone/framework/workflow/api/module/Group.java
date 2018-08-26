package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import lombok.Data;

import java.util.List;

/**
 * 工作流权限群体
 *
 * @author k
 */
@Data
public class Group extends BasicEntity {

    /**
     * 任务主键
     */
    private String key;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 人物ID集合
     */
    private List<String> operatorList;
}
