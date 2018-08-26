package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

@Data
public class DateRangeDTO extends AbstractDTO {

    @ApiParam("开始时间")
    private Date startDate;

    @ApiParam("结束时间")
    private Date endDate;
}
