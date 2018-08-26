package com.keywordstone.framework.workflow.api.module;

import lombok.Data;

import java.util.Date;

@Data
public class DateRange {

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;
}
