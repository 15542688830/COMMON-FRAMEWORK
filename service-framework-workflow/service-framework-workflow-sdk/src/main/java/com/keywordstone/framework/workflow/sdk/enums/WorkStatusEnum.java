package com.keywordstone.framework.workflow.sdk.enums;

import lombok.Getter;

@Getter
public enum WorkStatusEnum {

    COMPLETED(1, "已完成"),

    UNCOMPLETED(2, "待完成");

    private int value;

    private String name;

    WorkStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
