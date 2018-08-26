package com.keywordstone.framework.workflow.api.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author k
 */
@Configuration
@Slf4j
public class ProcessConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @Primary
    protected ProcessEngineConfiguration configuration() {
        return StandaloneProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcUrl(dataSourceProperties.getUrl())
                .setJdbcDriver(dataSourceProperties.getDriverClassName())
                .setJdbcUsername(dataSourceProperties.getUsername())
                .setJdbcPassword(dataSourceProperties.getPassword())
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setAsyncExecutorActivate(false)
                .setCreateDiagramOnDeploy(true)
                .setProcessDiagramGenerator(new DefaultProcessDiagramGenerator())
                .setActivityFontName("幼圆")
                .setAnnotationFontName("幼圆")
                .setLabelFontName("幼圆");
    }

    @Bean
    protected ProcessEngine engine() {
        //创建流程引擎
        return ProcessEngines.getDefaultProcessEngine();
    }
}
