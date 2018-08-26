package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.workflow.api.module.*;
import com.keywordstone.framework.workflow.api.service.EngineService;
import com.keywordstone.framework.workflow.api.service.LogService;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import com.keywordstone.framework.workflow.sdk.enums.LineStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.service.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.history.NativeHistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 工作流引擎服务
 */
@Service
@Slf4j
public class EngineWorkSerImpl implements EngineService<Work, WorkCondition> {

    private final static String COMPLETE = "完成";
    private final static String IMAGE_TYPE = "jpeg";
    private final static String IMAGE_HEAD = "data:image/" + IMAGE_TYPE + ";base64,";

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;

    @Autowired
    private LogService<WorkLog, WorkLogCondition> workLogService;

    /**
     * 发起工作流实例
     *
     * @param work
     * @return
     */
    @Override
    public String on(Work work) {
        if (null != work) {
            // 设定实例发起者
            identityService.setAuthenticatedUserId(TokenUtils.getUserId());
            // 发起
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(work.getFlowId(), work.getDataMap());
            // 注销实例发起者
            identityService.setAuthenticatedUserId(null);
            String definitionId = processInstance.getProcessDefinitionId();

            bindCandiateUser(work.getId(),processInstance.getId());

            work.setFlowId(definitionId);
            // executionId
            work.setInstanceId(processInstance.getId());
            // 保存操作日志
            work2Mongo(work);
            return processInstance.getId();
        }
        return null;

    }

    @Override
    public String off(Work work) {
        return null;
    }

