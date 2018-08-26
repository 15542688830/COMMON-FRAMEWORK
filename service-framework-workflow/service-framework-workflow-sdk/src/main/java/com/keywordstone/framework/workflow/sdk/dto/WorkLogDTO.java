package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

@Data
public class WorkLogDTO extends AbstractDTO {

    @ApiParam("工作流实例ID")
    private String id;

    @ApiParam("挂载数据ID")
    private String entityId;

    @ApiParam("操作者ID")
    private String operatorId;

    @ApiParam("任务名称")
    private String workName;

    @ApiParam("操作名称")
    private String lineName;

    @ApiParam("操作时间")
    private Date operateTime;

    @ApiParam("备忘")
    private String memo;
}
