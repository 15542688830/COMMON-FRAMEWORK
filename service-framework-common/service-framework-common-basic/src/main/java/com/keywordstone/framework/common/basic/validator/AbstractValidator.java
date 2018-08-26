package com.keywordstone.framework.common.basic.validator;

import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractValidator<DTO> {

    public abstract String validate(DTO dto);

    public ResultDTO validateResultDTO(DTO dto) {
        if (null != dto) {
            String validateResult = validate(dto);
            if (StringUtils.isEmpty(validateResult)) {
                return ResultDTO.success();
            }
            return ResultDTO.failure(ResultError.error(validateResult));
        }
        return ResultDTO.failure();
    }
}
