package com.keywordstone.framework.workflow.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EndEventListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
    }
}
