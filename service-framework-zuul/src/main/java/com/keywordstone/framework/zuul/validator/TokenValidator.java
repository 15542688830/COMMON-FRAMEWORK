package com.keywordstone.framework.zuul.validator;

import com.keywordstone.framework.common.basic.validator.AbstractValidator;
import com.keywordstone.framework.zuul.dto.TokenDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author k
 */
@Component
public class TokenValidator extends AbstractValidator<TokenDTO> {

    @Override
    public String validate(TokenDTO dto) {
        if (null == dto || StringUtils.isEmpty(dto.getId())) {
            return "无效token";
        }
        return null;
    }
}
