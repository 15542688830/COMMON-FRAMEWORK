package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.WorkLog;
import com.keywordstone.framework.workflow.sdk.dto.WorkLogDTO;
import org.springframework.stereotype.Component;

/**
 * 工作流日志转换
 */
@Component
public class WorkLogConvertor extends AbstractConvertor<WorkLogDTO, WorkLog> {
    @Override
    public WorkLogDTO toDTO(WorkLog workLog) {
        if (null != workLog) {
            WorkLogDTO dto = new WorkLogDTO();
            dto.setId(workLog.getId());
            dto.setEntityId(workLog.getEntityId());
            dto.setLineName(workLog.getLineName());
            dto.setOperatorId(workLog.getOperatorId());
            dto.setOperateTime(workLog.getOperateTime());
            dto.setWorkName(workLog.getWorkName());
            dto.setMemo(workLog.getMemo());
            return dto;
        }
        return null;
    }

    @Override
    public WorkLog toEntity(WorkLogDTO dto) {
        return null;
    }
}
