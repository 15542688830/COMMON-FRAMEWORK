package com.keywordstone.framework.workflow.api.controller;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.workflow.api.convertor.FlowConvertor;
import com.keywordstone.framework.workflow.api.module.Flow;
import com.keywordstone.framework.workflow.api.service.BpmnService;
import com.keywordstone.framework.workflow.api.service.UserService;
import com.keywordstone.framework.workflow.sdk.dto.FlowDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author k
 */
@RestController
@RequestMapping("/design")
@Slf4j
@Api(description = "设计工作流引擎")
public class DesignController {

    @Autowired
    private FlowConvertor flowConvertor;

    @Autowired
    private BpmnService<Flow> bpmnFlowService;

    @Autowired
    private UserService userService;


//    @RequestMapping(value = "/generate", method = RequestMethod.POST)
//    @ApiOperation("生成")
//    public ResultDTO generate(@RequestBody Flow flow) {
//        return ResultDTO.success(flowService.generate(flow));
//    }
//
//    @RequestMapping(value = "/remove", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResultDTO remove(@RequestParam String id) {
//        return ResultDTO.success(flowService.remove(id));
//    }
//
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @ApiOperation("修改")
//    public ResultDTO update(@RequestBody Flow flow) {
//        return ResultDTO.success(flowService.update(flow));
//    }
//
//    @RequestMapping(value = "/find", method = RequestMethod.GET)
//    @ApiOperation("查询")
//    public ResultDTO find(@RequestParam String id) {
//        return ResultDTO.success(flowService.findById(id));
//    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation("查询所有")
    public ResultDTO findAll() {
        return flowConvertor.toListResultDTO(bpmnFlowService.findAll());
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ApiOperation("根据业务主键查询")
    public ResultDTO findById(@RequestParam("id") String id) {
        return flowConvertor.toResultDTO(bpmnFlowService.findById(id));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation("设定工作流")
    public ResultDTO upload(@RequestBody FlowDTO dto) {
        return ResultDTO.success(bpmnFlowService.upload(flowConvertor.toEntity(dto)));
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ApiOperation("下载工作流")
    public void download(@RequestParam("id") String id) {
        bpmnFlowService.download(id);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ApiOperation("添加用户预留")
    public ResultDTO addUser(@RequestBody AbstractDTO user) {
        return userService.addUser(user);
    }
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    @ApiOperation("删除用户预留")
    public ResultDTO delUser(@RequestBody AbstractDTO user) {
        return userService.delUser(user);
    }
}
