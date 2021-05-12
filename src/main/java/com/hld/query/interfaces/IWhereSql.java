package com.hld.query.interfaces;

import com.hld.query.wrapper.GroupByWrapper;
import com.hld.query.wrapper.OrderBySqlWrapper;
import com.hld.query.wrapper.SqlWrapper;

import java.util.Collection;

public interface IWhereSql<T extends SqlWrapper> {

    T eq(boolean condition, String column, Object value);

    T eq(String column, Object value);

    T ne(boolean condition, String column, Object value);

    T ne(String column, Object value);

    T ge(boolean condition, String column, Object value);

    T ge(String column, Object value);

    T gt(boolean condition, String column, Object value);

    T gt(String column, Object value);

    T le(boolean condition, String column, Object value);

    T le(String column, Object value);

    T lt(boolean condition, String column, Object value);

    T lt(String column, Object value);

    T in(String column, Collection<?> values);

    T in(boolean condition, String column, Collection<?> values);

    T notIn(String column, Collection<?> values);

    T notIn(boolean condition, String column, Collection<?> values);

    T isNull(String column);

    T isNull(boolean condition, String column);

    T isNotNull(String column);

    T isNotNull(boolean condition, String column);

    T between(String column, Object val1, Object val2);

    T between(boolean condition, String column, Object val1, Object val2);

    T notBetween(String column, Object val1, Object val2);

    T notBetween(boolean condition, String column, Object val1, Object val2);

    T like(String column, Object value);

    T like(boolean condition, String column, Object value);

    T notLike(String column, Object value);

    T notLike(boolean condition, String column, Object value);

    T leftLike(String column, Object value);

    T leftLike(boolean condition, String column, Object value);

    T rightLike(String column, Object value);

    T rightLike(boolean condition, String column, Object value);

    T apply(String sql);

    T apply(boolean condition, String sql);

    T apply(String sql, Object... values);

    T apply(boolean condition, String sql, Object... values);

    T groupBy(boolean condition, String groupBySql);

    T groupBy(String groupBySql);

    T groupBy(boolean condition, GroupByWrapper groupByWrapper);

    T groupBy(GroupByWrapper groupByWrapper);

    T orderBy(boolean condition, String orderBySql);

    T orderBy(String orderBySql);

    T orderBy(boolean condition, OrderBySqlWrapper orderByWrapper);

    T orderBy(OrderBySqlWrapper orderByWrapper);


}
