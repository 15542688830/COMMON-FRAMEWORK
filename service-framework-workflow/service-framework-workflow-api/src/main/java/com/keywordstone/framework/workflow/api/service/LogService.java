package com.keywordstone.framework.workflow.api.service;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;

import java.util.List;

public interface LogService<T,E> {

    boolean insert(T t);

    PageResultDTO<T> findBy(E e, Pageable pageable);

    List<T> findAllBy(E e);
}
