package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.workflow.sdk.enums.LineStatusEnum;
import com.keywordstone.framework.workflow.sdk.enums.WorkStatusEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class WorkConditionDTO extends AbstractDTO {

    @ApiParam("工作状态")
    private WorkStatusEnum workStatus;

    @ApiParam("工作流业务主键")
    private String flowKey;

    @ApiParam("工作流载体主键")
    private String entityId;

    @ApiParam("日期区间")
    private DateRangeDTO dateRange;

    @ApiParam("分页器")
    private Pageable pageable;

    @ApiParam("操作者")
    private String operator;

    @ApiParam("操作选项")
    private LineStatusEnum lineStatus;
}
