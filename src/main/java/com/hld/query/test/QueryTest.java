package com.hld.query.test;

import com.hld.query.params.OrderType;
import com.hld.query.test.vo.SysUserVO;
import com.hld.query.params.IFilter;
import com.hld.query.params.IOrderBy;

import java.util.*;

import com.hld.query.params.QueryOptions;
import com.hld.query.util.QueryUtils;

/**
 * @author huald
 * @date 2019/12/31
 */
public class QueryTest {

    public static void main(String[] args) {
        test2();
    }

    private static void test2() {
        String[] s = new String[]{"1", "23"};
        List<String> list = new ArrayList<>(12);

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        for (String str : list) {
        }
    }

    private static void test() {
        QueryOptions options = new QueryOptions();
        options.setFirstSql("asdasfdasf");
        System.out.println(options.toString());
        test1(options);
    }

    private static void test1(QueryOptions options) {
        System.out.println(options.toString());
    }

    private static void testSql() {
        QueryOptions options = new QueryOptions();

        List<String> columns = new ArrayList<>(12);
        columns.add("id");
        columns.add("deptName");

        List<IFilter> filters = new ArrayList<>(12);

        IFilter iFilter = new IFilter();
        List<Object> l = new ArrayList<>();
        l.add("sdids");
        iFilter.setFilterName("deptName").setFilterValue(l);

        filters.add(iFilter);


        options.setColumns(columns).setFilters(filters).setCurPage(null).setLimit(null);

        List<String> groups = new ArrayList<>(12);
        groups.add("id");
        groups.add("deptName");

        List<IOrderBy> orders = new ArrayList<>(12);
        IOrderBy orderBy = new IOrderBy();
        orderBy.setOrderByName("id").setOrderByType(OrderType.ASC);
        orders.add(orderBy);


        options.setOrderBys(orders).setGroupBys(groups);

        String s = commonQuery(options);
        System.out.println("test sql = " + s);
    }

    public static String commonQuery(QueryOptions params) {
//        QueryUtils<SysUserVO> queryUtils = new QueryUtils<>();
//        return queryUtils.testSql2(params, SysUserVO.class, null);
        return QueryUtils.testSql(params, SysUserVO.class, null);
    }
}
