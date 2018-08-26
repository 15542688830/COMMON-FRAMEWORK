package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.workflow.api.service.BpmnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class BpmnMultiSerImpl implements BpmnService<MultipartFile> {

    @Autowired
    private BpmnService<InputStream> bpmnService;

    @Override
    public String upload(MultipartFile file) {
        if (null != file) {
            try {
                return bpmnService.upload(file.getInputStream());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public List<MultipartFile> findAll() {
        return null;
    }

    @Override
    public MultipartFile findById(String id) {
        return null;
    }

    @Override
    public void download(String id) {

    }
}
