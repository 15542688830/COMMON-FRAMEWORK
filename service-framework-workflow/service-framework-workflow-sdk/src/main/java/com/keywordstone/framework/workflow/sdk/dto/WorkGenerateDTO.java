package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author k
 */
@Data
public class WorkGenerateDTO extends AbstractDTO {

    @ApiParam("工作流ID")
    @NotNull(message = "工作流ID不能为空")
    private String flowId;

    @ApiParam("工作流挂载数据")
    private Map<String, Object> entity;

    @ApiParam("备注")
    private String memo;
}
