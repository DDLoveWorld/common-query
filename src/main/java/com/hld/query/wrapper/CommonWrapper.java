package com.hld.query.wrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.hld.query.enums.DatabaseType;
import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;
import com.hld.query.params.*;
import com.hld.query.util.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 自定义查询器
 *
 * @param <T> 参数实体
 * @author huald
 * @time 2019-07-18
 */
@Slf4j
public class CommonWrapper<T> extends AbstractWrapper<T, String, CommonWrapper<T>> implements Query<CommonWrapper<T>, T, String> {
    private SharedString sqlSelect;
    private List<IFilter> filters = null;
    private List<String> columns;
    private List<IOrderBy> orderBys;
    private Long curPage = null;
    private Long limit = null;
    private String firstSql = null;
    private DatabaseType databaseType = DatabaseType.MYSQL;

    public CommonWrapper() {
        this((T) null);
    }

    @Override
    protected CommonWrapper<T> instance() {
        return null;
    }

    public CommonWrapper(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    public CommonWrapper(T entity, QueryOptions options) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
        if (options != null) {
            this.filters = options.getFilters();
            this.columns = options.getColumns();
            this.orderBys = options.getOrderBys();
            this.curPage = options.getCurPage();
            this.limit = options.getLimit();
            this.firstSql = options.getFirstSql();
            splitColumns(this, columns);
            splitFilters(this, filters, false);
            splitOrderBy(this, orderBys);
        }
    }

    public CommonWrapper(QueryOptions options) {
        this.sqlSelect = new SharedString();
        // super.setEntity(entity);
        super.initNeed();
        if (options != null) {
            this.filters = options.getFilters();
            this.columns = options.getColumns();
            this.orderBys = options.getOrderBys();
            this.curPage = options.getCurPage();
            this.limit = options.getLimit();
            this.firstSql = options.getFirstSql();
            splitColumns(this, columns);
            splitFilters(this, filters, false);
            splitOrderBy(this, orderBys);
        }
    }

    public CommonWrapper(QueryOptions options, DatabaseType databaseType) {
        this.sqlSelect = new SharedString();
        // super.setEntity(entity);
        super.initNeed();
        if (databaseType != null) {
            this.databaseType = databaseType;
        }
        if (options != null) {
            this.filters = options.getFilters();
            this.columns = options.getColumns();
            this.orderBys = options.getOrderBys();
            this.curPage = options.getCurPage();
            this.limit = options.getLimit();
            this.firstSql = options.getFirstSql();
            splitColumns(this, columns);
            splitFilters(this, filters, false);
            splitOrderBy(this, orderBys);
        }
    }

