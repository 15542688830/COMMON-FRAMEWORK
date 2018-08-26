package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.WorkCondition;
import com.keywordstone.framework.workflow.sdk.dto.WorkConditionDTO;
import com.keywordstone.framework.workflow.sdk.enums.WorkStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流查询条件转换
 */
@Component
public class WorkConditionConvertor extends AbstractConvertor<WorkConditionDTO, WorkCondition> {

    @Autowired
    private DateRangeConvertor dateRangeConvertor;

    @Override
    public WorkConditionDTO toDTO(WorkCondition workCondition) {
        return null;
    }

    @Override
    public WorkCondition toEntity(WorkConditionDTO dto) {
        if (null != dto) {
            WorkCondition workCondition = new WorkCondition();
            workCondition.setId(dto.getId());
            workCondition.setFlowKey(dto.getFlowKey());
            workCondition.setOperator(dto.getOperator());
            workCondition.setEntityId(dto.getEntityId());
            workCondition.setDateRange(dateRangeConvertor.toEntity(dto.getDateRange()));
            workCondition.setPageable(dto.getPageable());
            WorkStatusEnum workStatus = dto.getWorkStatus();
            workCondition.setWorkStatus(workStatus);
            if (null != workStatus && WorkStatusEnum.COMPLETED.equals(workStatus)) {
                workCondition.setLineStatus(dto.getLineStatus());
            }
            return workCondition;
        }
        return null;
    }
}
