package com.keywordstone.framework.common.basic.entity;

import com.keywordstone.framework.common.basic.enums.OrderByEnum;
import lombok.Getter;

@Getter
public class OrderBy {

    /**
     * 键
     */
    private String byWhat;

    /**
     * 序
     */
    private OrderByEnum order;

    private OrderBy(String byWhat,
                    OrderByEnum order) {
        this.byWhat = byWhat;
        this.order = order;
    }

    protected static OrderBy orderBy(String byWhat,
                                     OrderByEnum order) {
        return new OrderBy(byWhat, order);
    }
}
