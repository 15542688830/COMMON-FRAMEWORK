package com.keywordstone.framework.common.database.mybatis.config;

import com.keywordstone.framework.common.database.mybatis.mapper.BaseMapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author k
 */
@Component
@ComponentScan("com.keywordstone.framework.common.database.mybatis")
public class MybatisConfig {

    @Bean
    protected MapperScannerConfigurer configurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage(BaseMapper.class.getPackage().getName());
        return configurer;
    }
}
