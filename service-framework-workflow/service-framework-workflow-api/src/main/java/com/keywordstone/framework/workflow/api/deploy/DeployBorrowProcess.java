package com.keywordstone.framework.workflow.api.deploy;

import com.keywordstone.framework.workflow.api.config.BorrowPropertiesConfig;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.keywordstone.framework.workflow.api.utils.WorkFlowConst.*;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:20 2018/4/28
 * @Modified By:
 */
@Component
public class DeployBorrowProcess {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private BorrowPropertiesConfig borrowPropertiesConfig;


    @PostConstruct
    private void  deploy(){

        if (borrowPropertiesConfig.isBorrowDeploy()) {
            repositoryService.createDeployment()
                    .addClasspathResource(PREFIX + BPMN_XML + SUFFIX)
                    .deploy();
        }

    }

}
