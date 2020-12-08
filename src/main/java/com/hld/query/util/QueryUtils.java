package com.hld.query.util;

import com.hld.query.annotations.FormatSwitch;
import com.hld.query.annotations.TableFiledInfo;
import com.hld.query.annotations.TableRelations;
import com.hld.query.enums.DatabaseType;
import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;
import com.hld.query.mapper.CommonMapper;
import com.hld.query.params.*;
import com.hld.query.service.CommonService;
import com.hld.query.wrapper.CommonWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义查询工具类
 *
 * @author huald
 * @date 2019/8/20
 */
@Slf4j
public class QueryUtils<T> {

    public static String[] forbidColumns = new String[]{"hld"};

    public static String splitSql(CommonWrapper wrapper) {
        String sql = "";
        sql = splitWhereSql(wrapper);
        return sql;
    }

    /**
     * 二次拼接查询参数
     *
     * @param params
     * @param c
     * @return
     */
    public static QueryOptions splitOptions(QueryOptions params, Class c) {
        if (params == null) {
            return null;
        }
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        List<String> columns = params.getColumns();
        //筛选返回列是否合法
        columns = splitColumns(columns, tableInfos);
        params.setColumns(columns);
        List<IFilter> filters = params.getFilters();
        //筛选过滤条件是否合法
        filters = splitFilterNameAlias(filters, tableInfos);
        params.setFilters(filters);
        List<IOrderBy> orderBys = params.getOrderBys();
        //筛选排序参数是否合法
        orderBys = splitOrderBys(orderBys, tableInfos);
        params.setOrderBys(orderBys);
        return params;
    }

    /**
     * 二次拼接查询参数
     *
     * @param params
     * @param tableInfos 反射列
     * @return
     */
    public static QueryOptions splitOptions(QueryOptions params, List<TableInfo> tableInfos) {
        if (params == null) {
            return null;
        }
        List<String> columns = params.getColumns();
        //筛选返回列是否合法
        columns = splitColumns(columns, tableInfos);
        params.setColumns(columns);
        List<IFilter> filters = params.getFilters();
        //筛选过滤条件是否合法
        filters = splitFilterNameAlias(filters, tableInfos);
        params.setFilters(filters);
        List<IOrderBy> orderBys = params.getOrderBys();
        //筛选排序参数是否合法
        orderBys = splitOrderBys(orderBys, tableInfos);
        params.setOrderBys(orderBys);
        //筛选分组列
        List<String> groupBys = params.getGroupBys();
        groupBys = splitGroupBys(groupBys, tableInfos);
        params.setGroupBys(groupBys);
        return params;
    }

