package com.keywordstone.framework.common.basic.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResultError implements Serializable{

    private String message;

    private String field;

    private ResultError() {
    }

    private ResultError(String message) {
        this.message = message;
    }

    private ResultError(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public static ResultError error(String message) {
        return new ResultError(message);
    }

    public static ResultError error(String message, String field) {
        return new ResultError(message, field);
    }
}
