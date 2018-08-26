package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class KeyValueDTO extends AbstractDTO {

    @ApiParam("键")
    private String key;

    @ApiParam("值")
    private String value;

    public KeyValueDTO() {
    }

    public KeyValueDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
