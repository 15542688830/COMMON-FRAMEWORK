package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.DateRange;
import com.keywordstone.framework.workflow.sdk.dto.DateRangeDTO;
import org.springframework.stereotype.Component;

/**
 * 时间区间转换
 */
@Component
public class DateRangeConvertor extends AbstractConvertor<DateRangeDTO, DateRange> {
    @Override
    public DateRangeDTO toDTO(DateRange dateRange) {
        return null;
    }

    @Override
    public DateRange toEntity(DateRangeDTO dto) {
        if (null != dto) {
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(dto.getStartDate());
            dateRange.setEndDate(dto.getEndDate());
            return dateRange;
        }
        return null;
    }
}
