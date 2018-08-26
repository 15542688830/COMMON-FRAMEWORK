package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 9:02 2018/5/9
 * @Modified By:
 */
@Data
public class PageMapDTO<T> extends AbstractDTO{

    private long total;

    private List<T> rows;
}
