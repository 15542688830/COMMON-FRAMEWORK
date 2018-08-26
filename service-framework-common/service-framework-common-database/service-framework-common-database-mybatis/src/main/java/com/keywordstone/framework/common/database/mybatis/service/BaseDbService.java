package com.keywordstone.framework.common.database.mybatis.service;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import com.keywordstone.framework.common.basic.entity.BasicEntity;
import com.keywordstone.framework.common.database.mybatis.base.BaseExample;
import com.keywordstone.framework.common.database.mybatis.mapper.BaseMapper;
import com.keywordstone.framework.common.database.mybatis.utils.PageUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author k
 */
@Service
public abstract class BaseDbService<ENTITY extends BasicEntity, EXAMPLE extends BaseExample> {

    protected String[] ctrl;

    private final static String NO_AUTH = "权限不足";

    @Value("${spring.application.name}")
    protected String appName;

    @Autowired
    private BaseMapper<ENTITY, EXAMPLE> mapper;

    protected boolean appCtrl() {
        return Arrays.asList(ctrl).stream().allMatch(s -> appName.startsWith(s));
    }

    public ResultDTO<Long> countByExample(@NotNull EXAMPLE example) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        return ResultDTO.success(mapper.countByExample(example));
    }

    public ResultDTO<String> insertSelective(@NotNull ENTITY record) {
        if (!record.preInsert()
                || !appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        mapper.insertSelective(record);
        return ResultDTO.success(record.getId());
    }

    public ResultDTO<List<ENTITY>> selectByExample(@NotNull EXAMPLE example) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        return ResultDTO.success(mapper.selectByExample(example));
    }

    public ResultDTO<ENTITY> selectByPrimaryKey(@NotEmpty String id) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        return ResultDTO.success(mapper.selectByPrimaryKey(id));
    }

    public ResultDTO<List<String>> updateByExampleSelective(@NotNull ENTITY record,
                                                            @NotNull EXAMPLE example) {
        if (!record.preUpdate()
                || !appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        List<ENTITY> entityList = mapper.selectByExample(example);
        if (null != entityList) {
            List<String> idList = entityList.stream()
                    .map(entity -> entity.getId())
                    .collect(Collectors.toList());
            mapper.updateByExampleSelective(record, example);
            return ResultDTO.success(idList);
        }
        return ResultDTO.success();
    }

    public ResultDTO<String> updateByPrimaryKeySelective(@NotNull ENTITY record) {
        if (!record.preUpdate()
                || !appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        mapper.updateByPrimaryKeySelective(record);
        return ResultDTO.success(record.getId());
    }

    public ResultDTO<String> removeByPrimaryKey(@NotEmpty String id) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        ENTITY entity = mapper.selectByPrimaryKey(id);
        if (null != entity) {
            if (!entity.remove()) {
                return ResultDTO.failure(ResultError.error(NO_AUTH));
            }
            mapper.updateByPrimaryKeySelective(entity);
            return ResultDTO.success(entity.getId());
        }
        return ResultDTO.success();
    }

    public ResultDTO<List<String>> removeByExample(@NotNull EXAMPLE example) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        List<ENTITY> entityList = mapper.selectByExample(example);
        if (null != entityList) {
            List<String> idList = new ArrayList<>();
            for (ENTITY entity : entityList) {
                if (!entity.remove()) {
                    return ResultDTO.failure(ResultError.error(NO_AUTH));
                }
                idList.add(entity.getId());
                mapper.updateByPrimaryKeySelective(entity);
            }
            return ResultDTO.success(idList);
        }
        return ResultDTO.success();
    }

    public ResultDTO<PageResultDTO<ENTITY>> selectByExamplePageable(@NotNull EXAMPLE example,
                                                                    Integer pageNo,
                                                                    Integer pageSize) {
        if (!appCtrl()) {
            return ResultDTO.failure(ResultError.error(NO_AUTH));
        }
        long count = mapper.countByExample(example);
        if (0 == count) {
            return ResultDTO.success(PageResultDTO.result(0, null));
        }
        example.setOrderByClause(PageUtils.pageMysql(count, example.getOrderByClause(), pageNo, pageSize));
        return ResultDTO.success(PageResultDTO.result(count, mapper.selectByExample(example)));
    }
}
