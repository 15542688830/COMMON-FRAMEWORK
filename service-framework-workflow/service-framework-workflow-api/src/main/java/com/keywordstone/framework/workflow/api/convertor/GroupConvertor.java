package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Group;
import com.keywordstone.framework.workflow.sdk.dto.GroupDTO;
import org.springframework.stereotype.Component;

/**
 * 工作流节点状态转换
 */
@Component
public class GroupConvertor extends AbstractConvertor<GroupDTO, Group> {
    @Override
    public GroupDTO toDTO(Group group) {
        if (null != group) {
            GroupDTO dto = new GroupDTO();
            dto.setId(group.getId());
            dto.setKey(group.getKey());
            dto.setName(group.getName());
            dto.setOperatorList(group.getOperatorList());
            return dto;
        }
        return null;
    }

    @Override
    public Group toEntity(GroupDTO dto) {
        if (null != dto) {
            Group group = new Group();
            group.setId(group.getId());
            group.setKey(dto.getKey());
            group.setOperatorList(dto.getOperatorList());
            return group;
        }
        return null;
    }
}
