package com.keywordstone.framework.workflow.api.validator;

import com.keywordstone.framework.common.basic.validator.AbstractValidator;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.api.service.WorkService;
import com.keywordstone.framework.workflow.sdk.dto.WorkGenerateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author k
 */
@Component
public class WorkGenerateValidator extends AbstractValidator<WorkGenerateDTO> {

    @Autowired
    private WorkService workService;

    @Override
    public String validate(WorkGenerateDTO dto) {
        if (null == dto
                || null == dto.getEntity()
                || null == dto.getEntity().get("id")) {
            return "数据异常";
        }
        List<Work> workList = workService.findByEntityId(dto.getEntity().get("id").toString()).getWorkList();
        if (null != workList && workList.size() > 0) {
            return "工作已存在";
        }
        return null;
    }
}
