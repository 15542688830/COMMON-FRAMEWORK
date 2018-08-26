//package com.keywordstone.framework.workflow.api.service.impl;
//
//import com.keywordstone.framework.workflow.api.service.EngineService;
//import com.keywordstone.framework.workflow.api.service.FlowService;
//import Flow;
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.bpmn.converter.BpmnXMLConverter;
//import org.flowable.bpmn.model.*;
//import org.flowable.bpmn.model.Process;
//import org.flowable.validation.ValidationError;
//import org.flowable.validation.validator.impl.BpmnModelValidator;
//import org.hibernate.validator.constraints.NotEmpty;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
///**
// * @author k
// */
//@Service
//@Slf4j
//public class FlowSerImpl implements FlowService {
//
//    @Autowired
//    private EngineService<Flow> engineService;
//
//    /**
//     * 引擎设计
//     *
//     * @param flow
//     */
//    @Override
//    public Flow generate(Flow flow) {
//
//        // 设定工作流层级及主键
//        List<List<String>> idCollection = designBasic(flow);
//        // 设计流程图模型
//        BpmnModel bpmnModel = designBpmnModel(idCollection, flow);
//        List<ValidationError> errors = new ArrayList<>();
//        BpmnModelValidator bpmnModelValidator = new BpmnModelValidator();
//        bpmnModelValidator.validate(bpmnModel, errors);
//        if (errors.size() > 0) {
//            errors.forEach(error -> log.error(error.getProblem(), error));
//        }
//        printBpmnModel(bpmnModel);
//        return flow;
//    }
//
//    @Override
//    public String remove(@NotEmpty String id) {
//        return null;
//    }
//
//    @Override
//    public String update(Flow flow) {
//        return null;
//    }
//
//    @Override
//    public Flow findById(@NotEmpty String id) {
//        Flow flow = new Flow();
//        flow.setId(id);
//        List<Flow> flowList = find(flow);
//        if (null != flowList && !flowList.isEmpty()) {
//            return flowList.get(0);
//        }
//        return null;
//    }
//
//    @Override
//    public List<Flow> find(Flow flow) {
//        return engineService.search(flow);
//    }
//
//    private void printBpmnModel(BpmnModel bpmnModel) {
//        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
//        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
//        String bytes = new String(convertToXML);
//        System.out.println(bytes);
//    }
//
//    private BpmnModel designBpmnModel(List<List<String>> idCollection,
//                                      Flow flow) {
//        if (null != flow
//                && null != flow.getLayerList()) {
//            // 导向流
//            List<List<SequenceFlow>> sequenceFlowCollection = designSequenceFlowCollection(idCollection, flow);
//            // 工作层
//            List<FlowElement> flowElementList = designFlow(idCollection, flow, sequenceFlowCollection);
//            // 进程
//            Process process = new Process();
//            process.setId(flow.getId());
//            process.setName(flow.getName());
//            flowElementList.forEach(flowElement -> process.addFlowElement(flowElement));
//            // 模型
//            BpmnModel bpmnModel = new BpmnModel();
//            bpmnModel.addProcess(process);
//            return bpmnModel;
//        }
//        return null;
//    }
//
//    private List<FlowElement> designFlow(List<List<String>> idCollection,
//                                         Flow flow,
//                                         List<List<SequenceFlow>> sequenceFlowCollection) {
//        List<FlowElement> flowElementList = new ArrayList<>();
//        ParallelGateway parallelGateway;
//        UserTask userTask;
//        for (int i = 0; i < idCollection.size(); i++) {
//            if (i == 0) {
//                // 开始事件
//                StartEvent startEvent = designStartEvent(
//                        idCollection.get(i).get(0),
//                        flow.getName() + " START",
//                        sequenceFlowCollection.get(0));
//                flowElementList.add(startEvent);
//            } else if (i == idCollection.size() - 1) {
//                // 结束事件
//                EndEvent endEvent = designEndEvent(
//                        idCollection.get(i).get(0),
//                        flow.getName() + " END",
//                        sequenceFlowCollection.get(sequenceFlowCollection.size() - 1));
//                flowElementList.add(endEvent);
//            } else if (i % 4 == 2 && i != flow.getLayerList().size() * 4 + 2) {
//                // 网关
//                parallelGateway = designParallelGateway(
//                        idCollection.get(i).get(0),
//                        flow.getLayerList().get((i - 2) / 4).getName(),
//                        sequenceFlowCollection.get(i / 2 - 1),
//                        sequenceFlowCollection.get(i / 2));
//                flowElementList.add(parallelGateway);
//            } else if (i % 4 == 0 && i != flow.getLayerList().size() * 4) {
//                for (int j = 0; j < idCollection.get(i).size(); j++) {
//                    userTask = designUserTask(
//                            idCollection.get(i).get(j),
//                            flow.getLayerList().get(i / 4 - 1).getName(),
//                            sequenceFlowCollection.get(i / 2 - 1),
//                            sequenceFlowCollection.get(i / 2));
//                    userTask.setCandidateUsers(flow.getLayerList().get(i / 4 - 1).getOperatorList());
//                    flowElementList.add(userTask);
//                }
//            }
//        }
//        for (List<SequenceFlow> sequenceFlows : sequenceFlowCollection) {
//            flowElementList.addAll(sequenceFlows);
//        }
//        return flowElementList;
//    }
//
//    private List<List<SequenceFlow>> designSequenceFlowCollection(List<List<String>> idCollection,
//                                                                  Flow flow) {
//        SequenceFlow sequenceFlow;
//        List<SequenceFlow> sequenceFlowListIn;
//        List<SequenceFlow> sequenceFlowListOut;
//        // 导向流总集合
//        List<List<SequenceFlow>> sequenceFlowCollection = new ArrayList<>();
//        // 开始事件导出流
//        sequenceFlow = designSequenceFlow(
//                idCollection.get(1).get(0),
//                // 上接开始事件
//                idCollection.get(0).get(0),
//                // 下接并行网关
//                idCollection.get(2).get(0));
//        sequenceFlowListOut = Arrays.asList(sequenceFlow);
//        // 开始事件导出流加载
//        sequenceFlowCollection.add(sequenceFlowListOut);
//        for (int a = 0; a < flow.getLayerList().size(); a++) {
//            // 用户任务导入流集合
//            sequenceFlowListIn = new ArrayList<>();
//            // 用户任务导出流集合
//            sequenceFlowListOut = new ArrayList<>();
//            for (int b = 0; b < flow.getLayerList().get(a).getGroupList().size(); b++) {
//                // 用户任务导入流
//                sequenceFlow = designSequenceFlow(
//                        idCollection.get(a * 4 + 3).get(b),
//                        // 上接并行网关
//                        idCollection.get(a * 4 + 2).get(0),
//                        // 下接用户任务
//                        idCollection.get(a * 4 + 4).get(b));
//                sequenceFlowListIn.add(sequenceFlow);
//                // 用户任务导出流
//                sequenceFlow = designSequenceFlow(
//                        idCollection.get(a * 4 + 5).get(b),
//                        // 上接用户任务
//                        idCollection.get(a * 4 + 4).get(b),
//                        // 下接并行网关
//                        idCollection.get(a * 4 + 6).get(0));
//                sequenceFlowListOut.add(sequenceFlow);
//            }
//            // 工作层导向流加载
//            sequenceFlowCollection.add(sequenceFlowListIn);
//            sequenceFlowCollection.add(sequenceFlowListOut);
//        }
//        // 结束事件导入流
//        sequenceFlow = designSequenceFlow(
//                idCollection.get(idCollection.size() - 2).get(0),
//                // 上接并行网关
//                idCollection.get(idCollection.size() - 3).get(0),
//                // 下接结束事件
//                idCollection.get(idCollection.size() - 1).get(0));
//        sequenceFlowListIn = Arrays.asList(sequenceFlow);
//        // 结束事件导入流加载
//        sequenceFlowCollection.add(sequenceFlowListIn);
//
//        return sequenceFlowCollection;
//    }
//
//    private List<List<String>> designBasic(Flow flow) {
//        // 假设工作层数为X，每一层的操作组为Y个（Y为不定量）
//        // 加上开始事件和结束事件
//        // 一共有X+2层
//        // X+2层中间添加网关，则网关层数为X+1层
//        // 一共2X+3层
//        // 网关与上下层之间添加导向流组，则导向流组的层数为2X+2层，每个导向流组的组数与其连接的工作层层内操作组数（Y）一致
//        // 最终一共4X+5层
//        List<List<String>> idCollection = new ArrayList<>(flow.getLayerList().size() * 4 + 5);
//        for (int i = 0; i < flow.getLayerList().size() * 4 + 5; i++) {
//            if (i == 0) {
//                // 开始事件
//                idCollection.add(i, Arrays.asList(UUID.randomUUID().toString()));
//            } else if (i == flow.getLayerList().size() * 4 + 4) {
//                // 结束事件
//                idCollection.add(i, Arrays.asList(UUID.randomUUID().toString()));
//            } else if (i % 4 == 0) {
//                // 任务层
//                idCollection.add(i, flow.getLayerList().get(i / 4 - 1).getGroupIdList());
//            } else if (i % 4 == 2 && i != flow.getLayerList().size() * 4 + 2) {
//                // 网关
//                idCollection.add(i, Arrays.asList(flow.getLayerList().get((i - 2) / 4).getId()));
//            } else if (i % 4 == 1 && i != 1) {
//                // 任务层导出流
//                idCollection.add(i,
//                        flow.getLayerList().get((i - 5) / 4).getGroupList().stream()
//                                .map(group -> UUID.randomUUID().toString())
//                                .collect(Collectors.toList()));
//            } else if (i % 4 == 3 && i != flow.getLayerList().size() * 4 + 3) {
//                // 任务层导入流
//                idCollection.add(i,
//                        flow.getLayerList().get((i - 3) / 4).getGroupList().stream()
//                                .map(group -> UUID.randomUUID().toString())
//                                .collect(Collectors.toList()));
//            } else {
//                idCollection.add(i, Arrays.asList(UUID.randomUUID().toString()));
//            }
//        }
//        return idCollection;
//    }
//
//    private ParallelGateway designParallelGateway(@NotNull String id,
//                                                  @NotNull String name,
//                                                  @NotNull List<SequenceFlow> incomingFlows,
//                                                  @NotNull List<SequenceFlow> outgoingFlows) {
//        ParallelGateway parallelGateway = new ParallelGateway();
//        parallelGateway.setId(id);
//        parallelGateway.setName(name);
//        parallelGateway.setIncomingFlows(incomingFlows);
//        parallelGateway.setOutgoingFlows(outgoingFlows);
//        return parallelGateway;
//    }
//
//    private UserTask designUserTask(@NotNull String id,
//                                    @NotNull String name,
//                                    @NotNull List<SequenceFlow> incomingFlows,
//                                    @NotNull List<SequenceFlow> outgoingFlows) {
//        UserTask userTask = new UserTask();
//        userTask.setId(id);
//        userTask.setName(name);
//        userTask.setIncomingFlows(incomingFlows);
//        userTask.setOutgoingFlows(outgoingFlows);
//        return userTask;
//    }
//
//    private SequenceFlow designSequenceFlow(@NotNull String id,
//                                            @NotNull String sourceRef,
//                                            @NotNull String targetRef) {
//        SequenceFlow sequenceFlow = new SequenceFlow();
//        sequenceFlow.setId(id);
//        sequenceFlow.setSourceRef(sourceRef);
//        sequenceFlow.setTargetRef(targetRef);
//        return sequenceFlow;
//    }
//
//    private StartEvent designStartEvent(@NotNull String id,
//                                        @NotNull String name,
//                                        @NotNull List<SequenceFlow> outgoingFlows) {
//        StartEvent startEvent = new StartEvent();
//        startEvent.setId(id);
//        startEvent.setName(name);
//        startEvent.setOutgoingFlows(outgoingFlows);
//        return startEvent;
//    }
//
//    private EndEvent designEndEvent(@NotNull String id,
//                                    @NotNull String name,
//                                    @NotNull List<SequenceFlow> incomingFlows) {
//        EndEvent endEvent = new EndEvent();
//        endEvent.setId(id);
//        endEvent.setName(name);
//        endEvent.setIncomingFlows(incomingFlows);
//        return endEvent;
//    }
//}
