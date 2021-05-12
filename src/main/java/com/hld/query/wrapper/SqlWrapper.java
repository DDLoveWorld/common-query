package com.hld.query.wrapper;

import com.hld.query.interfaces.IJoin;
import com.hld.query.interfaces.ISelectSql;
import com.hld.query.interfaces.IWhereSql;
import com.hld.query.util.StringUtils;

import java.util.Collection;

public abstract class SqlWrapper<T extends SqlWrapper> extends AbstractWrapper implements IWhereSql<T>, IJoin<T>, ISelectSql<T> {

    protected String whereSql = "";

    protected String selectSql = "*";

    private String joinKeyword = IJoin.AND + IJoin.SPACE;

    public SqlWrapper() {
    }

    @Override
    public String getSql() {
        return this.whereSql;
    }

    @Override
    public boolean isFirstAdd() {
        return StringUtils.isEmpty(this.whereSql);
    }

    protected String addJoinKey() {
        return StringUtils.isEmpty(this.whereSql) ? "" : joinKeyword;
    }

    protected String addWhereKey(String whereKey) {
        return addSpace(whereKey);
    }

    protected String addWhereKey(String... whereKeys) {
        return addWhereKey(StringUtils.join(whereKeys, IJoin.SPACE));
    }

    protected String addLikeValue(Object value) {
        return "'%" + value.toString() + "%'";
    }

    protected String addRightLikeValue(Object value) {
        return "'" + value.toString() + "%'";
    }

    protected String addLeftLikeValue(Object value) {
        return "'%" + value.toString() + "'";
    }

    /**
     * **************************sql拼接where条件方法  start*******************************************
     */

    @Override
    public T ne(String column, Object value) {
        return ne(true, column, value);
    }

