package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.WorkNoticeCondition;
import com.keywordstone.framework.workflow.sdk.dto.WorkNoticeConDTO;
import org.springframework.stereotype.Component;

/**
 * 工作流消息查询条件转换
 */
@Component
public class WorkNoticeConConvertor extends AbstractConvertor<WorkNoticeConDTO, WorkNoticeCondition> {
    @Override
    public WorkNoticeConDTO toDTO(WorkNoticeCondition workNoticeCondition) {
        return null;
    }

    @Override
    public WorkNoticeCondition toEntity(WorkNoticeConDTO dto) {
        if (null != dto) {
            WorkNoticeCondition condition = new WorkNoticeCondition();
            condition.setId(dto.getId());
            condition.setReceiverId(dto.getReceiverId());
            condition.setRead(dto.getRead());
            condition.setPageable(dto.getPageable());
            return condition;
        }
        return null;
    }
}
