package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 13:17 2018/3/15
 * @Modified By:
 */
@Data
public class BorrowContractDTO extends AbstractDTO {

    //任务主键
    private String taskId;
    //借阅人
    private String borrowPersonId;
    //合同主键
    private String contractId;
    //审核状态 1 通过  0 拒绝
    private String taskReview;
    //借阅事由
    private String scopeUse;
    //领导批示
    private String leaderOption;
    //借阅附件集合
    private List<String> fileArr;
    //申请日期
    private String applyDate;
    //借阅日期
    private String borrowDate;
    //归还状态 0 未归还 1 已归还
    private String returnStatus;
    //归还日期
    private String returnDate;
    //预计归还日期
    private String expectDate;
    //到期提醒日期
    private String dueDate;
    //备注
    private String memo;
    //版本
    private String version;
}
