/**
 * Copyright (c) 2018 北航天华 All rights reserved.
 * <p>
 * https://www.techcomer.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.hld.query.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author ibuaa@techcomer.com
 */
@Data
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long total;

    private Long curPage;

    private List<T> list;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, Long total, Long curPage) {
        this.list = list;
        this.total = total;
        this.curPage = curPage;
    }
}