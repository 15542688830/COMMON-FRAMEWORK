package com.keywordstone.framework.common.basic.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class PageResultDTO<ENTITY> implements Serializable {

    private long count;

    private List<ENTITY> result;

    private PageResultDTO() {
    }

    private PageResultDTO(long count,
                          List<ENTITY> result) {
        this.count = count;
        this.result = result;
    }

    public static <ENTITY> PageResultDTO result(long count,
                                                List<ENTITY> result) {
        return new PageResultDTO(count, result);
    }

    public static PageResultDTO empty() {
        return new PageResultDTO(0, null);
    }
}
