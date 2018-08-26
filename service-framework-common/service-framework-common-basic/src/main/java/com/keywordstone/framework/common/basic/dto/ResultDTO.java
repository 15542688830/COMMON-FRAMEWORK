package com.keywordstone.framework.common.basic.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
public class ResultDTO<T> implements Serializable {

    public final static int SUCCESS = 0;

    public final static int FAILURE = 1;

    private int code;

    private T data;

    private List<ResultError> errors;

    private ResultDTO() {
    }

    private ResultDTO(int code) {
        this.code = code;
    }

    private ResultDTO(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ResultDTO(int code, List<ResultError> errors) {
        this.code = code;
        this.errors = errors;
    }

    public static ResultDTO success() {
        return new ResultDTO(SUCCESS);
    }

    public static <T> ResultDTO success(T data) {
        return new ResultDTO(SUCCESS, data);
    }

    public static ResultDTO failure() {
        return new ResultDTO(FAILURE);
    }

    public static ResultDTO failure(ResultError... errors) {
        if (null == errors || errors.length == 0) {
            return failure();
        }
        return new ResultDTO(FAILURE, Arrays.asList(errors));
    }

    public boolean isSuccess() {
        return this.code == SUCCESS;
    }
}
