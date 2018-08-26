package com.keywordstone.framework.workflow.api.service;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;

import java.util.List;

public interface EngineService<T, E> {

    String on(T t);

    String off(T t);

    String operate(T t);

    PageResultDTO<T> search(E e, boolean withSub);

    PageResultDTO<T> searchHistory(E e, boolean withSub);

    String generateBase64(T... ts);

    Long searchCompleteCount(E e);

    Long searchUnCompleteCount(E e);

    String generateBase64By(String id,
                            List<String> keyList);
}
