package com.hld.query.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author huald
 * @version 1.0.0
 * @Description:查询数据封装类
 * @date 2019年1月8日 下午1:31:41
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryOptions {

    /**
     * 查询列集合
     */
    private List<String> columns;
    /**
     * 查询列值集合
     */
    private List<IFilter> filters;

    /**
     * 排序集合
     */
    private List<IOrderBy> orderBys;

    /**
     * 分组集合
     */
    private List<String> groupBys;

    /**
     * 当前页
     */
    private Long curPage;
    /**
     * 每页条数
     */
    private Long limit;

    /**
     * 最先拼接参数 对前端进行忽略，防止SQL注入风险
     */
    @JsonIgnore
    private String firstSql;
}
