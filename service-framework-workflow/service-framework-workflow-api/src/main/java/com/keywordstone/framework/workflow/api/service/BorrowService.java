package com.keywordstone.framework.workflow.api.service;

import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.workflow.sdk.dto.BorrowContractDTO;
import com.keywordstone.framework.workflow.sdk.dto.BorrowSearchDTO;

/**
 * @author Zhangrui
 */
public interface BorrowService {

    ResultDTO contractBorrowApply(BorrowContractDTO borrowContractDTO);

    ResultDTO contractBorrowApprove(BorrowContractDTO borrowContractDTO);

    ResultDTO searchApplyIds();

    ResultDTO searchApplyInfos(BorrowSearchDTO borrowSearchDTO);

    ResultDTO searchBorrowApprove(BorrowSearchDTO borrowSearchDTO);
}
