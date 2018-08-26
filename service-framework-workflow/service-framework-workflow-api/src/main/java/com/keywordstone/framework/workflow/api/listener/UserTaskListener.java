package com.keywordstone.framework.workflow.api.listener;

import com.keywordstone.framework.common.queue.rocketmq.client.RocketMqProducer;
import com.keywordstone.framework.workflow.api.module.WorkNotice;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // 工作流消息提醒
        workNotice(delegateTask);
    }

    private void workNotice(DelegateTask delegateTask) {
        Set<String> userSet;
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        if (null == candidates) {
            userSet = new HashSet<>();
        } else {
            userSet = candidates.stream()
                    .map(identityLink -> identityLink.getUserId())
                    .collect(Collectors.toSet());
        }
        String assignee = delegateTask.getAssignee();
        if (StringUtils.isNotEmpty(assignee)) {
            userSet.add(assignee);
        }
        if (0 == userSet.size()) {
            return;
        }
        Object object = delegateTask.getVariable(WorkFlowConst.ID);
        String entityId = object.toString();
        WorkNotice workNotice = new WorkNotice();
        workNotice.setEntityId(entityId);
        workNotice.setReadFlag(0);
        workNotice.setWorkName(delegateTask.getName());
        workNotice.setSendDate(new Date());
        userSet.forEach(userId -> {
            workNotice.setId(UUID.randomUUID().toString().replace("-", ""));
            workNotice.setReceiverId(userId);
            workNotice.setSenderId(delegateTask.getProcessInstanceId());
            RocketMqProducer.send("addNotice","1",workNotice);
        });
    }
}
