package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Flow;
import com.keywordstone.framework.workflow.api.module.Group;
import com.keywordstone.framework.workflow.api.service.BpmnService;
import com.keywordstone.framework.workflow.sdk.dto.FlowDTO;
import com.keywordstone.framework.workflow.sdk.dto.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作流状态转换
 */
@Component
public class FlowConvertor extends AbstractConvertor<FlowDTO, Flow> {

    @Autowired
    private GroupConvertor groupConvertor;

    @Autowired
    private BpmnService<Flow> flowBpmnService;

    @Override
    public FlowDTO toDTO(Flow flow) {
        if (null != flow) {
            FlowDTO dto = new FlowDTO();
            dto.setId(flow.getId());
            dto.setName(flow.getName());
            List<Group> groupList = flow.getGroupList();
            if (null != groupList && !groupList.isEmpty()) {
                dto.setGroupList(groupList.stream()
                        .map(group -> groupConvertor.toDTO(group))
                        .collect(Collectors.toList()));
            }
            return dto;
        }
        return null;
    }

    @Override
    public Flow toEntity(FlowDTO dto) {
        if (null != dto) {
            Flow flow = flowBpmnService.findById(dto.getId());
            List<GroupDTO> groupList = dto.getGroupList();
            if (null != groupList && !groupList.isEmpty()) {
                flow.setGroupList(groupList.stream()
                        .map(groupDTO -> groupConvertor.toEntity(groupDTO))
                        .collect(Collectors.toList()));
            }
            flow.setDataFlag(dto.getIsDelete());
            return flow;
        }
        return null;
    }
}
