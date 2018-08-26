package com.keywordstone.framework.workflow.api.convertor;

import com.keywordstone.framework.common.basic.convertor.AbstractConvertor;
import com.keywordstone.framework.workflow.api.module.Line;
import com.keywordstone.framework.workflow.sdk.dto.KeyValueDTO;
import com.keywordstone.framework.workflow.sdk.dto.LineDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 工作流走线转换
 */
@Component
public class LineConvertor extends AbstractConvertor<LineDTO, Line> {

    private final static String CONDITION_HEAD = "${";
    private final static String CONDITION_END = "}";
    private final static String CONDITION_EQUALS = "==";

    @Override
    public LineDTO toDTO(Line line) {
        if (null != line) {
            LineDTO dto = new LineDTO();
            dto.setId(line.getId());
            dto.setName(line.getName());
            String[] conditionArray = condition2Array(line.getCondition());
            if (null != conditionArray) {
                dto.setCondition(new KeyValueDTO(conditionArray[0], conditionArray[1]));
            }
            return dto;
        }
        return null;
    }

    @Override
    public Line toEntity(LineDTO dto) {
        return null;
    }

    /**
     * 表达式到数组转换
     * @param condition
     * @return
     */
    public String[] condition2Array(String condition) {
        if (StringUtils.isNotEmpty(condition)
                && condition.startsWith(CONDITION_HEAD)
                && condition.endsWith(CONDITION_END)) {
            condition = condition.substring(CONDITION_HEAD.length(), condition.length() - CONDITION_END.length());
            if (StringUtils.isNotEmpty(condition)) {
                if (condition.contains(CONDITION_EQUALS)) {
                    String[] conditionArray = condition.split(CONDITION_EQUALS);
                    if (2 == conditionArray.length) {
                        conditionArray[1] = conditionArray[1].replace("'", StringUtils.EMPTY);
                        return conditionArray;
                    }
                }
            }
        }
        return null;
    }
}
