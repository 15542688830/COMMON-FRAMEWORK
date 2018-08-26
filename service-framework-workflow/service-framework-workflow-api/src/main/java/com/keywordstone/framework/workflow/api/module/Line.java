package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import lombok.Data;

@Data
public class Line extends BasicEntity {

    /**
     * 名字
     */
    private String name;

    /**
     * 条件
     */
    private String condition;
}
