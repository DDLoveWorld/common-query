package com.hld.query.test;

import com.hld.query.enums.DatabaseType;
import com.hld.query.mapper.CommonMapper;
import com.hld.query.params.FilterType;
import com.hld.query.params.ICondition;
import com.hld.query.params.IFilter;
import com.hld.query.params.QueryOptions;
import com.hld.query.test.vo.SysUserVO;
import com.hld.query.util.BaseQueryUtils;
import com.hld.query.util.ResultUtil;
import com.hld.query.wrapper.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huald
 * @date 2019/12/31
 */
public class QueryTest {

    public static void main(String[] args) {

//        Object[] s = new Object[]{1,12,6};
//        String s1 = StringUtils.sqlJoin(s, ",");
//        System.out.println(s1);
        test4();
    }

    private static void test4() {


        CommonWrapper selfWrapper = new CommonWrapper(getOptions());

        System.out.println("whereSql : " + selfWrapper.getWhereSql());
        System.out.println("orderBySql : " + selfWrapper.getOrderBySql());
        System.out.println("groupBySql : " + selfWrapper.getGroupBySql());
        System.out.println("columnBySql : " + selfWrapper.getColumnSql());
        System.out.println("Sql : " + selfWrapper.getSql());

        String completedSQL = ResultUtil.getCompletedSQL(ResultUtil.getRelation(SysUserVO.class), selfWrapper.getWhereSql(), DatabaseType.MYSQL, selfWrapper);
        System.out.println("completedSql : " + completedSQL);

        ResultUtil.getPageMapResult((CommonMapper) null,getOptions(),SysUserVO.class,null);
//        CommonWrapper<SysUserEntity> commonWrapper = new CommonWrapper<>(getOptions());
//        String whereSql = splitSql(commonWrapper);
//        System.out.println("whereSql : " + whereSql);
//        System.out.println("columnBySql : " + commonWrapper.getSqlSelect());
    }

    private static QueryOptions getOptions() {
        QueryOptions options = new QueryOptions();
        //添加返回列
        List<String> columns = new ArrayList<>();
        columns.add("name");
        columns.add("id");
        options.setColumns(columns);

        //添加filter
        List<IFilter> filterList = new ArrayList<>();

        IFilter iFilter = new IFilter();
        iFilter.setFilterName("name");
        iFilter.setFilterType(FilterType.AND);
        iFilter.setCondition(ICondition.EQUAL);
        List list = new ArrayList();
        list.add("zhang");
        iFilter.setFilterValue(list);
        filterList.add(iFilter);

        IFilter iFilter1 = new IFilter();
        iFilter1.setFilterName("age");
        iFilter1.setFilterType(FilterType.OR);
        iFilter1.setCondition(ICondition.GE);
        List list1 = new ArrayList();
        list1.add(18);
        iFilter1.setFilterValue(list1);
        filterList.add(iFilter1);

        //andNew
        IFilter iFilter2 = new IFilter();
        iFilter2.setFilterType(FilterType.AND_NEW);

        List<IFilter> childFilterList2 = new ArrayList<>();
        IFilter childFilter20 = new IFilter();
        childFilter20.setFilterType(FilterType.AND);
        childFilter20.setFilterName("id");
        childFilter20.setCondition(ICondition.IN);
        List childFilterValueList20 = new ArrayList();
        childFilterValueList20.add(12);
        childFilterValueList20.add(125);
        childFilterValueList20.add(15);
        childFilter20.setFilterValue(childFilterValueList20);
        childFilterList2.add(childFilter20);

        IFilter childFilter21 = new IFilter();
        childFilter21.setFilterType(FilterType.AND);
        childFilter21.setFilterName("createDate");
        childFilter21.setCondition(ICondition.BETWEEN);
        childFilter21.setDateFormat("YYYY-MM-DD");
        List childFilterValueList21 = new ArrayList();
        childFilterValueList21.add("2020-02-20");
        childFilterValueList21.add("2020-03-06");
        childFilter21.setFilterValue(childFilterValueList21);

        childFilterList2.add(childFilter21);


        iFilter2.setChildren(childFilterList2);

        filterList.add(iFilter2);

        options.setFilters(filterList);

//        NAME = 'zhang' OR AGE >= '18' AND (ID IN('[12, 125, 15]') AND CREATE_DATE BETWEEN '2020-02-20' AND '2020-03-06' )
//        NAME = 'zhang' OR AGE >= '18' AND (ID IN ('12','125','15') AND CREATE_DATE BETWEEN '2020-02-20' AND '2020-03-06' )

        return options;
    }

    private static void test3() {
        SqlWrapper sqlWrapper = new MySqlWrapper();
        List in = new ArrayList();
        in.add("x");
        sqlWrapper.eq("zhangsan", "name")
                .ne(null, "name")
                .in("name", in)
                .andNew(new MySqlWrapper()
                        .and()
                        .ge("age", 12)
                        .le("workAge", 15)
                        .orNew(new MySqlWrapper().like("name", "ls")
                                .or().like("name", "z"))
                        .orderBy(new OrderBySqlWrapper().asc("id")).groupBy(new GroupByWrapper().groupBy("id"))
                )
                .orderBy(new OrderBySqlWrapper().asc("name", "id", "date"))
                .groupBy(new GroupByWrapper().groupBy("name", "id", "sex"));


        System.out.println(sqlWrapper.getSql());

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

}
