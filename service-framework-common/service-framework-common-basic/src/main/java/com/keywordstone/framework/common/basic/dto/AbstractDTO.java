package com.keywordstone.framework.common.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author k
 */
@Data
public abstract class AbstractDTO implements Serializable{

    private String id;

    public AbstractDTO() {
    }

    public AbstractDTO(String id) {
        this.id = id;
    }
}
