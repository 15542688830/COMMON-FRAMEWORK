package com.keywordstone.framework.workflow.api.controller;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.workflow.api.convertor.*;
import com.keywordstone.framework.workflow.api.module.Work;
import com.keywordstone.framework.workflow.api.service.WorkService;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import com.keywordstone.framework.workflow.api.validator.WorkGenerateValidator;
import com.keywordstone.framework.workflow.sdk.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author k
 */
@RestController
@Slf4j
@Api(description = "执行工作流引擎")
@RequestMapping("/process")
@CacheConfig(cacheNames = "workflow_process")
public class ProcessController {

    @Autowired
    private WorkGenerateValidator generateValidator;
    @Autowired
    private WorkGenerateConvertor workGenerateConvertor;
    @Autowired
    private WorkUpdateConvertor workUpdateConvertor;
    @Autowired
    private WorkConvertor workConvertor;
    @Autowired
    private WorkLogConvertor workLogConvertor;
    @Autowired
    private DateRangeConvertor dateRangeConvertor;
    @Autowired
    private WorkConditionConvertor workConditionConvertor;
    @Autowired
    private WorkNoticeConConvertor workNoticeConConvertor;
    @Autowired
    private WorkNoticeConvertor workNoticeConvertor;
    @Autowired
    private WorkEntityConvertor workEntityConvertor;

    @Autowired
    private WorkService workService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ApiOperation("生成")
    public ResultDTO<String> generate(@RequestBody @NotNull WorkGenerateDTO dto) {
        String result = generateValidator.validate(dto);
        if (StringUtils.isNotEmpty(result)) {
            return ResultDTO.failure(ResultError.error(result));
        }
        Work work = workGenerateConvertor.toEntity(dto);
        return ResultDTO.success(workService.generate(work));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ApiOperation("删除")
    public ResultDTO<String> remove(@RequestParam("entityId") @NotEmpty String entityId) {
//        String result = removeValidator.validate(entityId);
//        if (StringUtils.isNotEmpty(result)) {
//            return ResultDTO.failure(ResultError.error(result));
//        }
        return ResultDTO.success(workService.remove(entityId));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("修改（运行）")
    @CacheEvict(key = "#dto.entity.get('id')")
    public ResultDTO<String> update(@RequestBody @NotNull WorkUpdateDTO dto) {
//        String result = updateValidator.validate(dto);
//        if (StringUtils.isNotEmpty(result)) {
//            return ResultDTO.failure(ResultError.error(result));
//        }
        Work work = workUpdateConvertor.toEntity(dto);
        return ResultDTO.success(workService.update(work));
    }

    @RequestMapping(value = "/findByEntityId", method = RequestMethod.GET)
    @ApiOperation("根据工作载体ID查询")
    @Cacheable(key = "#entityId")
    public ResultDTO<WorkEntityDTO> findByEntityId(@RequestParam("entityId") @NotEmpty String entityId) {
        return workEntityConvertor.toResultDTO(workService.findByEntityId(entityId));
    }

    @RequestMapping(value = "/findByOperator", method = RequestMethod.GET)
    @ApiOperation("根据当前用户ID查询")
    public ResultDTO<List<WorkDTO>> findByOperator() {
        if (StringUtils.isEmpty(TokenUtils.getUserId())) {
            return ResultDTO.failure(ResultError.error("用户ID为空"));
        }
        return workConvertor.toListResultDTO(workService.findByOperator());
    }

    @RequestMapping(value = "/findByOperatorPageable", method = RequestMethod.POST)
    @ApiOperation("根据当前用户ID查询列表")
    public ResultDTO<PageResultDTO<WorkDTO>> findByOperatorPageable(@RequestBody Pageable pageable) {
        if (StringUtils.isEmpty(TokenUtils.getUserId())) {
            return ResultDTO.failure(ResultError.error("用户ID为空"));
        }
        return workConvertor.toPageResultDTO(workService.findByOperatorPageable(pageable));
    }

    @RequestMapping(value = "/findByWorkId", method = RequestMethod.GET)
    @ApiOperation("根据工作ID查询")
    public ResultDTO<WorkDTO> findByWorkId(@RequestParam("workId") @NotEmpty String workId) {
        Work work = workService.findByWorkId(workId);
        if (null != work) {
            List<Work> workList = workService.findByEntityId(work.getDataMap().get(WorkFlowConst.ID).toString()).getWorkList();
            work.setImg(workService.generateBase64(workList.toArray(new Work[workList.size()])));
        }
        return workConvertor.toResultDTO(work);
    }

    @RequestMapping(value = "/workLog", method = RequestMethod.GET)
    @ApiOperation("工作流操作日志")
    public ResultDTO<List<WorkLogDTO>> workLog(@RequestParam("entityId") @NotEmpty String entityId) {
        return workLogConvertor.toListResultDTO(workService.findWorkLogByEntityId(entityId));
    }

    @RequestMapping(value = "/workLogPageable", method = RequestMethod.POST)
    @ApiOperation("工作流操作日志列表")
    public ResultDTO<PageResultDTO<WorkLogDTO>> workLogPageable(@RequestParam("entityId") @NotEmpty String entityId,
                                                                @RequestBody Pageable pageable) {
        return workLogConvertor.toPageResultDTO(workService.findWorkLogByEntityIdPageable(entityId, pageable));
    }

    @RequestMapping(value = "/dateRangeCompletedCount", method = RequestMethod.POST)
    @ApiOperation("个人在时间段内完成任务数")
    public ResultDTO<List<Integer>> dateRangeCompletedCount(@RequestBody @NotNull List<DateRangeDTO> dtoList) {
        return ResultDTO.success(workService.dateRangeCompletedCount(dateRangeConvertor.toListEntity(dtoList)));
    }

    @RequestMapping(value = "/dateRangeUncompletedCount", method = RequestMethod.POST)
    @ApiOperation("个人在时间段内未完成任务数")
    public ResultDTO<List<Integer>> dateRangeUncompletedCount(@RequestBody @NotNull List<DateRangeDTO> dtoList) {
        return ResultDTO.success(workService.dateRangeUncompletedCount(dateRangeConvertor.toListEntity(dtoList)));
    }

    @RequestMapping(value = "/complicatedSearchPageable", method = RequestMethod.POST)
    @ApiOperation("综合查询")
    public ResultDTO<PageResultDTO<WorkDTO>> complicatedSearchPageable(@RequestBody WorkConditionDTO dto) {
        return workConvertor.toPageResultDTO(workService.complicatedSearchPageable(workConditionConvertor.toEntity(dto)));
    }
//
//    @RequestMapping(value = "/noticeSearch", method = RequestMethod.POST)
//    @ApiOperation("工作流消息")
//    public ResultDTO<PageResultDTO<WorkNoticeDTO>> noticeSearch(@RequestBody WorkNoticeConDTO dto) {
//        return workNoticeConvertor.toPageResultDTO(workService.noticeSearch(workNoticeConConvertor.toEntity(dto)));
//    }
//
//    @RequestMapping(value = "/noticeRead", method = RequestMethod.POST)
//    @ApiOperation("工作流消息已读")
//    public ResultDTO<Void> noticeRead(@RequestBody List<String> idList) {
//        workService.noticeRead(idList);
//        return ResultDTO.success();
//    }
}
