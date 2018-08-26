package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class WorkEntityDTO extends AbstractDTO {

    @ApiParam("发起人")
    private String starterId;

    @ApiParam("当前状态任务名")
    private List<String> workNames;

    @ApiParam("流程示意图（base64）")
    private String base64;
}