    /**
     * 执行工作流实例
     *
     * @param work
     * @return
     */
    @Override
    public String operate(Work work) {
        String userId = TokenUtils.getUserId();
        // 查询指定任务
        Task task = taskService.createTaskQuery()
                .includeProcessVariables()
                .taskId(work.getId())
                .taskCandidateOrAssigned(userId)
                .singleResult();
        if (null != task) {
            work.setInstanceId(task.getProcessInstanceId());
            // 保存操作日志
            work2Mongo(work);
            // 拼接操作参数
            Map<String, Object> variables = taskService.getVariables(task.getId());
            Map<String, Object> removedVariables = new HashMap<>();
            if (null != work.getDataMap()) {
                for (Map.Entry<String, Object> entry1 : variables.entrySet()) {
                    for (Map.Entry<String, Object> entry2 : work.getDataMap().entrySet()) {
                        if (entry1.getKey().equals(entry2.getValue())
                                && entry1.getValue().equals(entry2.getKey())) {
                            removedVariables.put(entry1.getKey(), entry1.getValue());
                        }
                    }
                }
            }
            Map<String, Object> dataMap = work.getDataMap();
            variables.putAll(dataMap);
            // 完成任务
            taskService.setVariableLocal(task.getId(), WorkFlowConst.OPERATE_NAME, work.getOperateName());
            taskService.complete(task.getId(), variables);
            //绑定候选人
             ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
             //如果下个任务为结束事件则不需要绑定人员
            if (processInstance != null) {
                bindCandiateUser(processInstance.getProcessDefinitionKey(),task.getProcessInstanceId());
            }
            // 删除冗余操作参数（为回滚再执行机制做准备）
            String executionId = task.getProcessInstanceId();
            if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0) {
                runtimeService.removeVariables(executionId, removedVariables.keySet());
            }
            return task.getProcessInstanceId();
        }
        return null;
    }

    /**
     * 给任务绑定候选人
     */
    private void bindCandiateUser(String flowId,String processId) {

        List<Task> tasks = taskService.createTaskQuery().includeIdentityLinks().processInstanceId(processId).list();
        tasks.stream().forEach(t-> {
            Group group = identityService.createGroupQuery().groupType(flowId).groupName(t.getTaskDefinitionKey()).singleResult();
            if (group != null) {
                List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
                if (users != null && !users.isEmpty()) {
                    List<String> indentitys = t.getIdentityLinks().stream().map(i -> i.getUserId()).collect(Collectors.toList());
                    Predicate<User> predicate = u -> !indentitys.contains(u.getId());
                    users.stream().filter(predicate).map(user -> user.getId()).forEach(id ->taskService.addCandidateUser(t.getId(),id));

                }
            }
        });
    }

    /**
     * 查询待完成任务
     *
     * @param workCondition
     * @param withSub
     * @return
     */
    @Override
    public PageResultDTO<Work> search(WorkCondition workCondition,
                                      boolean withSub) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .unfinished()
                .includeProcessVariables()
                .orderByTaskCreateTime().asc();
        return searchTaskInfo(workCondition, query, withSub);
    }

    /**
     * 查询已完成任务
     *
     * @param workCondition
     * @param withSub
     * @return
     */
    @Override
    public PageResultDTO<Work> searchHistory(WorkCondition workCondition,
                                             boolean withSub) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .finished()// 去掉此行的话 查询结果为待完成和已完成的总和
                .includeProcessVariables()
                .orderByTaskCreateTime().desc();
        LineStatusEnum lineStatus = workCondition.getLineStatus();
        if (null != lineStatus) {
            // 如果是拒绝则查询拒绝 如果不是则查非拒绝
            if (LineStatusEnum.DENIED.equals(lineStatus)) {
                query = query.taskVariableValueEquals(WorkFlowConst.OPERATE_NAME, LineStatusEnum.DENIED.getName());
            } else {
                query = query.taskVariableValueNotEquals(WorkFlowConst.OPERATE_NAME, LineStatusEnum.DENIED.getName());
            }
        }
        return searchTaskInfo(workCondition, query, withSub);
    }

    /**
     * 统计未完成或已完成任务
     *
     * @param workCondition
     * @return
     */
    @Override
    public Long searchCompleteCount(WorkCondition workCondition) {

        NativeHistoricTaskInstanceQuery query = historyService.createNativeHistoricTaskInstanceQuery();

        StringBuilder stringBuilder = new StringBuilder("SELECT COUNT(1) FROM ");
        stringBuilder.append(managementService.getTableName(HistoricTaskInstanceEntity.class));
        stringBuilder.append(" T1 LEFT JOIN ");
        stringBuilder.append(managementService.getTableName(HistoricIdentityLinkEntity.class));
        stringBuilder.append(" T2 ON T1.ID_ = T2.TASK_ID_ WHERE T1.REV_ = '2'");

        if (StringUtils.isNotEmpty(workCondition.getOperator())) {
            stringBuilder.append(" AND (T1.ASSIGNEE_ = '");
            stringBuilder.append(workCondition.getOperator());
            stringBuilder.append("'");
            stringBuilder.append(" OR (T2.TYPE_ = 'candidate' AND T2.USER_ID_ = '");
            stringBuilder.append(workCondition.getOperator());
            stringBuilder.append("')");
            stringBuilder.append(")");

        }
        if (null != workCondition.getDateRange()) {

            if (null != workCondition.getDateRange().getStartDate()) {
                stringBuilder.append(" AND TO_DAYS(T1.END_TIME_) >= TO_DAYS('");
                stringBuilder.append(new java.sql.Date(workCondition.getDateRange().getStartDate().getTime()));
                stringBuilder.append("')");
            }
            if (null != workCondition.getDateRange().getEndDate()) {
                stringBuilder.append(" AND TO_DAYS(T1.END_TIME_) <= TO_DAYS('");
                stringBuilder.append(new java.sql.Date(workCondition.getDateRange().getEndDate().getTime()));
                stringBuilder.append("')");
            }
        }
        query.sql(stringBuilder.toString());
        return query.count();
    }

    /**
     * 统计未完成任务
     *
     * @param workCondition
     * @return
     */
    @Override
    public Long searchUnCompleteCount(WorkCondition workCondition) {

        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();

        query.unfinished().orderByTaskCreateTime().desc();

        if (StringUtils.isNotEmpty(workCondition.getOperator())) {
            query.or()
                 .taskAssignee(workCondition.getOperator())
                 .taskCandidateUser(workCondition.getOperator())
                 .endOr();
        }

        if (null != workCondition.getDateRange()) {

            if (null != workCondition.getDateRange().getStartDate()) {
                query.taskCreatedAfter(workCondition.getDateRange().getStartDate());
            }
            if (null != workCondition.getDateRange().getEndDate()) {
                query.taskCreatedBefore(workCondition.getDateRange().getEndDate());
            }
        }
        return query.count();
    }

    /**
     * 按照任务节点key做出流程示意图
     *
     * @param works
     * @return
     */
    @Override
    public String generateBase64(Work... works) {
        if (null != works) {
            if (Arrays.stream(works)
                    .map(work -> work.getFlowId())
                    .distinct()
                    .count() > 1) {
                return null;
            }
            return generateBase64By(works[0].getFlowId(),
                    Arrays.stream(works)
                            .map(work -> work.getOperateKey())
                            .collect(Collectors.toList()));
        }
        return null;
    }

    @Override
    public String generateBase64By(String id,
                                   List<String> keyList) {
        if (StringUtils.isNotEmpty(id)) {
            if (null == keyList) {
                keyList = new ArrayList<>();
            }
            BpmnModel model = repositoryService.getBpmnModel(id);
            InputStream inputStream = new DefaultProcessDiagramGenerator()
                    .generateDiagram(model,
                            IMAGE_TYPE,
                            keyList,
                            new ArrayList<>(),
                            "幼圆",
                            "幼圆",
                            "幼圆",// TODO 字体设定无效
                            null,
                            1);
            try {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();
                String base64 = new BASE64Encoder().encode(bytes);
                if (StringUtils.isNotEmpty(base64)
                        && !base64.startsWith(IMAGE_HEAD)) {
                    base64 = IMAGE_HEAD + base64;
                }
                return base64;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private PageResultDTO<Work> searchTaskInfo(WorkCondition workCondition,
                                               TaskInfoQuery query,
                                               boolean withSub) {
        if (null != workCondition) {
            if (StringUtils.isNotEmpty(workCondition.getId())) {
                query = query.taskId(workCondition.getId());
            }
            if (StringUtils.isNotEmpty(workCondition.getOperator())) {
                query = query
                        .or()
                        .taskAssignee(workCondition.getOperator())
                        .taskCandidateUser(workCondition.getOperator())
                        .endOr();
            }
            if (StringUtils.isNotEmpty(workCondition.getFlowKey())) {
                query = query.processDefinitionKey(workCondition.getFlowKey());
            }
            if (StringUtils.isNotEmpty(workCondition.getEntityId())) {
                query = query.processVariableValueEquals(WorkFlowConst.ID, workCondition.getEntityId());
            }
            if (null != workCondition.getDateRange()) {
                Date startDate = workCondition.getDateRange().getStartDate();
                Date endDate = workCondition.getDateRange().getEndDate();
                if (null != startDate) {
                    query.taskCreatedAfter(startDate);
                }
                if (null != endDate) {
                    query.taskCreatedBefore(endDate);
                }
            }
        }
        long count = query.count();
        if (0 < count) {
            List<TaskInfo> taskList;
            if (null != workCondition.getPageable()
                    && workCondition.getPageable().isPage()) {
                Integer pageNo = workCondition.getPageable().getPageNo();
                Integer pageSize = workCondition.getPageable().getPageSize();
                if (null == pageNo || 1 > pageNo) {
                    pageNo = 1;
                }
                if (null == pageSize || 1 > pageSize) {
                    pageSize = 10;
                }
                taskList = query.listPage((pageNo - 1) * pageSize, pageSize);
            } else {
                taskList = query.list();
            }
            return PageResultDTO.result(count,
                    taskList.stream()
                            .map(task -> task2Work(task, withSub))
                            .collect(Collectors.toList()));
        }
        return null;
    }

    private Work task2Work(TaskInfo task,
                           boolean withSub) {
        Work work = new Work();
        work.setId(task.getId());
        if (StringUtils.isNotEmpty(task.getProcessInstanceId())) {
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            if (null != instance) {
                work.setCreateTime(task.getCreateTime());
                work.setCreateUser(instance.getStartUserId());
            }
        }
        work.setFlowId(task.getProcessDefinitionId());
        work.setDataMap(task.getProcessVariables());
        work.setName(task.getName());
        work.setOperateKey(task.getTaskDefinitionKey());
        work.setInstanceId(task.getProcessInstanceId());
        // withSub标识是否在查询工作的同事也查询下路走线
        if (withSub) {
            BpmnModel model = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            List<SequenceFlow> sequenceFlowList = model.getMainProcess().findFlowElementsOfType(SequenceFlow.class);
            // 当前UserTask的下路线
            final Predicate<SequenceFlow> predicate1 = sequenceFlow ->
                    sequenceFlow.getSourceRef().equals(task.getTaskDefinitionKey());
            // 当前UserTask的下路线 并且该下路线指向会签网关
            final Predicate<SequenceFlow> predicate2 = sequenceFlow ->
                    sequenceFlow.getSourceRef().equals(task.getTaskDefinitionKey())
                            && sequenceFlow.getTargetFlowElement().getClass().equals(ExclusiveGateway.class);
            SequenceFlow nextSequenceFlow;
            List<Line> lineList = null;
            if (1 == sequenceFlowList.stream().filter(predicate2).count()) {
                // 有条件网关时返回网关下路线
                nextSequenceFlow = sequenceFlowList.stream()
                        .filter(predicate2)
                        .findFirst()
                        .get();
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) nextSequenceFlow.getTargetFlowElement();
                lineList = exclusiveGateway.getOutgoingFlows().stream()
                        .map(this::sequenceFlow2Line)
                        .collect(Collectors.toList());
            } else {
                // 无网关时直接返回当前任务下路线
                if (1 == sequenceFlowList.stream()
                        .filter(predicate1).count()) {
                    nextSequenceFlow = sequenceFlowList.stream()
                            .filter(predicate1)
                            .findFirst()
                            .get();
                    // 如果当前任务下路线没有名字 则默认返回名字为“完成”;
                    if (StringUtils.isEmpty(nextSequenceFlow.getName())) {
                        nextSequenceFlow.setName(COMPLETE);
                    }
                    lineList = Arrays.asList(sequenceFlow2Line(nextSequenceFlow));
                } else {

                    Line line = new Line();
                    line.setName(COMPLETE);
                    lineList = Arrays.asList(line);
                }
            }
            work.setLineList(lineList);
        }
        return work;
    }

    private Line sequenceFlow2Line(SequenceFlow sequenceFlow) {
        if (null != sequenceFlow) {
            Line line = new Line();
            line.setId(sequenceFlow.getId());
            line.setName(sequenceFlow.getName());
            line.setCondition(sequenceFlow.getConditionExpression());
            return line;
        }
        return null;
    }

    void work2Mongo(Work work) {
        WorkLog workLog = work2Log(work);
        workLogService.insert(workLog);
    }

    private WorkLog work2Log(Work work) {
        if (null != work) {
            WorkLog workLog = new WorkLog();
            workLog.setId(UUID.randomUUID().toString());
            Map<String, Object> map = work.getDataMap();
            workLog.setEntityId((String) map.get(WorkFlowConst.ID));
            workLog.setOperatorId(TokenUtils.getUserId());
            workLog.setOperateTime(new Date());
            workLog.setMemo(work.getMemo());
            workLog.setVariableId(work.getInstanceId());
            // 查询
            String lineId = work.getOperateKey();
            WorkCondition workCondition = new WorkCondition();
            workCondition.setId(work.getId());
            PageResultDTO<Work> pageResultDTO = search(workCondition, true);
            //TODO 没有下路线时的历史记录问题
            if (null != pageResultDTO && pageResultDTO.getResult() != null &&
                    !pageResultDTO.getResult().isEmpty()) {

                work = pageResultDTO.getResult().get(0);
                workLog.setWorkKey(work.getId());
                workLog.setWorkName(work.getName());
                List<Line> lineList = work.getLineList();
                if (StringUtils.isNotEmpty(lineId) && null != lineList
                        && !lineList.isEmpty() && !lineId.equals("null")) {
                    Line line = lineList.stream()
                            .filter(l -> null != l && lineId.equals(l.getId()))
                            .findFirst()
                            .get();
                    workLog.setLineKey(lineId);
                    workLog.setLineName(line.getName());
                } else {
                    workLog.setLineName(lineList.get(0).getName());
                }
            }
            if (StringUtils.isEmpty(workLog.getWorkName())) {
                // 此时为StartEvent
                workLog.setWorkName(repositoryService
                        .getBpmnModel(work.getFlowId())
                        .getMainProcess()
                        .findFlowElementsOfType(StartEvent.class)
                        .get(0)
                        .getName());
                workLog.setLineName("发起");
            }
            return workLog;
        }
        return null;
    }
}
