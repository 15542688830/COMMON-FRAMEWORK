package com.keywordstone.framework.workflow.api.listener;

import com.keywordstone.framework.common.queue.rocketmq.client.RocketMqProducer;
import com.keywordstone.framework.workflow.api.module.WorkNotice;
import com.keywordstone.framework.workflow.api.utils.WorkFlowConst;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

import java.util.Date;
import java.util.UUID;

import static com.keywordstone.framework.workflow.api.utils.WorkFlowConst.NOTICE_CONTENT;
import static com.keywordstone.framework.workflow.api.utils.WorkFlowConst.RETURN_STATUS;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 15:00 2018/4/24
 * @Modified By:
 */
public class EndTimerExecutorListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {

        if (delegateExecution.getVariable(RETURN_STATUS) != null && delegateExecution.getVariable(RETURN_STATUS).equals("0")) {

            String receiverId = (String)delegateExecution.getVariable(WorkFlowConst.BORROW_PERSON_ID);
            String entityId = (String)delegateExecution.getVariable(WorkFlowConst.CONTRACT_ID);
            WorkNotice workNotice = new WorkNotice();
            workNotice.setEntityId(entityId);
            workNotice.setReadFlag(0);
            workNotice.setWorkName(NOTICE_CONTENT);
            workNotice.setSendDate(new Date());
            workNotice.setId(UUID.randomUUID().toString().replace("-", ""));
            workNotice.setReceiverId(receiverId);
            workNotice.setSenderId(delegateExecution.getProcessInstanceId());
            RocketMqProducer.send("addNotice","1",workNotice);
        }
    }
}
