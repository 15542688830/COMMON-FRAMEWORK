package com.keywordstone.framework.workflow.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangRui
 * @Description: 查询参数实体类
 * @date: Created in 16:39 2018/5/8
 * @Modified By:
 */
@Data
public class BorrowSearchDTO extends AbstractDTO {

    //待审核或已审核 true 待审核 false 已审核
    private boolean flag;
    //页数
    private Integer pageNumber;
    //每页条数
    private Integer pageSize;
    //条件查询过滤(用处：任务合同主键与查询条件主键的交集时使用)
    private List<String> contractIds;
}