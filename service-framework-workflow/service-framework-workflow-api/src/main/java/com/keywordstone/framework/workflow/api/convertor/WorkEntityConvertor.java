package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.api.module.WorkEntity;
import com.keywordstone.framework.workflow.sdk.dto.WorkEntityDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作流挂载实体状态转换
 */
@Component
public class WorkEntityConvertor extends AbstractConvertor<WorkEntityDTO, WorkEntity> {

    @Override
    public WorkEntityDTO toDTO(WorkEntity workEntity) {
        if (null != workEntity) {
            WorkEntityDTO dto = new WorkEntityDTO();
            List<Work> works = workEntity.getWorkList();
            if (null != works) {
                dto.setStarterId(works.get(0).getCreateUser());
                dto.setWorkNames(works.stream().map(work -> work.getName()).collect(Collectors.toList()));
            }
            dto.setBase64(workEntity.getBase64());
            return dto;
        }
        return null;
    }

    @Override
    public WorkEntity toEntity(WorkEntityDTO dto) {
        return null;
    }
}
