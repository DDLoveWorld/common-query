package com.hld.query.test;

import com.hld.query.test.vo.SysUserVO;
import com.hld.query.params.IFilter;
import com.hld.query.params.IOrderBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hld.query.params.QueryOptions;
import com.hld.query.util.QueryUtils;

/**
 * @author huald
 * @date 2019/12/31
 */
public class QueryTest {

    public static void main(String[] args) {
        testSql();
    }

    private static void testSql() {
        QueryOptions options = new QueryOptions();

        List<String> columns = new ArrayList<>(12);
        columns.add("id");
        columns.add("username");
        columns.add("deptName");
        columns.add("workNo");

        List<IFilter> filters = new ArrayList<>(12);

        options.setColumns(columns);
        options.setFilters(new ArrayList<IFilter>());
        options.setOrderBys(new ArrayList<IOrderBy>());
        options.setCurPage(null);
        options.setLimit(null);

        String s = commonQuery(options);
        System.out.println("test sql = " + s);
    }

    public static String commonQuery(QueryOptions params) {
        return QueryUtils.testSql(params, SysUserVO.class, null);
    }
}
