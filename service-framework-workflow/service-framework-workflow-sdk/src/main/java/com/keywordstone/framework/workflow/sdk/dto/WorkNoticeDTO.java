package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

@Data
public class WorkNoticeDTO extends AbstractDTO{

    @ApiParam("工作载体ID")
    private String entityId;

    @ApiParam("工作名称")
    private String workName;

    @ApiParam("接收者ID")
    private String receiverId;

    @ApiParam("已读")
    private boolean read;

    @ApiParam("发送时间")
    private Date sendDate;
}
