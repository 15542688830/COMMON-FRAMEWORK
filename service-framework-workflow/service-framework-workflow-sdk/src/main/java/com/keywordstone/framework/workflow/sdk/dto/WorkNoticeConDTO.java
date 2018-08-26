package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class WorkNoticeConDTO extends AbstractDTO {

    @ApiParam("接收者ID")
    private String receiverId;

    @ApiParam("已读")
    private Boolean read;

    @ApiParam("分页器")
    private Pageable pageable;
}
