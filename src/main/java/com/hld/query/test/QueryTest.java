package com.hld.query.test;

import com.hld.query.params.IFilter;
import com.hld.query.params.IOrderBy;
import com.hld.query.params.OrderType;
import com.hld.query.params.QueryOptions;

import java.util.ArrayList;
import java.util.List;

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

}
