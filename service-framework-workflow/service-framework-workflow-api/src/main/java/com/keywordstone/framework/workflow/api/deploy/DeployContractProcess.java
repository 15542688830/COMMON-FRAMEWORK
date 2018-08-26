package com.keywordstone.framework.workflow.api.deploy;

import com.keywordstone.framework.workflow.api.config.ContractPropertiesConfig;
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
public class DeployContractProcess {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContractPropertiesConfig contractPropertiesConfig;


    @PostConstruct
    private void  deploy(){

        if (contractPropertiesConfig.isContractDeploy()) {

            for (String path : CONTRACT_AUDIT) {
                repositoryService.createDeployment()
                        .addClasspathResource(PREFIX + path + SUFFIX)
                        .deploy();
            }
        }

    }

}
