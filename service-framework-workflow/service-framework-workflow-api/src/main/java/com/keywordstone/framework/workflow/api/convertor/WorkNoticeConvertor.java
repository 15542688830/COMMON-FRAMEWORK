package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.WorkNotice;
import com.keywordstone.framework.workflow.sdk.dto.WorkNoticeDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkNoticeConvertor extends AbstractConvertor<WorkNoticeDTO, WorkNotice> {
    @Override
    public WorkNoticeDTO toDTO(WorkNotice workNotice) {
        if (null != workNotice) {
            WorkNoticeDTO dto = new WorkNoticeDTO();
            dto.setId(workNotice.getId());
            dto.setEntityId(workNotice.getEntityId());
            dto.setReceiverId(workNotice.getReceiverId());
//            dto.setRead(workNotice.isRead());
            dto.setWorkName(workNotice.getWorkName());
            dto.setSendDate(workNotice.getSendDate());
            return dto;
        }
        return null;
    }

    @Override
    public WorkNotice toEntity(WorkNoticeDTO dto) {
        return null;
    }
}
