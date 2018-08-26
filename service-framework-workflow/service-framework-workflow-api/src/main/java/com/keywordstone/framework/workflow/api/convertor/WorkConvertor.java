package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.sdk.dto.KeyValueDTO;
import com.keywordstone.framework.workflow.sdk.dto.LineDTO;
import com.keywordstone.framework.workflow.sdk.dto.WorkDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务状态转换
 */
@Component
public class WorkConvertor extends AbstractConvertor<WorkDTO, Work> {

    @Autowired
    private LineConvertor lineConvertor;

    @Override
    public WorkDTO toDTO(Work work) {
        if (null != work) {
            WorkDTO dto = new WorkDTO();
            dto.setId(work.getId());
            dto.setName(work.getName());
            dto.setFlowId(work.getFlowId());
            dto.setCreateUser(work.getCreateUser());
            dto.setCreateTime(work.getCreateTime());
            Map<String, Object> dataMap = work.getDataMap();
            dto.setData(dataMap);
            List<LineDTO> lineList = lineConvertor.toListDTO(work.getLineList());
            if (null != lineList && !lineList.isEmpty()) {
                if (null != dataMap) {
                    // 如果dataMap与line的condition有相同的参数 代表当前line是回滚类型的选项 所以只显示选项值
                    boolean exist = false;
                    boolean match;
                    List<LineDTO> filteredLineList = new ArrayList<>();
                    KeyValueDTO keyValueDTO;
                    for (LineDTO lineDTO : lineList) {
                        keyValueDTO = lineDTO.getCondition();
                        if (null == keyValueDTO
                                || StringUtils.isAnyEmpty(keyValueDTO.getKey(), keyValueDTO.getValue())) {
                            continue;
                        }
                        match = false;
                        if (dataMap.containsKey(keyValueDTO.getValue())
                                && keyValueDTO.getKey().equals(dataMap.get(keyValueDTO.getValue()))) {
                            match = true;
                            exist = true;
                        }
                        if (match || keyValueDTO.getValue().equals(work.getOperateKey())) {
                            filteredLineList.add(lineDTO);
                        }
                    }
                    if (exist) {
                        lineList = new ArrayList<>(filteredLineList);
                    }
                }
                dto.setLineList(lineList);
                dto.setImg(work.getImg());
            }
            return dto;
        }
        return null;
    }

    @Override
    public Work toEntity(WorkDTO dto) {
        return null;
    }
}
