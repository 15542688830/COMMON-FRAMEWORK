package com.keywordstone.framework.workflow.sdk.enums;

import lombok.Getter;

@Getter
public enum LineStatusEnum {

    APPROVED(1, "同意"),

    DENIED(2, "拒绝"),

    COMPLETED(3, "完成");

    private int value;

    private String name;

    LineStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
