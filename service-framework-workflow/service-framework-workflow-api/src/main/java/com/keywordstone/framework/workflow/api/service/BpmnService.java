package com.keywordstone.framework.workflow.api.service;

import java.util.List;

public interface BpmnService<BPMN> {

    String upload(BPMN bpmn);

    List<BPMN> findAll();

    BPMN findById(String id);

    void download(String id);
}
