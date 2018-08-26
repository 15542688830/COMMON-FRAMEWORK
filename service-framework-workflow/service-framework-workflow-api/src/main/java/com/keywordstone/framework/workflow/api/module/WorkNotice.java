package com.keywordstone.framework.workflow.api.module;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkNotice implements Serializable{

    /**
     * 主键
     */
    private String id;

    /**
     * 工作载体ID
     */
    private String entityId;

    /**
     * 工作名称
     */
    private String workName;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 0 未读 1已读
     */
    private Integer readFlag;

    /**
     * 发送时间
     */
    private Date sendDate;

    /**
     * 执行ID TODO
     */
    private String senderId;
}
