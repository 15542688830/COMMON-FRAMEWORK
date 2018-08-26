package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Line;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.api.service.WorkService;
import com.keywordstone.framework.workflow.sdk.dto.WorkUpdateDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 工作流执行转换
 */
@Component
public class WorkUpdateConvertor extends AbstractConvertor<WorkUpdateDTO, Work> {

    @Autowired
    private WorkService workService;

    @Autowired
    private LineConvertor lineConvertor;

    @Override
    public WorkUpdateDTO toDTO(Work work) {
        return null;
    }

    @Override
    public Work toEntity(WorkUpdateDTO dto) {
        if (null != dto) {
            Work work = new Work();
            work.setId(dto.getId());
            Map<String, Object> dataMap = dto.getEntity();
            if (null == dataMap) {
                dataMap = new HashMap<>();
            }
            if (StringUtils.isNotEmpty(dto.getLineId())) {
                Work workSearch = workService.findByWorkId(work.getId());
                if (null != workSearch) {
                    // 查询并拼接执行条件
                    Predicate<Line> predicate = line -> dto.getLineId().equals(line.getId());
                    if (null != workSearch.getLineList()
                            && 1 == workSearch.getLineList().stream().filter(predicate).count()) {
                        Line conditionLine = workSearch.getLineList().stream()
                                .filter(predicate)
                                .findFirst()
                                .get();
                        // 将操作名称存入工作流
                        work.setOperateName(conditionLine.getName());
                        if (StringUtils.isNotEmpty(conditionLine.getCondition())) {
                            String[] array = lineConvertor.condition2Array(conditionLine.getCondition());
                            dataMap.put(array[0], array[1]);
                        }
                    }
                }
            }
            work.setDataMap(dataMap);
            work.setOperateKey(dto.getLineId());
            work.setMemo(dto.getMemo());
            return work;
        }
        return null;
    }
}
