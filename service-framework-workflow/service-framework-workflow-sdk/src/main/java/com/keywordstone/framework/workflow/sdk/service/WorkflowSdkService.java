package com.keywordstone.framework.workflow.sdk.service;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.workflow.sdk.dto.*;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-framework-workflow")
public interface WorkflowSdkService {

    @RequestMapping(value = "/design/findAll", method = RequestMethod.GET)
    @ApiOperation("查询所有")
    ResultDTO<List<FlowDTO>> findAll();

    @RequestMapping(value = "/design/findById", method = RequestMethod.GET)
    @ApiOperation("根据主键查询")
    ResultDTO<FlowDTO> findById(@RequestParam("id") String id);

    @RequestMapping(value = "/design/upload", method = RequestMethod.POST)
    @ApiOperation("设定工作流")
    ResultDTO<Void> upload(@RequestBody FlowDTO dto);

    @RequestMapping(value = "/process/generate", method = RequestMethod.POST)
    @ApiOperation("生成")
    ResultDTO<String> generate(@RequestBody @NotNull WorkGenerateDTO dto);

    @RequestMapping(value = "/process/update", method = RequestMethod.POST)
    @ApiOperation("修改（运行）")
    ResultDTO<String> update(@RequestBody @NotNull WorkUpdateDTO dto);

    @RequestMapping(value = "/process/findByEntityId", method = RequestMethod.GET)
    @ApiOperation("根据工作ID查询")
    ResultDTO<WorkEntityDTO> findByEntityId(@RequestParam("entityId") @NotEmpty String entityId);

    @RequestMapping(value = "/process/findByOperator", method = RequestMethod.GET)
    @ApiOperation("根据当前用户ID查询")
    ResultDTO<List<WorkDTO>> findByOperator();

    @RequestMapping(value = "/process/findByOperatorPageable", method = RequestMethod.POST)
    @ApiOperation("根据当前用户ID查询列表")
    ResultDTO<PageResultDTO<WorkDTO>> findByOperatorPageable(@RequestBody Pageable pageable);

    @RequestMapping(value = "/process/findByWorkId", method = RequestMethod.GET)
    @ApiOperation("根据工作ID查询")
    ResultDTO<WorkDTO> findByWorkId(@RequestParam("workId") String workId);

    @RequestMapping(value = "/process/workLog", method = RequestMethod.GET)
    @ApiOperation("工作流操作日志")
    ResultDTO<List<WorkLogDTO>> workLog(@RequestParam("entityId") @NotEmpty String entityId);

    @RequestMapping(value = "/process/workLogPageable", method = RequestMethod.POST)
    @ApiOperation("工作流操作日志列表")
    ResultDTO<PageResultDTO<WorkLogDTO>> workLogPageable(@RequestParam("entityId") @NotEmpty String entityId,
                                                         @RequestBody Pageable pageable);

    @RequestMapping(value = "/process/dateRangeCompletedCount", method = RequestMethod.POST)
    @ApiOperation("个人在时间段内完成任务数")
    ResultDTO<List<Integer>> dateRangeCompletedCount(@RequestBody @NotNull List<DateRangeDTO> dtoList);

    @RequestMapping(value = "/process/dateRangeUncompletedCount", method = RequestMethod.POST)
    @ApiOperation("个人在时间段内未完成任务数")
    ResultDTO<List<Integer>> dateRangeUncompletedCount(@RequestBody @NotNull List<DateRangeDTO> dtoList);

    @RequestMapping(value = "/process/complicatedSearchPageable", method = RequestMethod.POST)
    @ApiOperation("综合查询")
    ResultDTO<PageResultDTO<WorkDTO>> complicatedSearchPageable(@RequestBody WorkConditionDTO dto);

    @RequestMapping(value = "/borrow/contractBorrowApply", method = RequestMethod.POST)
    @ApiOperation("合同借阅申请")
    ResultDTO contractBorrowApply(@RequestBody BorrowContractDTO borrowContractDTO);

    @RequestMapping(value = "/borrow/contractBorrowApprove", method = RequestMethod.POST)
    @ApiOperation("合同借阅审核")
    ResultDTO contractBorrowApprove(@RequestBody BorrowContractDTO borrowContractDTO);

    @RequestMapping(value = "/borrow/searchApplyIds", method = RequestMethod.GET)
    @ApiOperation("查询工作流返回所有自己已申请过的合同ID集合")
    ResultDTO<List<BorrowContractDTO>> searchApplyIds();

    @RequestMapping(value = "/borrow/searchApplyInfos", method = RequestMethod.POST)
    @ApiOperation("查询工作流返回所有本人已经申请的流程信息")
    ResultDTO<PageMapDTO<BorrowContractDTO>> searchApplyInfos(@RequestBody BorrowSearchDTO borrowSearchDTO);

    @RequestMapping(value = "/borrow/searchBorrowApprove", method = RequestMethod.POST)
    @ApiOperation("借阅审批列表")
    ResultDTO<PageMapDTO<Map<String, Object>>> searchBorrowApprove(@RequestBody BorrowSearchDTO borrowSearchDTO);

    @RequestMapping(value = "/design/addUser", method = RequestMethod.POST)
    @ApiOperation("添加用户")
    ResultDTO<String> addUser(@RequestBody AbstractDTO user);

    @RequestMapping(value = "/design/delUser", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    ResultDTO<String> delUser(@RequestBody AbstractDTO user);

}
