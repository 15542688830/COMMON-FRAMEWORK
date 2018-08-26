package com.keywordstone.framework.common.database.mybatis.mapper;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import com.keywordstone.framework.common.database.mybatis.base.BaseExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BaseMapper<ENTITY extends BasicEntity, EXAMPLE extends BaseExample> {
    long countByExample(EXAMPLE example);

//    int insert(ENTITY record);

    int insertSelective(ENTITY record);

    List<ENTITY> selectByExample(EXAMPLE example);

    ENTITY selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

//    int updateByExample(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

    int updateByPrimaryKeySelective(ENTITY record);

//    int updateByPrimaryKey(ENTITY record);
}


