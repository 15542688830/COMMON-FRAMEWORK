package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Map;

/**
 * @author k
 */
@Data
public class WorkUpdateDTO extends AbstractDTO {

    @ApiParam("工作流挂载数据")
    private Map<String, Object> entity;

    @ApiParam("操作")
    private String lineId;

    @ApiParam("备注")
    private String memo;
}
