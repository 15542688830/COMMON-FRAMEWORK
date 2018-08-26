package com.keywordstone.framework.common.basic.enums;

import lombok.Getter;

@Getter
public enum DataFlagEnum {

    /**
     * 有效的
     */
    FUNCTIONAL(0, "有效的"),

    /**
     * 已删除
     */
    REMOVED(1, "已删除");

    private int value;

    private String name;

    DataFlagEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
