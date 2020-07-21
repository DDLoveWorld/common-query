package com.hld.query.test;

import com.hld.query.params.OrderType;
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
        columns.add("deptName");

        List<IFilter> filters = new ArrayList<>(12);

        IFilter iFilter = new IFilter();
        iFilter.setFilterName("deptName");
        List<Object> l = new ArrayList<>();
        l.add("sdids");
        iFilter.setFilterValue(l);

        filters.add(iFilter);



        options.setColumns(columns);
        options.setFilters(filters);
        options.setCurPage(null);
        options.setLimit(null);

        List<String> groups = new ArrayList<>(12);
        groups.add("id");
        groups.add("deptName");

        List<IOrderBy> orders = new ArrayList<>(12);
        IOrderBy orderBy = new IOrderBy();
        orderBy.setOrderByName("id");
        orderBy.setOrderByType(OrderType.ASC);
        orders.add(orderBy);


        options.setOrderBys(orders);
        options.setGroupBys(groups);

        String s = commonQuery(options);
        System.out.println("test sql = " + s);
    }

    public static String commonQuery(QueryOptions params) {
//        QueryUtils<SysUserVO> queryUtils = new QueryUtils<>();
//        return queryUtils.testSql2(params, SysUserVO.class, null);
        return QueryUtils.testSql(params, SysUserVO.class, null);
    }
}
