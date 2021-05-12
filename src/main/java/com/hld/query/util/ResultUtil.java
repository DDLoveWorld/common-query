package com.hld.query.util;

import com.hld.query.enums.DatabaseType;
import com.hld.query.mapper.CommonMapper;
import com.hld.query.params.QueryOptions;
import com.hld.query.params.TableInfo;
import com.hld.query.service.CommonService;
import com.hld.query.wrapper.CommonWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class ResultUtil<T> extends BaseQueryUtils {


    /**
     * 拼接mysql分页参数
     *
     * @param sql
     * @param wrapper
     * @return
     */
    public static String splitPage(String sql, CommonWrapper wrapper) {
        return splitPage(sql, wrapper.getCurPage(), wrapper.getLimit());
    }


    /**
     * Oracle 分页拼接sql
     *
     * @param sql
     * @param wrapper
     * @return
     */
    public static String splitPageOracle(String sql, CommonWrapper wrapper) {
        return splitPageOracle(sql, wrapper.getCurPage(), wrapper.getLimit());
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
        return getCompletedSQL(relation, whereSql, type, wrapper.getColumnSql(), wrapper.getCurPage(), wrapper.getLimit());
    }

    public static String splitWhereSql(CommonWrapper wrapper) {
        return splitWhereSql(wrapper.getFirstSql(), wrapper.getSql(), wrapper.getOrderBys(), wrapper.getFilters(), wrapper.getGroupBys());
    }

    public static String splitSql(CommonWrapper wrapper) {
        String sql = "";
        sql = splitWhereSql(wrapper);
        return sql;
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
    public static PageData getPageMapResult(CommonMapper baseMapper, QueryOptions params, Class c, DatabaseType type) {
        if (type == null) {
            type = DatabaseType.MYSQL;
        }
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
    public static PageData getPageMapResult(CommonService commonService, QueryOptions params, Class c, DatabaseType type) {
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
        return baseMapper.commonQueryReturnObject(wrapper.getColumnSql(), whereSql, relation);
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
        List<T> list = baseMapper.commonQueryReturnEntity(wrapper.getColumnSql(), whereSql, relation);
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
        List<T> list = commonService.commonQueryReturnEntity(wrapper.getColumnSql(), whereSql, relation);
        return list;
    }


}
