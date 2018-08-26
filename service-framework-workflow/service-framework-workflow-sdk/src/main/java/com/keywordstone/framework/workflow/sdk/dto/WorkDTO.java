package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author k
 */
@Data
@ApiModel("工作内容")
public class WorkDTO extends AbstractDTO {

    @ApiParam("名称")
    private String name;

    @ApiParam("对应工作流ID")
    private String flowId;

    @ApiParam("挂载数据")
    private Map<String, Object> data;

    @ApiParam("操作选项")
    private List<LineDTO> lineList;

    @ApiParam("创建人")
    private String createUser;

    @ApiParam("创建时间")
    private Date createTime;

    @ApiParam("流程示意图（base64）")
    private String img;
}
