package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import lombok.Data;

import java.util.List;

/**
 * 工作流
 *
 * @author k
 */
@Data
public class Flow extends BasicEntity {

    /**
     * 工作流业务主键
     */
    private String key;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 工作群组集合
     */
    private List<Group> groupList;
}
