package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.workflow.api.config.BorrowPropertiesConfig;
import com.keywordstone.framework.workflow.api.service.BorrowService;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import com.keywordstone.framework.workflow.sdk.dto.BorrowContractDTO;
import com.keywordstone.framework.workflow.sdk.dto.BorrowSearchDTO;
import com.keywordstone.framework.workflow.sdk.dto.PageMapDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;

import static com.keywordstone.framework.workflow.api.utils.WorkFlowConst.*;

/**
 * 合同借阅服务
 *
 * @author Zhangrui
 */
@Service
@Slf4j
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BorrowPropertiesConfig borrowPropertiesConfig;

    //region 合同借阅申请
    /**
     * @Author: ZhangRui
     * @param: borrowContractDTO
     * @Description: 合同借阅申请
     * @date: Created in 15:11 2018/4/28
     */
    //endregion
    @Override
    public ResultDTO contractBorrowApply(BorrowContractDTO borrowContractDTO) {

        if (StringUtils.isNotEmpty(borrowContractDTO.getBorrowPersonId())) {

            ProcessDefinition processDefinition = getProcessDefinition();

            Map<String, Object> variables = new HashMap<>();
            //存入版本信息为后期工作流变更且保留历史数据时做准备
            borrowContractDTO.setVersion(String.valueOf(borrowPropertiesConfig.getVersion()));

            toMap(borrowContractDTO, variables,false);
            // 设定实例发起者
            identityService.setAuthenticatedUserId(borrowContractDTO.getBorrowPersonId());
            //发起流程
            runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
            identityService.setAuthenticatedUserId(null);
            return ResultDTO.success();
        } else {
            return ResultDTO.failure(ResultError.error("申请人信息获取失败！"));
        }
    }

    //region 合同借阅审核|归还
    /**
     * @Author: ZhangRui
     * @param: approveContractDTO
     * @Description: 合同借阅审核|归还
     * @date: Created in 16:33 2018/4/28
     */
    //endregion
    @Override
    public ResultDTO contractBorrowApprove(BorrowContractDTO borrowContractDTO) {

        Map<String, Object> variables = null;
        TaskInfo task = null;
        if (StringUtils.isNotEmpty(borrowContractDTO.getTaskId())) {
            task = taskService.createTaskQuery()
                    .taskId(borrowContractDTO.getTaskId())
                    .singleResult();
            variables = taskService.getVariables(borrowContractDTO.getTaskId());
            if (StringUtils.isNotEmpty((String) variables.get(EXPECT_DATE))) {
                SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                try {
                    Date expectDate = format.parse((String)variables.get(EXPECT_DATE));
                    Instant now = Instant.now();
                    Instant expectInstant = expectDate.toInstant();
                    Instant dueInstant = expectInstant.minus(DURATION_DAY, ChronoUnit.DAYS);
                    Duration duration = Duration.between(now, dueInstant);
                    borrowContractDTO.setDueDate(duration.toString().replace("-",""));

            } catch (ParseException e) {
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } else {
//            ProcessDefinition processDefinition = getProcessDefinition();
            task = taskService.createTaskQuery()
//                    .processDefinitionId(processDefinition.getId())
                    .includeProcessVariables()
                    .processVariableValueEquals(CONTRACT_ID,borrowContractDTO.getContractId())
                    .singleResult();
            if (task != null) {
                variables = taskService.getVariables(task.getId());
            }
        }

        toMap(borrowContractDTO, variables,true);

        if (task != null) {
            taskService.complete(task.getId(), variables);
            return ResultDTO.success(task.getProcessInstanceId());
        }

        return ResultDTO.failure();
    }

    //region 查询工作流返回所有本人正在申请的合同ID集合
    /**
     * @Author: ZhangRui
     * @param: No such property: code for class: Script1
     * @Description: 查询工作流返回所有本人正在申请的合同ID集合
     * @date: Created in 19:30 2018/5/8
     */
    //endregion
    @Override
    public ResultDTO searchApplyIds() {

//        ProcessDefinition processDefinition =  getProcessDefinition();
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
//                .processDefinitionId(processDefinition.getId())
                .includeProcessVariables()
                .processVariableValueEquals(BORROW_PERSON_ID, TokenUtils.getUserId())
                //未归还的
                .processVariableValueEquals(RETURN_STATUS,"0");

        TaskInfoQuery taskInfoQuery = taskQuery;
        List<TaskInfo> tasks = taskInfoQuery.list();

        return ResultDTO.success(taskToDto(tasks));
    }

    //region 查询工作流返回所有本人已经申请的流程信息
    /**
     * @Author: ZhangRui
     * @param: No such property: code for class: Script1
     * @Description: 查询工作流返回所有本人已经申请的流程信息
     * @date: Created in 19:30 2018/5/8
     */
    //endregion
    @Override
    public ResultDTO searchApplyInfos(BorrowSearchDTO borrowSearchDTO) {

//        ProcessDefinition processDefinition =  getProcessDefinition();
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
//                .processDefinitionId(processDefinition.getId())
                .includeProcessVariables()
                .taskDefinitionKey(WorkFlowConst.SID_APPROVE)
                .processVariableValueEquals(BORROW_PERSON_ID, TokenUtils.getUserId());

        PageMapDTO<BorrowContractDTO> pageMapDTO = new PageMapDTO<>();

        TaskInfoQuery taskInfoQuery = taskQuery;
        List<TaskInfo> tasks = taskInfoQuery.listPage((borrowSearchDTO.getPageNumber() - 1) * borrowSearchDTO.getPageSize()
                , borrowSearchDTO.getPageSize());

        pageMapDTO.setTotal(taskInfoQuery.count());
        pageMapDTO.setRows(taskToDto(tasks));

        return ResultDTO.success(pageMapDTO);
    }

    //region 借阅审批列表
    /**
     * @Author: ZhangRui
     * @param: searchApproveDTO
     * @Description: 借阅审批列表
     * @date: Created in 19:35 2018/5/8
     */
    //endregion
    @Override
    public ResultDTO searchBorrowApprove(BorrowSearchDTO borrowSearchDTO) {

//        ProcessDefinition processDefinition =  getProcessDefinition();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
//            .processDefinitionId(processDefinition.getId())
            .includeProcessVariables();
        if (borrowSearchDTO.isFlag()) {
            query.unfinished();
            query.taskDefinitionKey(WorkFlowConst.SID_APPROVE);
        } else {
            query.taskDefinitionKey(WorkFlowConst.SID_RETURN);
            query.or();
            query.processVariableValueEquals(TASK_REVIEW, "0");
            query.processVariableValueEquals(TASK_REVIEW, "1");
            query.endOr();
        }

        if (borrowSearchDTO.getContractIds() != null && borrowSearchDTO.getContractIds().size() > 0) {
            query.or();
            for (String id : borrowSearchDTO.getContractIds()) {
                query.processVariableValueEquals(CONTRACT_ID,id);
            }
            query.endOr();
        } else if (borrowSearchDTO.getContractIds() != null && borrowSearchDTO.getContractIds().size() == 0) {
            return ResultDTO.success();
        }
        query = query.orderByTaskCreateTime().desc();
        TaskInfoQuery taskInfoQuery = query;
        Integer pageNo = borrowSearchDTO.getPageNumber();
        Integer pageSize = borrowSearchDTO.getPageSize();

        PageMapDTO<Map<String, Object>> pageMapDTO = new PageMapDTO<>();
        pageMapDTO.setTotal(query.count());

        List<TaskInfo> tasks = taskInfoQuery.listPage((pageNo - 1) * pageSize, pageSize);
        Predicate<TaskInfo> predicate = (task) -> task.getProcessVariables() != null;
        List<Map<String, Object>> list = new ArrayList<>();
        tasks.stream().filter(predicate)
                .forEach(task -> {
                    Map<String, Object> variable = task.getProcessVariables();
                    variable.put(TASK_ID, task.getId());
                    list.add(variable);
                });

        pageMapDTO.setRows(list);

        return ResultDTO.success(pageMapDTO);
    }

    private void toDto(Object obj,Map<String, Object> variables) {

            Class<?> clazz = obj.getClass();
            try {
                // 获取类属性
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
                for(PropertyDescriptor propertyDescriptor :propertyDescriptors){
                    try {
                        //将map值赋值给DTO
                        if (variables.get(propertyDescriptor.getName()) != null) {
                            propertyDescriptor.getWriteMethod().invoke(obj, variables.get(propertyDescriptor.getName()));
                        }
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
    }

    private void toMap(Object obj,Map<String,Object> variables,boolean flag) {

        Class<?> clazz = obj.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (!flag || value != null) {
                    variables.put(fieldName, value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private List<BorrowContractDTO> taskToDto(List<TaskInfo> tasks) {

        List<BorrowContractDTO> applyInfos = new ArrayList<>();

        if (tasks != null && tasks.size() > 0) {
            for (TaskInfo task : tasks) {
                BorrowContractDTO borrowContractDTO = new BorrowContractDTO();
                Map<String, Object> variables = task.getProcessVariables();
                variables.put(TASK_ID, task.getId());
                toDto(borrowContractDTO,variables);
                applyInfos.add(borrowContractDTO);
            }
        }
        return applyInfos;
    }

    private ProcessDefinition getProcessDefinition() {

        //获取流程定义
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(WorkFlowConst.CONTRACT_BORROW_KEY)
                .latestVersion()
                .singleResult();
    }
}