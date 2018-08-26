package com.keywordstone.framework.common.basic.enums;

import lombok.Getter;

@Getter
public enum OrderByEnum {

    /**
     * 正序
     */
    ASC(1, "ASC"),

    /**
     * 倒序
     */
    DESC(0, "DESC");

    private int key;

    private String value;

    OrderByEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
