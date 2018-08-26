package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LineDTO extends AbstractDTO {

    @ApiParam("名字")
    private String name;

    @ApiParam("条件")
    private KeyValueDTO condition;
}
