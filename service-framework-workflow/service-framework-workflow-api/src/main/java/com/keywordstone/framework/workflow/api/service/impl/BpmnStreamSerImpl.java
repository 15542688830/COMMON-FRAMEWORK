package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.workflow.api.service.BpmnService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class BpmnStreamSerImpl implements BpmnService<InputStream> {

    @Override
    public String upload(InputStream inputStream) {
        if (null != inputStream) {
            BpmnXMLConverter converter = new BpmnXMLConverter();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = null;
            try {
                reader = factory.createXMLStreamReader(inputStream);
            } catch (XMLStreamException e) {
                log.error(e.getMessage(), e);
            }
            BpmnModel model = converter.convertToBpmnModel(reader);
            reBuildModel(model);
            model.toString();
        }
        return null;
    }

    @Override
    public List<InputStream> findAll() {
        return null;
    }

    @Override
    public InputStream findById(String id) {
        return null;
    }

    @Override
    public void download(String data) {

    }

    private void reBuildModel(BpmnModel model) {
        model.getProcesses().forEach(process -> {
            process.getFlowElements().forEach(flowElement -> {
                // TODO
                flowElement.getClass();
            });
        });
    }
}