    @Override
    public T ne(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.NE) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }


    @Override
    public T eq(String column, Object value) {
        return eq(true, column, value);
    }

    @Override
    public T eq(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.EQ) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }


    @Override
    public T ge(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.GE) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T ge(String column, Object value) {
        return ge(true, column, value);
    }

    @Override
    public T gt(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.GT) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T gt(String column, Object value) {
        return gt(true, column, value);
    }

    @Override
    public T le(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.LE) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T le(String column, Object value) {
        return le(true, column, value);
    }

    @Override
    public T lt(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.LT) + addValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T lt(String column, Object value) {
        return lt(true, column, value);
    }

    @Override
    public T in(String column, Collection<?> values) {
        return in(true, column, values);
    }

    @Override
    public T in(boolean condition, String column, Collection<?> values) {
        if (condition && StringUtils.isNotBlank(column) && values != null && values.size() > 0) {
            String join = StringUtils.sqlJoin(values, IJoin.COMMA);
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.IN + IJoin.LEFT_PARENTHESES + join + IJoin.RIGHT_PARENTHESES);
        }
        return (T) this;
    }

    @Override
    public T notIn(String column, Collection<?> values) {
        return notIn(true, column, values);
    }

    @Override
    public T notIn(boolean condition, String column, Collection<?> values) {
        if (condition && StringUtils.isNotBlank(column) && values != null && values.size() > 0)
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.NOT, IJoin.IN) + IJoin.LEFT_PARENTHESES +
                    StringUtils.sqlJoin(values, IJoin.COMMA) + IJoin.RIGHT_PARENTHESES + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T isNull(String column) {
        return isNull(true, column);
    }

    @Override
    public T isNull(boolean condition, String column) {
        if (condition && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.IS_NULL);
        return (T) this;
    }

    @Override
    public T isNotNull(String column) {
        return isNotNull(true, column);
    }

    @Override
    public T isNotNull(boolean condition, String column) {
        if (condition && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.IS_NOT_NULL);
        return (T) this;
    }


    @Override
    public T between(String column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    @Override
    public T between(boolean condition, String column, Object val1, Object val2) {
        if (condition && StringUtils.isNotBlank(column) && val1 != null && val2 != null)
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.BETWEEN) + addValue(val1)
                    + addWhereKey(IJoin.AND) + addValue(val2) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T notBetween(String column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    @Override
    public T notBetween(boolean condition, String column, Object val1, Object val2) {
        if (condition && StringUtils.isNotBlank(column) && val1 != null && val2 != null)
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.NOT, IJoin.BETWEEN)
                    + addValue(val1) + addWhereKey(IJoin.AND) + addValue(val2) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T like(String column, Object value) {
        return like(true, column, value);
    }

    @Override
    public T like(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.LIKE) + addLikeValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T notLike(String column, Object value) {
        return notLike(true, column, value);
    }

    @Override
    public T notLike(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.NOT, IJoin.LIKE) + addLikeValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T leftLike(String column, Object value) {
        return leftLike(true, column, value);
    }

    @Override
    public T leftLike(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.LIKE) + addLeftLikeValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T rightLike(String column, Object value) {
        return rightLike(true, column, value);
    }

    @Override
    public T rightLike(boolean condition, String column, Object value) {
        if (condition && null != value && StringUtils.isNotBlank(column))
            this.whereSql += addJoinKey() + column + addWhereKey(IJoin.LIKE) + addRightLikeValue(value) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T apply(String sql) {
        return apply(true, sql);
    }

    @Override
    public T apply(boolean condition, String sql) {
        if (condition && StringUtils.isNotBlank(sql))
            this.whereSql += addJoinKey() + sql + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T apply(String sql, Object... values) {
        return apply(true, sql, values);
    }

    @Override
    public T apply(boolean condition, String sql, Object... values) {
        if (condition && StringUtils.isNotBlank(sql) && values != null && values.length > 0)
            this.whereSql += addJoinKey() + replaceSqlVariable(sql, values) + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T and() {
        if (StringUtils.isNotBlank(this.whereSql)) this.joinKeyword = IJoin.AND + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T or() {
        if (StringUtils.isNotBlank(this.whereSql)) this.joinKeyword = IJoin.OR + IJoin.SPACE;
        return (T) this;
    }

    @Override
    public T andNew() {
        return (T) this;
    }

    @Override
    public T orNew() {
        return (T) this;
    }

    @Override
    public T andNew(String childSql) {
        this.whereSql += IJoin.AND_NEW_START + childSql + IJoin.AND_NEW_END;
        return (T) this;
    }

    @Override
    public T orNew(String childSql) {
        this.whereSql += IJoin.OR_NEW_START + childSql + IJoin.OR_NEW_END;
        return (T) this;
    }

    @Override
    public T andNew(T t) {
        if (t != null && StringUtils.isNotBlank(t.whereSql))
            this.whereSql += IJoin.AND_NEW_START + t.whereSql + IJoin.AND_NEW_END;
        return (T) this;
    }

    @Override
    public T orNew(T t) {
        if (t != null && StringUtils.isNotBlank(t.whereSql))
            this.whereSql += IJoin.OR_NEW_START + t.whereSql + IJoin.OR_NEW_END;
        return (T) this;
    }

    /**
     * **************************sql拼接where条件方法  end*******************************************
     */

    /**
     * **************************sql排序条件方法  star*******************************************
     */

    @Override
    public T groupBy(boolean condition, String groupBySql) {
        if (condition && StringUtils.isNotBlank(groupBySql))
            this.whereSql += addWhereKey(groupBySql);
        return (T) this;
    }

    @Override
    public T groupBy(String groupBySql) {
        return groupBy(true, groupBySql);
    }

    @Override
    public T groupBy(boolean condition, GroupByWrapper groupByWrapper) {
        if (condition && groupByWrapper != null)
            return groupBy(true, groupByWrapper.groupBySql);
        return (T) this;
    }

    @Override
    public T groupBy(GroupByWrapper groupByWrapper) {
        return groupBy(true, groupByWrapper);
    }

    @Override
    public T orderBy(boolean condition, String orderBySql) {
        if (condition && StringUtils.isNotBlank(orderBySql))
            this.whereSql += addWhereKey(orderBySql);
        return (T) this;
    }

    @Override
    public T orderBy(String orderBySql) {
        return orderBy(true, orderBySql);
    }

    @Override
    public T orderBy(boolean condition, OrderBySqlWrapper orderByWrapper) {
        if (condition && orderByWrapper != null)
            return orderBy(true, orderByWrapper.orderBySql);
        return (T) this;
    }

    @Override
    public T orderBy(OrderBySqlWrapper orderByWrapper) {
        return orderBy(true, orderByWrapper);
    }

    /**
     * **************************sql排序条件方法  end*******************************************
     */


    /**
     * *****************************返回列 start************************
     */
    @Override
    public String getSelectColumn() {
        return this.selectSql;
    }

    @Override
    public T select(String column) {
        if (StringUtils.isNotBlank(column))
            this.selectSql = column;
        return (T) this;
    }

    @Override
    public T select(String... columns) {
        if (columns != null && columns.length > 0)
            this.selectSql = StringUtils.join(columns, IJoin.COMMA);
        return (T) this;
    }

/**
 * *****************************返回列 end************************
 */
}
