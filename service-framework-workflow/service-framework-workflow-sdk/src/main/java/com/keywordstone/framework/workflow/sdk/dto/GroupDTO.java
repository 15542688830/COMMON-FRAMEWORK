package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class GroupDTO extends AbstractDTO {

    @ApiParam("任务主键")
    private String key;

    @ApiParam("任务名称")
    private String name;

    @ApiParam("人员ID集合")
    private List<String> operatorList;
}
