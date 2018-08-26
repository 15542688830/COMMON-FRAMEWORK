package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.workflow.api.module.*;
import com.keywordstone.framework.workflow.api.service.EngineService;
import com.keywordstone.framework.workflow.api.service.LogService;
import com.keywordstone.framework.workflow.api.service.WorkService;
import com.keywordstone.framework.workflow.sdk.enums.WorkStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作流服务
 *
 * @author k
 */
@Service
@Slf4j
public class WorkSerImpl implements WorkService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private EngineService<Work, WorkCondition> engineService;
    @Autowired
    private LogService<WorkLog, WorkLogCondition> workLogService;

    @Override
    public String generate(Work work) {
        return engineService.on(work);
    }

    @Override
    public String remove(String entityId) {
        return null;
    }

    @Override
    public String update(Work work) {
        return engineService.operate(work);
    }

    @Override
    public WorkEntity findByEntityId(String entityId) {
        WorkEntity workEntity = new WorkEntity();
        PageResultDTO<Work> pageResultDTO = findByEntityIdPageable(entityId, null);
        if (null != pageResultDTO) {
            workEntity.setWorkList(pageResultDTO.getResult());
        }
        WorkCondition workCondition = new WorkCondition();
        workCondition.setEntityId(entityId);
        workCondition.setPageable(new Pageable().createBuilder().pageNo(1).pageSize(1).build());
        PageResultDTO<Work> pageResultDTO1 = engineService.searchHistory(workCondition, false);
        if (null != pageResultDTO1 && null != pageResultDTO1.getResult() && !pageResultDTO1.getResult().isEmpty()) {
            String flowId = pageResultDTO1.getResult().get(0).getFlowId();
            workEntity.setBase64(engineService.generateBase64By(flowId, null));
        } else {
            PageResultDTO<Work> pageResultDTO2 = engineService.search(workCondition, false);
            if (null != pageResultDTO2 && null != pageResultDTO2.getResult() && !pageResultDTO2.getResult().isEmpty()) {
                String flowId = pageResultDTO2.getResult().get(0).getFlowId();
                workEntity.setBase64(engineService.generateBase64By(flowId, null));
            }
        }
        return workEntity;
    }

    @Override
    public List<Work> findByOperator() {
        PageResultDTO<Work> pageResultDTO = findByOperatorPageable(null);
        if (null != pageResultDTO) {
            return pageResultDTO.getResult();
        }
        return null;
    }

    @Override
    public Work findByWorkId(String workId) {
        WorkCondition workCondition = new WorkCondition();
        workCondition.setId(workId);
        PageResultDTO<Work> pageResultDTO = engineService.search(workCondition, true);
        if (null != pageResultDTO) {
            List<Work> workList = pageResultDTO.getResult();
            if (null != workList && workList.size() > 0) {
                return workList.get(0);
            }
        }
        return null;
    }

    @Override
    public List<WorkLog> findWorkLogByEntityId(String entityId) {
        PageResultDTO<WorkLog> pageResultDTO = findWorkLogByEntityIdPageable(entityId, null);
        if (null != pageResultDTO) {
            return pageResultDTO.getResult();
        }
        return null;
    }

    @Override
    public PageResultDTO<Work> findByEntityIdPageable(String entityId,
                                                      Pageable pageable) {
        if (StringUtils.isNotEmpty(entityId)) {
            WorkCondition workCondition = new WorkCondition();
            workCondition.setEntityId(entityId);
            workCondition.setPageable(pageable);
            return engineService.search(workCondition, false);
        }
        return null;
    }

    @Override
    public PageResultDTO<Work> findByOperatorPageable(Pageable pageable) {
        WorkCondition workCondition = new WorkCondition();
        workCondition.setOperator(TokenUtils.getUserId());
        workCondition.setPageable(pageable);
        return engineService.search(workCondition, false);
    }

    @Override
    public PageResultDTO<WorkLog> findWorkLogByEntityIdPageable(String entityId,
                                                                Pageable pageable) {
        if (StringUtils.isNotEmpty(entityId)) {
            WorkLogCondition workLogCondition = new WorkLogCondition();
            workLogCondition.setEntityId(entityId);
            return workLogService.findBy(workLogCondition, pageable);
        }
        return null;
    }

    @Override
    public List<Integer> dateRangeCompletedCount(List<DateRange> dateRanges) {
        String userId = TokenUtils.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            WorkCondition workCondition = new WorkCondition();
            workCondition.setOperator(userId);
            return dateRanges.stream()
                    .filter(dateRange -> null != dateRange)
                    .map(dateRange -> {
                        workCondition.setDateRange(dateRange);
                        return engineService.searchCompleteCount(workCondition).intValue();
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Integer> dateRangeUncompletedCount(List<DateRange> dateRanges) {
        String userId = TokenUtils.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            WorkCondition workCondition = new WorkCondition();
            workCondition.setOperator(userId);
            return dateRanges.stream()
                    .filter(dateRange -> null != dateRange)
                    .map(dateRange -> {
                        workCondition.setDateRange(dateRange);
                        return engineService.searchUnCompleteCount(workCondition).intValue();
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public PageResultDTO<Work> complicatedSearchPageable(WorkCondition condition) {
        if (null != condition
                && null != condition.getWorkStatus()) {
            WorkStatusEnum workStatus = condition.getWorkStatus();
            if (WorkStatusEnum.COMPLETED.equals(workStatus)) {
                return engineService.searchHistory(condition, false);
            }
            if (WorkStatusEnum.UNCOMPLETED.equals(workStatus)) {
                return engineService.search(condition, false);
            }
        }
        return null;
    }

    @Override
    public String generateBase64(Work... works) {
        return engineService.generateBase64(works);
    }

}