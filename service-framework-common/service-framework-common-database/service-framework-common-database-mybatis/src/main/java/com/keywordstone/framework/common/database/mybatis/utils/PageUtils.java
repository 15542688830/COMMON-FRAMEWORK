package com.keywordstone.framework.common.database.mybatis.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {

    private final static int DEFAULT_PAGE_NO = 1;
    private final static int DEFAULT_PAGE_SIZE = 10;

    public static String pageMysql(long count,
                                   String orderBy,
                                   Integer pageNo,
                                   Integer pageSize) {
        if (0 == count) {
            return StringUtils.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtils.SPACE);
        if (StringUtils.isNotEmpty(orderBy)) {
            builder.append(orderBy);
        } else {
            builder.append("ID");
        }
        if (null == pageNo || 1 > pageNo) {
            pageNo = DEFAULT_PAGE_NO;
        }
        if (null == pageSize || 1 > pageSize) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageNo * pageSize > count) {
            pageNo = Math.toIntExact(count / pageSize) + 1;
        }
        builder.append(StringUtils.SPACE);
        builder.append("LIMIT");
        builder.append(StringUtils.SPACE);
        builder.append((pageNo - 1) * pageSize);
        builder.append(StringUtils.SPACE);
        builder.append(",");
        builder.append(StringUtils.SPACE);
        builder.append(pageSize);
        return builder.toString();
    }
}
