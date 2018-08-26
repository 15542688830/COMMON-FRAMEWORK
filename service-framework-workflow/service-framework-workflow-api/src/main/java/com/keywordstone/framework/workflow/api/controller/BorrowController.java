package com.keywordstone.framework.workflow.api.controller;

import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.workflow.api.service.BorrowService;
import com.keywordstone.framework.workflow.sdk.dto.BorrowContractDTO;
import com.keywordstone.framework.workflow.sdk.dto.BorrowSearchDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 16:34 2018/4/28
 * @Modified By:
 */
@RestController
@RequestMapping("/borrow")
@Slf4j
@Api(description = "合同借阅")
public class BorrowController {

    @Autowired
    public BorrowService borrowService;

    @Autowired
    public TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @RequestMapping(value = "/contractBorrowApply", method = RequestMethod.POST)
    @ApiOperation("合同借阅申请")
    public ResultDTO contractBorrowApply(@RequestBody BorrowContractDTO borrowContractDTO) {
        borrowService.contractBorrowApply(borrowContractDTO);
        return ResultDTO.success();
    }

    @RequestMapping(value = "/contractBorrowApprove", method = RequestMethod.POST)
    @ApiOperation("合同借阅审核")
    public ResultDTO contractBorrowApprove(@RequestBody BorrowContractDTO borrowContractDTO) {
        borrowService.contractBorrowApprove(borrowContractDTO);
        return ResultDTO.success();
    }

    @RequestMapping(value = "/searchApplyIds", method = RequestMethod.GET)
    @ApiOperation("查询工作流返回所有本人正在申请的合同ID集合")
    public ResultDTO searchApplyIds() {
        return borrowService.searchApplyIds();
    }
    @RequestMapping(value = "/searchApplyInfos", method = RequestMethod.POST)
    @ApiOperation("查询工作流返回所有本人已经申请的流程信息")
    public ResultDTO searchApplyInfos(@RequestBody BorrowSearchDTO borrowSearchDTO) {
        return borrowService.searchApplyInfos(borrowSearchDTO);
    }

    @RequestMapping(value = "/searchBorrowApprove", method = RequestMethod.POST)
    @ApiOperation("借阅审批列表")
    public ResultDTO searchBorrowApprove(@RequestBody BorrowSearchDTO borrowSearchDTO) {
        return borrowService.searchBorrowApprove(borrowSearchDTO);
    }

    @ApiOperation("工作流消息已读")
    @RequestMapping(value = "/searchProcess",method = RequestMethod.GET)
    public List<Map<String,Object>> searchProcess() {
        List<Task> tasks = taskService.createTaskQuery().includeIdentityLinks().list();
        List<Map<String,Object>> list = new ArrayList<>();

        for (Task task : tasks) {
            Map<String, Object> map = new HashMap<>();
            map.put("taskId", task.getId());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("taskName", task.getName());
            map.put("users", task.getIdentityLinks());

            list.add(map);
        }
        return list;
    }
}
