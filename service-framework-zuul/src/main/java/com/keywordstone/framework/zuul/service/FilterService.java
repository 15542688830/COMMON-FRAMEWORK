package com.keywordstone.framework.zuul.service;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;

/**
 * @author k
 */
public interface FilterService<T, DTO extends AbstractDTO> {

    /**
     * 生成过滤实体
     *
     * @param t
     * @return
     */
    DTO generate(T t);
}
