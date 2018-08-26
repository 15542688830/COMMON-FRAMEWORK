package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.workflow.api.module.Flow;
import com.keywordstone.framework.workflow.api.module.FlowCondition;
import com.keywordstone.framework.workflow.api.service.EngineService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineFlowSerImpl implements EngineService<Flow, FlowCondition> {

    @Override
    public String on(Flow flow) {
        return null;
    }

    @Override
    public String off(Flow flow) {
        return null;
    }

    @Override
    public String operate(Flow flow) {
        return null;
    }

    @Override
    public PageResultDTO<Flow> search(FlowCondition flowCondition,
                                      boolean withSub) {
//        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
//        if (null != flow) {
//            if (StringUtils.isNotEmpty(flow.getId())) {
//                query = query.processDefinitionKey(flow.getId());
//            }
//        }
//        List<ProcessDefinition> definitionList = query.list();
//        if (null != definitionList
//                && !definitionList.isEmpty()) {
//            return definitionList.stream()
//                    .filter(definition -> null != definition)
//                    .map(definition -> definitionToWork(definition))
//                    .collect(Collectors.toList());
//        }
        return null;
    }

    @Override
    public PageResultDTO<Flow> searchHistory(FlowCondition flowCondition,
                                             boolean withSub) {
        return null;
    }

    @Override
    public String generateBase64(Flow... flows) {
        return null;
    }

    @Override
    public Long searchCompleteCount(FlowCondition flowCondition) {
        return null;
    }

    @Override
    public Long searchUnCompleteCount(FlowCondition flowCondition) {
        return null;
    }

    @Override
    public String generateBase64By(String id, List<String> keyList) {
        return null;
    }

    private Flow definitionToWork(ProcessDefinition definition) {
        if (null != definition) {
            Flow flow = new Flow();
            flow.setId(definition.getId());
            flow.setName(definition.getName());
            return flow;
        }
        return null;
    }
}
