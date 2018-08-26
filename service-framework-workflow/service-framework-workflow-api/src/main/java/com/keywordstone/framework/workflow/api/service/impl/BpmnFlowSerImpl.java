package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.workflow.api.module.Flow;
import com.keywordstone.framework.workflow.api.module.Group;
import com.keywordstone.framework.workflow.api.service.BpmnService;
import com.keywordstone.framework.workflow.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.idm.api.UserQuery;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.keywordstone.framework.workflow.api.utils.WorkFlowConst.INITIATOR;

/**
 * 工作流图形服务
 */
@Service
@Slf4j
public class BpmnFlowSerImpl implements BpmnService<Flow> {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    /**
     * 人员绑定
     * @param flow
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String upload(Flow flow) {
        if (null != flow) {

            if (flow.getGroupList() != null && !flow.getGroupList().isEmpty()) {
                for (Group g : flow.getGroupList()) {
                    updateIdm(g,flow.getDataFlag(),flow.getKey());
                    List<Task> tasks = taskService.createTaskQuery().taskDefinitionKey(g.getKey()).list();

                    if (flow.getDataFlag() != null && flow.getDataFlag() == 1) {
                        for (Task task : tasks) {

                            //如果是最后一个则不让删除
                            if (tasks != null && tasks.size() >= 1 &&
                                    g.getOperatorList() != null && !g.getOperatorList().isEmpty()) {
                                for (String userId : g.getOperatorList()) {
                                    taskService.deleteCandidateUser(task.getId(),userId);
                                }
                            }
                        }

                    } else if(flow.getDataFlag() != null){
                        for (Task task : tasks) {

                            if(g.getOperatorList() != null && !g.getOperatorList().isEmpty()){
                                for (String userId : g.getOperatorList()) {
                                    taskService.addCandidateUser(task.getId(),userId);
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    private void updateIdm(Group g,Integer idDelete,String type) {

        if (idDelete == 1 && g.getOperatorList() != null && !g.getOperatorList().isEmpty()) {
            //解除绑定
            org.flowable.idm.api.Group group = identityService.createGroupQuery().groupType(type).groupName(g.getKey()).singleResult();
            identityService.deleteMembership(g.getOperatorList().get(0),group.getId());

        } else {
            //绑定
            org.flowable.idm.api.Group group = identityService.createGroupQuery().groupType(type).groupName(g.getKey()).singleResult();

            org.flowable.idm.api.Group userGroup;
            //如果没有组则添加组
            if (group == null) {
                userGroup = identityService.newGroup(UUID.randomUUID().toString().replace("-", ""));
                //对应processKey
                userGroup.setType(type);
                //任务taskKey
                userGroup.setName(g.getKey());
                identityService.saveGroup(userGroup);
            } else{
                userGroup = group;
            }
            //创建user然后添加与组的关联关系
            g.getOperatorList().stream().forEach(id ->{

                UserQuery queryuery = identityService.createUserQuery().userId(id);
                //没有用户则添加用户
                if (queryuery.count() == 0) {
                    AbstractDTO abstractDTO = new AbstractDTO() {};
                    abstractDTO.setId(id);
                    userService.addUser(abstractDTO);
                }
                identityService.createMembership(id,userGroup.getId());
            });

        }

    }

    @Override
    public List<Flow> findAll() {
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        if (null == definitionList || definitionList.isEmpty()) {
            return null;
        }
        return definitionList.stream()
                .filter(definition -> null != definition)
                .map(definition -> definition2Flow(definition))
                .collect(Collectors.toList());
    }

    @Override
    public Flow findById(String key) {
        return definition2Flow(repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .latestVersion()
                .singleResult());
    }

    /**
     * 下载指定工作流图形xml文件
     * @param id
     */
    @Override
    public void download(String id) {
        BpmnModel model = repositoryService.getBpmnModel(id);
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] bytes = bpmnXMLConverter.convertToXML(model);
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Flow definition2Flow(ProcessDefinition definition) {
        if (null != definition) {
            Flow flow = new Flow();
            flow.setId(definition.getId());
            flow.setKey(definition.getKey());
            flow.setName(definition.getName());
            BpmnModel model = repositoryService.getBpmnModel(definition.getId());
            List<Group> groupList = new ArrayList<>();
            Collection<UserTask> userTaskCollection = model.getMainProcess().findFlowElementsOfType(UserTask.class);
            if (null != userTaskCollection && !userTaskCollection.isEmpty()) {
                groupList.addAll(userTaskCollection.stream()
                        .filter(userTask -> null != userTask
                                // 已指定执行人为发起者的任务 业务上不允许再绑定
                                && !INITIATOR.equals(userTask.getAssignee()))
                        .map(userTask -> {
                            Group group = new Group();
                            // 指派人也设定为操作者
                            org.flowable.idm.api.Group userGroup = identityService.createGroupQuery()
                                    .groupName(userTask.getId()).groupType(definition.getKey()).singleResult();

                            List<String> operatorSet = null;
                            if (null != userGroup) {
                                operatorSet = identityService.createUserQuery().memberOfGroup(userGroup.getId())
                                        .list().stream().map(user -> user.getId()).collect(Collectors.toList());
                            }
                            if (null != operatorSet && !operatorSet.isEmpty()) {
                                group.setOperatorList(operatorSet);
                            }
                            group.setKey(userTask.getId());
                            group.setName(userTask.getName());
                            return group;
                        })
                        .collect(Collectors.toList()));
            }
            flow.setGroupList(groupList);
            return flow;
        }
        return null;
    }
}
