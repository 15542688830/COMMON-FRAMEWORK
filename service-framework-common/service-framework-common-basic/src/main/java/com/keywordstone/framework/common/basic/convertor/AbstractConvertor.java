package com.keywordstone.framework.common.basic.convertor;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConvertor<DTO extends AbstractDTO, ENTITY> {

    public abstract DTO toDTO(ENTITY entity);

    public abstract ENTITY toEntity(DTO dto);

    public List<DTO> toListDTO(List<ENTITY> entityList) {
        if (null != entityList && entityList.size() > 0) {
            return entityList.stream()
                    .filter(entity -> null != entity)
                    .map(entity -> toDTO(entity))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<ENTITY> toListEntity(List<DTO> dtoList) {
        if (null != dtoList && dtoList.size() > 0) {
            return dtoList.stream()
                    .filter(dto -> null != dto)
                    .map(dto -> toEntity(dto))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public ResultDTO<DTO> toResultDTO(ENTITY entity) {
        if (null != entity) {
            return ResultDTO.success(toDTO(entity));
        }
        return ResultDTO.success();
    }

    public ResultDTO<List<DTO>> toListResultDTO(List<ENTITY> entityList) {
        if (null != entityList) {
            return ResultDTO.success(toListDTO(entityList));
        }
        return ResultDTO.success();
    }

    public ResultDTO<PageResultDTO<DTO>> toPageResultDTO(PageResultDTO<ENTITY> pageResultEntity) {
        if (null != pageResultEntity) {
            return ResultDTO.success(PageResultDTO.result(
                    pageResultEntity.getCount(),
                    toListDTO(pageResultEntity.getResult())));
        }
        return ResultDTO.success(PageResultDTO.result(0, null));
    }
}
