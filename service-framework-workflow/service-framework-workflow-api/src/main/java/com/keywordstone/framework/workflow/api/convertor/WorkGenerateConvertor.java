package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Flow;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.api.service.BpmnService;
import com.keywordstone.framework.workflow.sdk.dto.WorkGenerateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流发起转换
 */
@Component
public class WorkGenerateConvertor extends AbstractConvertor<WorkGenerateDTO, Work> {

    @Autowired
    private BpmnService<Flow> flowBpmnService;

    @Override
    public WorkGenerateDTO toDTO(Work work) {
        return null;
    }

    @Override
    public Work toEntity(WorkGenerateDTO dto) {
        if (null != dto) {
            Flow flow = flowBpmnService.findById(dto.getFlowId());
            if (null != flow) {
                Work work = new Work(flow.getId(), dto.getEntity());
                work.setId(dto.getFlowId());
                work.setMemo(dto.getMemo());
                return work;
            }
        }
        return null;
    }
}
