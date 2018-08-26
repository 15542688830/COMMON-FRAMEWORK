package com.keywordstone.framework.zuul.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

@Data
public class TokenDTO extends AbstractDTO {

    private String ip;

    public TokenDTO(String token) {
        super(token);
    }
}