    public List<IFilter> getFilters() {
        return filters;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<IOrderBy> getOrderBys() {
        return orderBys;
    }

    public Long getCurPage() {
        return curPage;
    }

    public Long getLimit() {
        return limit;
    }

    public String getFirstSql() {
        return firstSql;
    }

    public void setFirstSql(String firstSql) {
        this.firstSql = firstSql;
    }

    /**
     * 添加排序字段
     *
     * @param wrapper
     * @param orderBys 排序参数
     */
    private void splitOrderBy(CommonWrapper<T> wrapper, List<IOrderBy> orderBys) {
        if (orderBys != null && orderBys.size() > 0) {
            int length = orderBys.size();
            for (int i = 0; i < length; i++) {
                IOrderBy orderBy = orderBys.get(i);
                //校验是否有SQL注入风险
                orderBy.setOrderByName(SqlFilter.sqlInject(orderBy.getOrderByName()));
                if (OrderType.ASC == orderBy.getOrderByType()) {
                    wrapper.orderByAsc(orderBy.getOrderByName());
                } else {
                    wrapper.orderByDesc(orderBy.getOrderByName());
                }
            }
        }
    }

    /**
     * 拼接返回字段
     *
     * @param wrapper
     * @param columns
     */
    private void splitColumns(CommonWrapper<T> wrapper, List<String> columns) {
        if (columns != null && columns.size() > 0) {
            if (log.isDebugEnabled()) {
                log.debug("common wrapper columns:[{}]", columns.toString());
            }
            //校验是否有SQL注入风险,将校验操作提前
            //列没有注入风险
//            columns = SqlFilter.sqlInject(columns);
            wrapper.select(columns.toArray(new String[columns.size()]));
        } else {
            wrapper.select(" * ");
        }
    }


    public CommonWrapper(T entity, String... columns) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    private CommonWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        // this.entityClass = entityClass;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    @Override
    public CommonWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(",", columns));
        }

        return (CommonWrapper) this.typedThis;
    }

    @Override
    public CommonWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        return null;
    }

    @Override
    public CommonWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        return null;
    }

    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }


    /**
     * 构造查询SQL条件
     *
     * @param wrapper
     * @param filters
     * @param isSecond
     * @return
     */
    private CommonWrapper<T> splitFilters(CommonWrapper<T> wrapper, List<IFilter> filters, boolean isSecond) {
        if (filters == null || filters.size() <= 0) {
            return wrapper;
        }
        log.info("common wrapper split filters isSecond:[{}]", isSecond);
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            IFilter filter = filters.get(i);
            //过滤Object为空，则跳过 ,过滤列名为空，则跳过
            if (filter == null) {
                continue;
            }
            if (filter.getFilterType() == null) {
                filter.setFilterType(FilterType.DEFAULT);
            }
            log.info("common wrapper split filters type:[{}]", filter.getFilterType());
            if (filter.getFilterType() == FilterType.OR_NEW || filter.getFilterType() == FilterType.AND_NEW) {
                CommonWrapper<T> childWrapper = splitFilters(new CommonWrapper<T>(), filter.getChildren(), true);
                String sql = QueryUtils.splitWhereSql(childWrapper.getSqlSegment(), childWrapper.getParamNameValuePairs());
                if (filter.getFilterType() == FilterType.OR_NEW) {
                    wrapper.or();
                }
                wrapper.apply(" (" + sql + " )");
            } else {
                if (filter.getFilterName() == null) {
                    continue;
                }
                if (filter.getFilterType() == FilterType.OR) {
                    wrapper.or();
                } else {
                    wrapper.and(true);
                }
                //过滤类型为空，则设置默认值为EQUAL
                if (filter.getCondition() == null) {
                    filter.setCondition(ICondition.EQUAL);
                }
                List<Object> filterValues = filter.getFilterValue();
                if (filterValues != null && filterValues.size() > 0) {
                    if (isSecond) {
                        if (i != 0) {
                            if (filter.getFilterType() == FilterType.OR) {
                                wrapper.or();
                            } else {
                                wrapper.and(true);
                            }
                        }
                    }
                    //拼接查询函数
                    splitCondition(wrapper, filter, filterValues, isSecond);
                } else {
                    splitCondition2(wrapper, filter);
                }

            }
        }

        return wrapper;
    }

    /**
     * 拼接查询函数（=,!=,>,<）
     *
     * @param wrapper
     * @param filter
     * @param filterValues
     */
    private void splitCondition(CommonWrapper<T> wrapper, IFilter filter, List<Object> filterValues, boolean isSecond) {
        String filterName = filter.getFilterName();
        //校验是否有SQL注入风险
        filterName = SqlFilter.sqlInject(filterName);
        filterValues = SqlFilter.sqlInjectObject(filterValues);
        if (filter.getCondition() == ICondition.EQUAL) {
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, "=");
            } else {
                wrapper.eq(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.UN_EQUAL) {
            //不等于
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, "!=");
            } else {
                wrapper.ne(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.LIKE) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.LIKE + ") 不支持时间类型参数");
            }
            for (int j = 0; j < filterValues.size(); j++) {
                Object filterValue = filterValues.get(j);
                wrapper.like(filterValue != null, ReflexUtil.humpToUnderline(filterName),
                        filterValue != null ? splitSingleQuote(filterValue.toString()) : "");
                if (isSecond) {
                    if (j != filterValues.size() - 1) {
                        if (filter.getFilterType() == FilterType.OR) {
                            wrapper.or();
                        } else {
                            wrapper.and(true);
                        }
                    }
                }
            }
        } else if (filter.getCondition() == ICondition.LEFT_LIKE) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.LEFT_LIKE + ") 不支持时间类型参数");
            }
            for (int j = 0; j < filterValues.size(); j++) {
                Object filterValue = filterValues.get(j);
                wrapper.likeLeft(filterValue != null, ReflexUtil.humpToUnderline(filterName),
                        filterValue != null ? splitSingleQuote(filterValue.toString()) : "");
                if (isSecond) {
                    if (j != filterValues.size() - 1) {
                        if (filter.getFilterType() == FilterType.OR) {
                            wrapper.or();
                        } else {
                            wrapper.and(true);
                        }
                    }
                }
            }
        } else if (filter.getCondition() == ICondition.RIGHT_LIKE) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.RIGHT_LIKE + ") 不支持时间类型参数");
            }
            for (int j = 0; j < filterValues.size(); j++) {
                Object filterValue = filterValues.get(j);
                wrapper.likeRight(filterValue != null, ReflexUtil.humpToUnderline(filterName),
                        filterValue != null ? splitSingleQuote(filterValue.toString()) : "");
                if (isSecond) {
                    if (j != filterValues.size() - 1) {
                        if (filter.getFilterType() == FilterType.OR) {
                            wrapper.or();
                        } else {
                            wrapper.and(true);
                        }
                    }
                }
            }
        } else if (filter.getCondition() == ICondition.NO_LIKE) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NO_LIKE + ") 不支持时间类型参数");
            }
            for (int j = 0; j < filterValues.size(); j++) {
                Object filterValue = filterValues.get(j);
                wrapper.notLike(filterValue != null, ReflexUtil.humpToUnderline(filterName),
                        filterValue != null ? splitSingleQuote(filterValue.toString()) : "");
                if (isSecond) {
                    if (j != filterValues.size() - 1) {
                        if (filter.getFilterType() == FilterType.OR) {
                            wrapper.or();
                        } else {
                            wrapper.and(true);
                        }
                    }
                }
            }
        } else if (filter.getCondition() == ICondition.IN) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.IN + ") 不支持时间类型参数");
            }
            wrapper.in(ReflexUtil.humpToUnderline(filterName), filterValues);
        } else if (filter.getCondition() == ICondition.NO_IN) {
            //不等于
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NO_IN + ") 不支持时间类型参数");
            }
            wrapper.notIn(ReflexUtil.humpToUnderline(filterName), filterValues);
        } else if (filter.getCondition() == ICondition.GE) {
            //ge >=
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, ">=");
            } else {
                wrapper.ge(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.LE) {
            //le <=
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, "<=");
            } else {
                wrapper.le(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.GT) {
            //gt >
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, ">");
            } else {
                wrapper.gt(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.LT) {
            //lt <
            if (isDateCondition(filter)) {
                splitDateCondition(wrapper, filter, "<");
            } else {
                wrapper.lt(filterValues.get(0) != null, ReflexUtil.humpToUnderline(filterName),
                        filterValues.get(0) == null ? "" : splitSingleQuote(filterValues.get(0).toString()));
            }
        } else if (filter.getCondition() == ICondition.NOT_NULL) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NOT_NULL + ") 不支持时间类型参数");
            }
            wrapper.isNotNull(ReflexUtil.humpToUnderline(filterName));
        } else if (filter.getCondition() == ICondition.NULL) {
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NULL + ") 不支持时间类型参数");
            }
            wrapper.isNull(ReflexUtil.humpToUnderline(filterName));
        } else if (filter.getCondition() == ICondition.BETWEEN) {
            if (filterValues.size() < 2) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.BETWEEN + ") 需两个参数值");
            }
            Object start = filterValues.get(0);
            Object end = filterValues.get(1);
            wrapper.between(ReflexUtil.humpToUnderline(filterName), start, end);
        } else if (filter.getCondition() == ICondition.NOT_BETWEEN) {
            if (filterValues.size() < 2) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NOT_BETWEEN + ") 需两个参数值");
            }
            Object start = filterValues.get(0);
            Object end = filterValues.get(1);
            wrapper.notBetween(ReflexUtil.humpToUnderline(filterName), start, end);
        }
    }

    /**
     * 添加查询函数，当filterValue为null的时候
     * 查询当前字段为空
     *
     * @param wrapper
     * @param filter
     */
    private void splitCondition2(CommonWrapper<T> wrapper, IFilter filter) {
        String filterName = filter.getFilterName();
        //校验是否有SQL注入风险
        filterName = SqlFilter.sqlInject(filterName);
        if (filter.getCondition() == ICondition.NOT_NULL) {
            //NOT_NULL
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NOT_NULL + ") 不支持时间类型参数");
            }
            wrapper.isNotNull(ReflexUtil.humpToUnderline(filterName));
        } else if (filter.getCondition() == ICondition.NULL) {
            //lt NULL
            if (isDateCondition(filter)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "当前condition(" + ICondition.NULL + ") 不支持时间类型参数");
            }
            wrapper.isNull(ReflexUtil.humpToUnderline(filterName));
        } else {
            //防止sql拼接错误，无And情况
            wrapper.eq("1", 1);
        }
    }

    private boolean isDateCondition(IFilter filter) {
        if (filter == null || StringUtils.isEmpty(filter.getDateFormat())) {
            return false;
        }
//        String d = (String) filter.getFilterValue().get(0);
//        if (!DateUtils.isValidDate(d, filter.getDateFormat())) {
//            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "日期格式不正确");
//        }
        return true;

    }

    /**
     * 当字段类型为时间类型时
     *
     * @param wrapper
     * @param filter
     */
    private void splitDateCondition(CommonWrapper<T> wrapper, IFilter filter, String condition) {
        StringBuilder builder = new StringBuilder();
        String value = filter.getFilterValue().get(0) + "";
        switch (this.databaseType) {
            case MYSQL:
                builder.append("date_format(").append(ReflexUtil.humpToUnderline(filter.getFilterName()))
                        .append(",'").append(filter.getDateFormat()).append("') ").append(condition).append(" {0}");
                break;
            case ORACLE:
                builder.append("to_char(").append(ReflexUtil.humpToUnderline(filter.getFilterName()))
                        .append(",'").append(filter.getDateFormat()).append("') ").append(condition).append(" {0}");
//                builder.append(ReflexUtil.humpToUnderline(filter.getFilterName())).append(condition).append(" {0}") ;
//                value = "to_date('"+value+"','"+filter.getDateFormat()+"')";
                break;
            default:
                break;
        }
        wrapper.apply(builder.toString(), value);
    }

    private String splitSingleQuote(String s) {
//        return SqlParams.SQL_SINGLE_QUOTE + s + SqlParams.SQL_SINGLE_QUOTE;
        return s;
    }

    private List<String> splitSingleQuotes(List<Object> objects) {
        if (objects == null || objects.size() <= 0) {
            return new ArrayList<>(1);
        }
        List<String> list = new ArrayList<>(objects.size());
        for (Object o : objects) {
            list.add(SqlParams.SQL_SINGLE_QUOTE + o.toString() + SqlParams.SQL_SINGLE_QUOTE);
        }
        return list;
    }
}