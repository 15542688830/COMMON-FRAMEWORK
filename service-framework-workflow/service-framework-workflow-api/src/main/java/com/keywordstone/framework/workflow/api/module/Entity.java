package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Getter;


/**
 * 工作流个体
 *
 * @author k
 */
@Getter
public abstract class Entity extends AbstractDTO {

    public Entity(String id) {
        super(id);
    }
}
