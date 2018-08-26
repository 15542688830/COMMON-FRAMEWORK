package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class FlowDTO extends AbstractDTO {

    @ApiParam("工作流名称")
    private String name;

    @ApiParam("是否删除绑定 1 删除")
    private Integer isDelete;

    @ApiParam("群组集合")
    private List<GroupDTO> groupList;
}
