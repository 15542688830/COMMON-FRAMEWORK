package com.keywordstone.framework.common.basic.entity;

import com.keywordstone.framework.common.basic.enums.OrderByEnum;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Pageable {

    /**
     * 是否分页
     */
    private boolean page;

    /**
     * 是否排序
     */
    private boolean order;

    /**
     * 是否乱序
     */
    private boolean random;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 页幅
     */
    private Integer pageSize;

    /**
     * 排序集合
     */
    private List<OrderBy> orderByList;

    /**
     * 分页构建
     */
    @Getter(AccessLevel.PRIVATE)
    private PageableBuilder builder;

    public PageableBuilder createBuilder() {
        this.builder = new PageableBuilder(this);
        return this.builder;
    }

    public class PageableBuilder {

        /**
         * 分页器
         */
        private Pageable pageableInstance;

        public PageableBuilder(Pageable pageable) {
            this.pageableInstance = pageable;
        }

        public PageableBuilder pageNo(Integer pageNo) {
            this.pageableInstance.pageNo = pageNo;
            return this;
        }

        public PageableBuilder pageSize(Integer pageSize) {
            this.pageableInstance.pageSize = pageSize;
            return this;
        }

        public PageableBuilder random(boolean ifRandom) {
            this.pageableInstance.random = ifRandom;
            return this;
        }

        public PageableBuilder orderBy(String key,
                                       OrderByEnum order) {
            if (null == this.pageableInstance.orderByList) {
                this.pageableInstance.orderByList = new ArrayList<>();
            }
            this.pageableInstance.orderByList.add(OrderBy.orderBy(key, order));
            return this;
        }

        public Pageable build() {
            if (null != this.pageableInstance.pageNo || null != this.pageableInstance.pageSize) {
                this.pageableInstance.page = true;
            }
            if (this.pageableInstance.random) {
                return this.pageableInstance;
            }
            if (null != this.pageableInstance.orderByList && !this.pageableInstance.orderByList.isEmpty()) {
                this.pageableInstance.order = true;
            }
            return this.pageableInstance;
        }
    }
}