    /**
     * 递归所有filterName
     * 添加字段别名
     *
     * @param filters
     * @return
     */
    public static List<IFilter> splitFilterNameAlias(List<IFilter> filters, Class c) {
        if (filters == null || filters.size() == 0) {
            return filters;
        }
        List<TableInfo> tableInfos = getTableInfo(c);
        int size = tableInfos.size();
        int length = filters.size();
        for (int i = 0; i < length; i++) {
            IFilter filter = filters.get(i);
            //过滤Object为空，则跳过 ,过滤列名为空，则跳过
            if (filter == null || filter.getFilterName() == null) {
                continue;
            }
            if (filter.getFilterType() == null) {
                filter.setFilterType(FilterType.DEFAULT);
            }
            if (filter.getFilterType() == FilterType.OR_NEW || filter.getFilterType() == FilterType.AND_NEW) {
                splitFilterNameAlias(filter.getChildren(), c);
            } else {
                String filterName = filter.getFilterName();
                if (StringUtils.isNotBlank(filterName)) {
                    for (int j = 0; j < size; j++) {
                        TableInfo tableInfo = tableInfos.get(j);
                        String columnName2 = tableInfo.getColumnName();
                        if (filterName.equals(columnName2)) {
                            //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                            filterName = tableInfo.getTableAlias().toLowerCase() + SqlParams.SQL_POINT +
                                    (StringUtils.isEmpty(tableInfo.getTableFieldName()) ? columnName2 : tableInfo.getTableFieldName());
                            filter.setFilterName(filterName);
                            filters.set(i, filter);
                            break;
                        }
                        if (j == size - 1) {
                            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "无此过滤参数：" + filterName);
                        }
                    }

                }

            }
        }
        return filters;
    }

    /**
     * @param filters    过滤条件
     * @param tableInfos 表注解信息
     * @return
     */
    public static List<IFilter> splitFilterNameAlias(List<IFilter> filters, List<TableInfo> tableInfos) {
        if (filters == null || filters.size() == 0) {
            return filters;
        }
        if (tableInfos == null || tableInfos.size() <= 0) {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "params tableInfos is not null or empty");
        }
        int size = tableInfos.size();
        int length = filters.size();
        for (int i = 0; i < length; i++) {
            IFilter filter = filters.get(i);
            //过滤Object为空，则跳过 ,过滤列名为空，则跳过
            if (filter == null) {
                continue;
            }
            if (filter.getFilterType() == null) {
                filter.setFilterType(FilterType.DEFAULT);
            }
            if (filter.getFilterType() == FilterType.OR_NEW || filter.getFilterType() == FilterType.AND_NEW) {
                splitFilterNameAlias(filter.getChildren(), tableInfos);
            } else {
                if (filter.getFilterName() == null) {
                    continue;
                }
                String filterName = filter.getFilterName();
                if (StringUtils.isNotBlank(filterName)) {
                    for (int j = 0; j < size; j++) {
                        TableInfo tableInfo = tableInfos.get(j);
                        String columnName2 = tableInfo.getColumnName();
                        if (filterName.equals(columnName2)) {
                            //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                            filterName = tableInfo.getTableAlias().toLowerCase() + SqlParams.SQL_POINT +
                                    (StringUtils.isEmpty(tableInfo.getTableFieldName()) ? columnName2 : tableInfo.getTableFieldName());
                            filter.setFilterName(filterName);
                            filters.set(i, filter);
                            break;
                        }
                        if (j == size - 1) {
                            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "无此过滤参数：" + filterName);
                        }
                    }

                }

            }
        }
        return filters;
    }


    /**
     * 二次拼接wrapper内条件sql
     * 将引用参数转换为实参
     *
     * @param sql sql
     * @param map 实参值
     * @return
     */
    public static String splitWhereSql(String sql, Map<String, Object> map) {
        if (StringUtils.isEmpty(sql) || map == null) {
            return sql;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            //根据mybatis plus源码解析出此拼接规范
            String replaceStr = "#{ew.paramNameValuePairs." + mapKey + "}";
            Object mapValue = entry.getValue();
            //将引用参数转换为实参
            sql = sql.replace(replaceStr, mapValue == null ? "" : SqlParams.SQL_SINGLE_QUOTE + mapValue.toString() + SqlParams.SQL_SINGLE_QUOTE);
        }
        return sql;
    }

    public static String splitWhereSql(CommonWrapper wrapper) {
        if (wrapper == null) {
            throw new CommonException(ErrorCode.NOT_NULL);
        }
        String sql = wrapper.getSqlSegment();
        Map<String, Object> map = wrapper.getParamNameValuePairs();
        if (StringUtils.isEmpty(sql) || map == null) {
            if (StringUtils.isNotBlank(wrapper.getFirstSql())) {
                return wrapper.getFirstSql();
            }
            return SqlParams.SQL_NO_WHERE_SQL;
        }
        List<IOrderBy> orderBys = wrapper.getOrderBys();
        List<IFilter> filters = wrapper.getFilters();
        List<String> groupBys = wrapper.getGroupBys();
        boolean isFilter = filters == null || filters.size() == 0;
        boolean isOrder = orderBys != null && orderBys.size() > 0;
        boolean isGroup = groupBys != null && groupBys.size() > 0;
        boolean isAddNoWhereSql = isGroup || isOrder;
        if (isAddNoWhereSql && isFilter) {
            if (StringUtils.isNotBlank(wrapper.getFirstSql())) {
                return wrapper.getFirstSql() + sql;
            }
            //无筛选条件但是有排序或分组条件情况下
            return SqlParams.SQL_NO_WHERE_SQL + sql;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            //根据mybatis plus源码解析出此拼接规范
            String replaceStr = "#{ew.paramNameValuePairs." + mapKey + "}";
            Object mapValue = entry.getValue();
            //将引用参数转换为实参
            sql = sql.replace(replaceStr, mapValue == null ? "" : SqlParams.SQL_SINGLE_QUOTE + mapValue.toString() + SqlParams.SQL_SINGLE_QUOTE);
        }
        if (StringUtils.isNotBlank(wrapper.getFirstSql())) {
            return wrapper.getFirstSql() + SqlParams.SQL_AND + sql;
        }
        return sql;
    }

    /**
     * 拼接mysql分页参数
     *
     * @param sql
     * @param wrapper
     * @return
     */
    public static String splitPage(String sql, CommonWrapper wrapper) {
        Long start = getStartCount(wrapper.getCurPage(), wrapper.getLimit());
        Long end = getEndCount(wrapper.getCurPage(), wrapper.getLimit());
        if (start != null && end != null) {
            sql = sql + SqlParams.SQL_LIMIT + start + SqlParams.SQL_COMMA + end;
        }
        return sql;
    }


    /**
     * 为排序字段添加别名
     *
     * @param orderBys
     * @param c
     * @return
     */
    public static List<IOrderBy> splitOrderBys(List<IOrderBy> orderBys, Class c) {
        if (orderBys == null || orderBys.size() == 0) {
            return orderBys;
        }
        int length = orderBys.size();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        int size = tableInfos.size();

        for (int i = 0; i < length; i++) {
            IOrderBy orderBy = orderBys.get(i);
            String orderByName = orderBy.getOrderByName();
            if (StringUtils.isEmpty(orderByName)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "排序参数字段名称不能为空");
            }

            for (int j = 0; j < size; j++) {
                TableInfo tableInfo = tableInfos.get(j);
                String columnName2 = tableInfo.getColumnName();
                if (orderByName.equals(columnName2)) {
                    //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                    orderByName = tableInfo.getTableAlias().toUpperCase() + SqlParams.SQL_POINT +
                            (StringUtils.isEmpty(tableInfo.getTableFieldName()) ? ReflexUtil.humpToUnderline(columnName2)
                                    : ReflexUtil.humpToUnderline(tableInfo.getTableFieldName()));
                    orderBy.setOrderByName(orderByName);
                    orderBys.set(i, orderBy);
                    break;
                }
                if (j == size - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "无此排序参数：" + orderByName);
                }
            }
        }
        return orderBys;
    }

    /**
     * @param orderBys   排序列
     * @param tableInfos 表间关系数据
     * @return
     */
    public static List<IOrderBy> splitOrderBys(List<IOrderBy> orderBys, List<TableInfo> tableInfos) {
        if (orderBys == null || orderBys.size() == 0) {
            return orderBys;
        }
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        if (tableInfos == null || tableInfos.size() == 0) {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "params error : tableInfos is not null or empty");
        }
        int length = orderBys.size();
        int size = tableInfos.size();
        for (int i = 0; i < length; i++) {
            IOrderBy orderBy = orderBys.get(i);
            String orderByName = orderBy.getOrderByName();
            if (StringUtils.isEmpty(orderByName)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "排序参数字段名称不能为空");
            }

            for (int j = 0; j < size; j++) {
                TableInfo tableInfo = tableInfos.get(j);
                String columnName2 = tableInfo.getColumnName();
                if (orderByName.equals(columnName2)) {
                    //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                    orderByName = tableInfo.getTableAlias().toUpperCase() + SqlParams.SQL_POINT +
                            (StringUtils.isEmpty(tableInfo.getTableFieldName()) ? ReflexUtil.humpToUnderline(columnName2)
                                    : ReflexUtil.humpToUnderline(tableInfo.getTableFieldName()));
                    orderBy.setOrderByName(orderByName);
                    orderBys.set(i, orderBy);
                    break;
                }
                if (j == size - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "无此排序参数：" + orderByName);
                }
            }
        }
        return orderBys;
    }


    /**
     * @param groupBys   分组
     * @param tableInfos 表间关系数据
     * @return
     */
    public static List<String> splitGroupBys(List<String> groupBys, List<TableInfo> tableInfos) {
        if (groupBys == null || groupBys.size() == 0) {
            return groupBys;
        }
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        if (tableInfos == null || tableInfos.size() == 0) {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "params error : tableInfos is not null or empty");
        }
        int length = groupBys.size();
        int size = tableInfos.size();
        for (int i = 0; i < length; i++) {
            String groupByName = groupBys.get(i);
            if (StringUtils.isBlank(groupByName)) {
                throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "排序参数字段名称不能为空");
            }

            for (int j = 0; j < size; j++) {
                TableInfo tableInfo = tableInfos.get(j);
                String columnName2 = tableInfo.getColumnName();
                if (groupByName.equals(columnName2)) {
                    //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                    groupByName = tableInfo.getTableAlias().toUpperCase() + SqlParams.SQL_POINT +
                            (StringUtils.isEmpty(tableInfo.getTableFieldName()) ? ReflexUtil.humpToUnderline(columnName2)
                                    : ReflexUtil.humpToUnderline(tableInfo.getTableFieldName()));
                    groupBys.set(i, groupByName);
                    break;
                }
                if (j == size - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "无此分组列：" + groupByName);
                }
            }
        }
        return groupBys;
    }

    /**
     * 根据不同class的不同注解配置 拼接SQL返回字段
     *
     * @param columns 需要返回的字段名称
     * @param c       业务类
     * @return
     */
    public static List<String> splitColumns(List<String> columns, Class c) {
        if (columns == null || columns.size() == 0) {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "params error : columns is not null or empty");
        }
        int length = columns.size();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        int size = tableInfos.size();
        for (int i = 0; i < length; i++) {
            String columnName = columns.get(i);
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < size; j++) {
                TableInfo tableInfo = tableInfos.get(j);
                String columnName2 = tableInfo.getColumnName();
                if (columnName.equals(columnName2)) {
                    //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                    if (StringUtils.isNotEmpty(tableInfo.getTableFieldName())) {
                        builder.append(SqlParams.SQL_OPEN_PAREN).append(tableInfo.getTableAlias().toUpperCase())
                                .append(SqlParams.SQL_POINT).append(ReflexUtil.humpToUnderline(tableInfo.getTableFieldName()))
                                .append(SqlParams.SQL_CLOSE_PAREN).append(columnName);
                    } else {
                        builder.append(tableInfo.getTableAlias().toUpperCase()).append(SqlParams.SQL_POINT).append(ReflexUtil.humpToUnderline(columnName2));
                    }
                    columns.set(i, builder.toString());
                    break;
                }
                if (j == size - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, " ：" + columnName);
                }

            }
        }
        return columns;
    }

    public static List<String> addColumns(List<TableInfo> tableInfos) {

        List<String> collect = tableInfos.parallelStream().map(TableInfo::getColumnName).collect(Collectors.toList());
        collect.removeAll(Arrays.asList(forbidColumns));
        return collect;
    }

    /**
     * @param columns    查询结果返回列
     * @param tableInfos 表间关系数据
     * @return
     */
    public static List<String> splitColumns(List<String> columns, List<TableInfo> tableInfos) {
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        if (tableInfos == null || tableInfos.size() == 0) {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "params error : tableInfos is not null or empty");
        }
        if (columns == null || columns.size() == 0) {
            columns = addColumns(tableInfos);
        }
        int length = columns.size();
        int size = tableInfos.size();
        for (int i = 0; i < length; i++) {
            String columnName = columns.get(i);
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < size; j++) {
                TableInfo tableInfo = tableInfos.get(j);
                String columnName2 = tableInfo.getColumnName();
                if (columnName.equals(columnName2)) {
                    String filedSql = tableInfo.getFiledSql();
                    if (StringUtils.isNotBlank(filedSql)) {
                        //当filedSql不为空时，直接拼接此内容到字段上
                        builder.append(SqlParams.SQL_OPEN_PAREN).append(filedSql)
                                .append(SqlParams.SQL_CLOSE_PAREN).append(ReflexUtil.humpToUnderline(columnName));
                    } else {
                        //当tableFieldName  不为空时，采用此值为数据库字段值（及columnName非数据库字段值的情况）
                        if (StringUtils.isNotEmpty(tableInfo.getTableFieldName())) {
                            builder.append(SqlParams.SQL_OPEN_PAREN).append(tableInfo.getTableAlias().toUpperCase())
                                    .append(SqlParams.SQL_POINT).append(ReflexUtil.humpToUnderline(tableInfo.getTableFieldName()))
                                    .append(SqlParams.SQL_CLOSE_PAREN).append(ReflexUtil.humpToUnderline(columnName));
                        } else {
                            builder.append(tableInfo.getTableAlias().toUpperCase()).append(SqlParams.SQL_POINT).append(ReflexUtil.humpToUnderline(columnName2));
                        }
                    }
                    columns.set(i, builder.toString());
                    break;
                }
                if (j == size - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, " ：" + columnName);
                }

            }
        }
        return columns;
    }

    /**
     * 获取表字段关系信息
     *
     * @param c 查询的表间关系，以及表字段映射，别名
     * @return
     */
    public static List<TableInfo> getTableInfo(Class c) {
        if (c == null || Object.class.equals(c)) {
            throw new CommonException(ErrorCode.NOT_NULL, "params is not null");
        }
        String tableRelations = "";
        //主表
        String mTableName = null;
        //主表别名
        String mTableAlias = null;
        Class<?> parentClass = c.getSuperclass();
        List<Field> allFields = new ArrayList<>();
        // 查询父类的属性
        if (null != parentClass && !Object.class.equals(parentClass)) {
            Field[] parentFields = parentClass.getDeclaredFields();
            if (null != parentFields && parentFields.length != 0) {
                Collections.addAll(allFields, parentFields);
            }
        }
        // 查询父类的属性 查询所有，不建议此操作
//        while (parentClass != null) {
//            if (Object.class.equals(parentClass)) {
//                break;
//            }
//            Field[] parentFields = parentClass.getDeclaredFields();
//            if (parentFields.length != 0) {
//                Collections.addAll(allFields, parentFields);
//            }
//            parentClass = parentClass.getSuperclass();
//        }
        //添加子类字段
        Field[] fields = c.getDeclaredFields();
        if (null != fields && fields.length != 0) {
            Collections.addAll(allFields, fields);
        }
        Annotation[] classAnnotations = c.getDeclaredAnnotations();
        if (classAnnotations != null && classAnnotations.length > 0) {
            for (int i = 0; i < classAnnotations.length; i++) {
                Annotation annotation = classAnnotations[i];
                if (annotation instanceof TableRelations) {
                    tableRelations = ((TableRelations) annotation).mRelation();
                    if (StringUtils.isEmpty(tableRelations)) {
                        throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "注解参数：{relation} 不能为空");
                    }
                    mTableAlias = ((TableRelations) annotation).mTableAlias();
                    mTableName = ((TableRelations) annotation).mTableName();
                    break;
                }
                if (i == classAnnotations.length - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, c.getName() + "需添加表间关系注解 TableRelations");
                }
            }
        } else {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, c.getName() + "需添加表间关系注解 TableRelations");
        }
        List<TableInfo> tableInfos = new ArrayList<>(12);
        for (Field field : allFields) {
            field.setAccessible(true);
            //得到成员变量的类型的类类型
            Class fieldType = field.getType();
            String typeName = fieldType.getName();
            //得到成员变量的名称
            String fieldName = field.getName();
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof TableFiledInfo) {
                    String tableName = ((TableFiledInfo) annotation).tableName();
                    String tableAlia = ((TableFiledInfo) annotation).tableAlias();
                    String tableFieldName = ((TableFiledInfo) annotation).filedName();
                    String filedSql = ((TableFiledInfo) annotation).filedSql();
                    String relation = ((TableFiledInfo) annotation).relation();
                    TableInfo tableInfo = new TableInfo();
                    if (StringUtils.isEmpty(tableAlia) && StringUtils.isEmpty(mTableAlias)) {
                        throw new CommonException(ErrorCode.PARAMS_GET_ERROR, "table alias 别名不能为空");
                    }
                    tableInfo.setTableName(StringUtils.isEmpty(tableName) ? mTableName : tableName)
                            .setTableAlias(StringUtils.isEmpty(tableAlia) ? mTableAlias : tableAlia)
                            .setTableFieldName(tableFieldName).setColumnName(fieldName)
                            .setAttrType(typeName)
                            .setFiledSql(filedSql)
                            .setRelation(relation);
                    tableInfos.add(tableInfo);
                }
            }
        }
        tableInfos = tableInfos.stream().distinct().collect(Collectors.toList());
        return tableInfos;
    }

    /**
     * 获取表间关系
     * 通过注解获取当前业务表间关系，此项为必填项
     *
     * @param c
     * @return
     */
    public static String getRelation(Class c) {
        Annotation[] classAnnotations = c.getDeclaredAnnotations();
        if (classAnnotations.length > 0) {
            for (int i = 0; i < classAnnotations.length; i++) {
                Annotation annotation = classAnnotations[i];
                if (annotation instanceof TableRelations) {
                    String tableRelations = ((TableRelations) annotation).mRelation();
                    if (StringUtils.isEmpty(tableRelations)) {
                        throw new CommonException(ErrorCode.PARAMS_GET_ERROR, c.getName() + "需添加表间关系参数");
                    }
                    return tableRelations;
                }
                if (i == classAnnotations.length - 1) {
                    throw new CommonException(ErrorCode.PARAMS_GET_ERROR, c.getName() + "需添加表间关系注解 TableRelations");
                }
            }
        } else {
            throw new CommonException(ErrorCode.PARAMS_GET_ERROR, c.getName() + "需添加表间关系注解 TableRelations");
        }
        return "";
    }

    public static String getRelation(String relation, List<String> columns, List<TableInfo> tableInfos) {
        if (columns == null || columns.size() == 0) {
            columns = addColumns(tableInfos);
        }
        StringBuffer buffer = new StringBuffer(relation);
        Set<String> relations = new HashSet<>(12);
        for (String column : columns) {
            List<TableInfo> collect = tableInfos.parallelStream().filter(r -> r.getColumnName().equals(column)).collect(Collectors.toList());
            if (collect.size() > 0) {
                relations.add(collect.get(0).getRelation());
            }
        }
        for (String s : relations) {
            buffer.append(" ").append(s);
        }
        return buffer.toString();
    }

    /**
     * 获取待转换数据格式字段
     *
     * @param c
     * @return
     */
    public static List<FormatSwitchInfo> getSwitchColumns(Class c) {
        if (c == null || Object.class.equals(c)) {
            throw new CommonException(ErrorCode.NOT_NULL, "params is not null");
        }
        List<Field> allFields = new ArrayList<>(12);
        //添加字段
        Field[] fields = c.getDeclaredFields();
        if (fields.length != 0) {
            Collections.addAll(allFields, fields);
        } else {
            return null;
        }
        List<FormatSwitchInfo> infos = new ArrayList<>(12);
        for (Field field : allFields) {
            field.setAccessible(true);
            //得到成员变量的类型的类类型
            Class fieldType = field.getType();
            String typeName = fieldType.getName();
            //得到成员变量的名称
            String fieldName = field.getName();
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof FormatSwitch) {
                    FormatSwitchInfo info = FormatSwitchInfo
                            .builder()
                            .filedName(fieldName)
                            .separation(((FormatSwitch) annotation).separation())
                            .type(((FormatSwitch) annotation).type())
                            .cls(((FormatSwitch) annotation).cls()).build();
                    infos.add(info);
                }
            }
        }

        return infos;
    }

    /**
     * 检查前端前端需要的字段中是否有需要进行转换的字段
     *
     * @param infos   所有需要转换数据格式的字段
     * @param columns 前端需要展示的字段
     * @return
     */
    public static List<FormatSwitchInfo> checkSwitchColumn(List<FormatSwitchInfo> infos, List<String> columns) {
        if (null == infos || null == columns || infos.size() == 0 || columns.size() == 0) {
            return null;
        }
        List<FormatSwitchInfo> result = new ArrayList<>(12);
        for (FormatSwitchInfo info : infos) {
            if (columns.parallelStream().anyMatch(r -> r.equals(info.getFiledName()))) {
                result.add(info);
            }
        }
        return result;
    }

    /**
     * 获取分页start条数
     *
     * @param curPage
     * @param limit
     * @return
     */
    public static Long getStartCount(Long curPage, Long limit) {
        if (curPage != null && limit != null) {
            return curPage < 1 ? 0 : (curPage - 1) * limit;
        }
        return null;
    }

    /**
     * 获取分页end条数
     *
     * @param curPage
     * @param limit
     * @return
     */
    public static Long getEndCount(Long curPage, Long limit) {


        return getEndCount(curPage, limit, DatabaseType.MYSQL);
    }

    public static Long getEndCount(Long curPage, Long limit, DatabaseType type) {
        Long end = 0L;
        switch (type) {
            case MYSQL:
                end = limit;
                break;
            case ORACLE:
                if (curPage != null && limit != null) {
                    end = curPage * limit;
                }
                break;
            default:
                break;
        }
        return end;
    }

    /**
     * Oracle 分页拼接sql
     *
     * @param sql
     * @param wrapper
     * @return
     */
    public static String splitPageOracle(String sql, CommonWrapper wrapper) {
        Long start = getStartCount(wrapper.getCurPage(), wrapper.getLimit());
        Long end = getEndCount(wrapper.getCurPage(), wrapper.getLimit(), DatabaseType.ORACLE);
        if (start != null && end != null && start < end) {
            sql = SqlParams.SQL_ORACLE_PAGING1 + sql + SqlParams.SQL_ORACLE_PAGING2 +
                    end + SqlParams.SQL_ORACLE_PAGING3 + start;
        }
        return sql;
    }

    /**
     * 获取完整查询语句
     *
     * @param relation 表间关系 SQL语句
     * @param whereSql where后条件SQL语句
     * @param type     数据库类型
     * @param wrapper  构造器（获取返回表字段，分页参数等）
     * @return
     */
    public static String getCompletedSQL(String relation, String whereSql, DatabaseType type, CommonWrapper wrapper) {
        if (StringUtils.isEmpty(relation)) {
            throw new CommonException("SQL拼接错误，参数 relation 不能为空");
        }
        if (StringUtils.isEmpty(whereSql)) {
            throw new CommonException("SQL拼接错误，参数 whereSql 不能为空");
        }
        if (type == null) {
            throw new CommonException("SQL拼接错误，参数 DatabaseType 不能为null");
        }
        if (wrapper == null) {
            throw new CommonException("SQL拼接错误，CommonWrapper 不能为null");
        }
        String columns = wrapper.getSqlSelect();
        String sqlParams = SqlParams.SQL_SELECT + columns + SqlParams.SQL_FROM + relation +
                SqlParams.SQL_WHERE + whereSql;
        switch (type) {
            case MYSQL:
                sqlParams = splitPage(sqlParams, wrapper);
                break;
            case ORACLE:
                sqlParams = splitPageOracle(sqlParams, wrapper);
                break;
            default:
                break;
        }
        return sqlParams;
    }

    /**
     * 获取查询结果集并封装返回数据格式
     *
     * @param baseMapper
     * @param params
     * @param c
     * @return
     */
    public static Result getResult(CommonMapper baseMapper, QueryOptions params, Class c) {
        return getResult(baseMapper, params, c, DatabaseType.MYSQL);
    }

    /**
     * 获取查询结果集并封装返回数据格式
     *
     * @param commonService
     * @param params
     * @param c
     * @return
     */
    public static Result getResult(CommonService commonService, QueryOptions params, Class c) {
        return getResult(commonService, params, c, DatabaseType.MYSQL);
    }

    /**
     * 获取查询返回结果集（map）
     *
     * @param baseMapper 当前业务mapper
     * @param params     查询参数
     * @param c          当前业务VO类
     * @param type       当前数据库类型（默认为Mysql）
     * @return
     */
    public static Result getResult(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        if (null == columns || columns.size() == 0) {
            columns = addColumns(tableInfos);
        }
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        Long total = baseMapper.commonQueryCount(whereSql, relation);
        List<Map<String, Object>> map = baseMapper.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));

        return new Result().success(new PageData<>(MapUtils.keysToCamelByList(map, checkSwitchColumn(getSwitchColumns(c), columns)), total, wrapper.getCurPage()));
    }

    /**
     * 获取查询返回结果集（map）
     *
     * @param commonService 当前业务service
     * @param params        查询参数
     * @param c             当前业务VO类
     * @param type          当前数据库类型（默认为Mysql）
     * @return
     */
    public static Result getResult(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        if (null == columns || columns.size() == 0) {
            columns = addColumns(tableInfos);
        }
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        Long total = commonService.commonQueryCount(whereSql, relation);
        List<Map<String, Object>> map = commonService.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));

        return new Result().success(new PageData<>(MapUtils.keysToCamelByList(map, checkSwitchColumn(getSwitchColumns(c), columns)), total, wrapper.getCurPage()));
    }


    /**
     * 获取查询返回分页结果集（map）
     *
     * @param baseMapper 当前业务mapper
     * @param params     查询参数
     * @param c          当前业务VO类
     * @param type       当前数据库类型（默认为Mysql）
     * @return 返回结果分页结果集
     */
    public static PageData getPageResult(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        Long total = baseMapper.commonQueryCount(whereSql, relation);
        List<Map<String, Object>> map = baseMapper.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));
        return new PageData<>(MapUtils.keysToCamelByList(map), total, wrapper.getCurPage());
    }

    /**
     * 获取查询返回分页结果集（map）
     *
     * @param commonService 当前业务service
     * @param params        查询参数
     * @param c             当前业务VO类
     * @param type          当前数据库类型（默认为Mysql）
     * @return 返回结果分页结果集
     */
    public static PageData getPageResult(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        Long total = commonService.commonQueryCount(whereSql, relation);
        List<Map<String, Object>> map = commonService.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));
        return new PageData<>(MapUtils.keysToCamelByList(map), total, wrapper.getCurPage());
    }

    /**
     * 获取查询结果集（map）
     *
     * @param baseMapper 当前业务mapper
     * @param params     查询参数
     * @param c          当前业务VO类
     * @param type       当前数据库类型（默认为Mysql）
     * @return 返回结果集
     */
    public static List<Map<String, Object>> getListResult(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<Map<String, Object>> map = baseMapper.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));
        return MapUtils.keysToCamelByList(map);
    }

    /**
     * 获取查询结果集（map）
     *
     * @param commonService 当前业务service
     * @param params        查询参数
     * @param c             当前业务VO类
     * @param type          当前数据库类型（默认为Mysql）
     * @return 返回结果集
     */
    public static List<Map<String, Object>> getListResult(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        log.info("SysUser common query News Params ：[{}] , database type [{}]", params.toString(), type);
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<Map<String, Object>> map = commonService.commonQueryByParams(getCompletedSQL(relation, whereSql, type, wrapper));
        return MapUtils.keysToCamelByList(map);
    }


    public static Result getObjectResult(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<Object> list = baseMapper.commonQueryReturnObject(wrapper.getSqlSelect(), whereSql, relation);
        return new Result().success(list);
    }

    public static Result getObjectResult(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<Object> list = commonService.commonQueryReturnObject(wrapper.getSqlSelect(), whereSql, relation);
        return new Result().success(list);
    }

    public static List<Object> getObjectList(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        return baseMapper.commonQueryReturnObject(wrapper.getSqlSelect(), whereSql, relation);
    }


    public List<T> getEntityList(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper<T> wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<T> list = baseMapper.commonQueryReturnEntity(wrapper.getSqlSelect(), whereSql, relation);
        return list;
    }

    public List<T> getEntityList(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
        List<String> columns = params.getColumns();
        //利用反射原理读出当前查询的表间关系，以及表字段映射，别名
        List<TableInfo> tableInfos = getTableInfo(c);
        CommonWrapper<T> wrapper = new CommonWrapper(splitOptions(params, tableInfos), type);
        String relation = getRelation(getRelation(c), columns, tableInfos);
        String whereSql = splitSql(wrapper);
        List<T> list = commonService.commonQueryReturnEntity(wrapper.getSqlSelect(), whereSql, relation);
        return list;
    }

}
