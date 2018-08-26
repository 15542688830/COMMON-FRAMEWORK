package com.keywordstone.framework.workflow.api.service;

import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.workflow.api.module.*;

import java.util.List;

/**
 * @author k
 */
public interface WorkService {

    String generate(Work work);

    String remove(String entityId);

    String update(Work work);

    WorkEntity findByEntityId(String entityId);

    List<Work> findByOperator();

    Work findByWorkId(String workId);

    List<WorkLog> findWorkLogByEntityId(String entityId);

    PageResultDTO<Work> findByEntityIdPageable(String entityId, Pageable pageable);

    PageResultDTO<Work> findByOperatorPageable(Pageable pageable);

    PageResultDTO<WorkLog> findWorkLogByEntityIdPageable(String entityId, Pageable pageable);

    List<Integer> dateRangeCompletedCount(List<DateRange> dateRanges);

    List<Integer> dateRangeUncompletedCount(List<DateRange> dateRanges);

    PageResultDTO<Work> complicatedSearchPageable(WorkCondition workCondition);

//    PageResultDTO<WorkNotice> noticeSearch(WorkNoticeCondition workNoticeCondition);

    String generateBase64(Work... works);

//    void noticeRead(List<String> idList);
}
