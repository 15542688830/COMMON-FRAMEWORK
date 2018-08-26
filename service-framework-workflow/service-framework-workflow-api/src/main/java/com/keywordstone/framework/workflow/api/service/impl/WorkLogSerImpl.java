package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.workflow.api.module.WorkLog;
import com.keywordstone.framework.workflow.api.module.WorkLogCondition;
import com.keywordstone.framework.workflow.api.service.LogService;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class WorkLogSerImpl implements LogService<WorkLog, WorkLogCondition> {

    private final static String LIKE_FIX = "%";

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    @Override
    public boolean insert(WorkLog workLog) {
        if (null != workLog
                && StringUtils.isNotEmpty(workLog.getId())
                && StringUtils.isNotEmpty(workLog.getEntityId())) {
//            return Mongo.insert(WORK_LOG_TABLE, workLog);
            String variableName = WorkFlowConst.WORK_FLOW_LOG_TABLE + WorkFlowConst.FIX + workLog.getEntityId();
            List<WorkLog> workLogList = runtimeService.getVariable(workLog.getVariableId(), variableName, List.class);
            if (null == workLogList) {
                List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                        .variableValueEquals(WorkFlowConst.ID, workLog.getEntityId())
                        .list();
                if (null == processInstanceList) {
                    return false;
                }
                workLogList = new ArrayList<>();
            }
            workLogList.add(workLog);
            runtimeService.setVariable(workLog.getVariableId(), variableName, workLogList);
            return true;
        }
        return false;
    }

    @Override
    public PageResultDTO<WorkLog> findBy(WorkLogCondition condition,
                                         Pageable pageable) {
        if (null != condition) {
//            List<Bson> bsonList = new ArrayList<>();
//            String entityId = workLogCondition.getEntityId();
//            if (StringUtils.isNotEmpty(entityId)) {
//                bsonList.add(Filters.eq(WORK_LOG_TABLE_ENTITY_ID, entityId));
//            }
//            String lineNameEquals = workLogCondition.getLineNameEquals();
//            if (StringUtils.isNotEmpty(lineNameEquals)) {
//                bsonList.add(Filters.eq(WORK_LOG_TABLE_lINE_NAME, lineNameEquals));
//            }
//            String lineNameNotEquals = workLogCondition.getLineNameNotEquals();
//            if (StringUtils.isNotEmpty(lineNameNotEquals)) {
//                bsonList.add(Filters.ne(WORK_LOG_TABLE_lINE_NAME, lineNameNotEquals));
//            }
//            Bson[] bsons = bsonList.toArray(new Bson[bsonList.size()]);
//            return Mongo.findPageableBy(
//                    WORK_LOG_TABLE,
//                    Filters.and(bsons),
//                    WorkLog.class,
//                    pageable);
            List<WorkLog> workLogList = logFromHistory(condition);
            if (null != workLogList) {
                Predicate<WorkLog> nullPredicate = workLog -> null != workLog;
                Predicate<WorkLog> lineNameEqualsPredicate = workLog -> {
                    if (StringUtils.isNotEmpty(condition.getLineNameEquals())) {
                        return condition.getLineNameEquals().equals(workLog.getLineName());
                    }
                    return true;
                };
                Predicate<WorkLog> lineNameNotEqualsPredicate = workLog -> {
                    if (StringUtils.isNotEmpty(condition.getLineNameNotEquals())) {
                        return !condition.getLineNameNotEquals().equals(workLog.getLineName());
                    }
                    return true;
                };
                workLogList = workLogList.stream()
                        .filter(nullPredicate)
                        .filter(lineNameEqualsPredicate)
                        .filter(lineNameNotEqualsPredicate)
                        .sorted(Comparator.comparing(WorkLog::getOperateTime).reversed())
                        .collect(Collectors.toList());
                if (null != workLogList) {
                    if (null == pageable) {
                        return PageResultDTO.result(workLogList.size(), workLogList);
                    }
                    if (null != workLogList) {
                        int pageNo = pageable.getPageNo();
                        int pageSize = pageable.getPageSize();
                        if (pageNo < 1) {
                            pageNo = 1;
                        }
                        if (pageSize < 1) {
                            pageSize = 10;
                        }
                        int top;
                        int bottom;
                        int count = workLogList.size();
                        if (count < pageNo * pageSize) {
                            top = count - count % pageSize;
                            bottom = count;
                        } else {
                            top = (pageNo - 1) * pageSize;
                            bottom = pageNo * pageSize;
                        }
                        return PageResultDTO.result(workLogList.size(),
                                workLogList.subList(top, bottom));
                    }
                }
            }
        }
        return PageResultDTO.empty();
    }

    @Override
    public List<WorkLog> findAllBy(WorkLogCondition workLogCondition) {
        PageResultDTO<WorkLog> pageResultDTO = findBy(workLogCondition, null);
        if (null != pageResultDTO) {
            return pageResultDTO.getResult();
        }
        return null;
    }

    private List<WorkLog> logFromHistory(WorkLogCondition condition) {
        String entityId = condition.getEntityId();
        if (StringUtils.isEmpty(entityId)) {
            entityId = StringUtils.EMPTY;
        }
        String variableNameLike = WorkFlowConst.WORK_FLOW_LOG_TABLE + WorkFlowConst.FIX + entityId + LIKE_FIX;
        List<HistoricVariableInstance> instanceList = historyService.createHistoricVariableInstanceQuery()
                .variableNameLike(variableNameLike)
                .list();
        if (null != instanceList) {
            List<WorkLog> workLogList = new ArrayList<>();
            for (HistoricVariableInstance instance : instanceList) {
                workLogList.addAll((List<WorkLog>) instance.getValue());
            }
            return workLogList;
        }
        return null;
    }
}
