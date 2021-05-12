package com.hld.query.service.impl;

import com.hld.query.mapper.CommonMapper;

import java.util.List;
import java.util.Map;

/**
 * 自定义 server实现类
 *
 * @author huald
 * @version 2.0.0
 * @email 869701411@qq.com
 * @date 2021/05/15
 */
public class CommonServiceImpl<M extends CommonMapper<T>, T> {

    protected M baseMapper;

    /**
     * 多表通用查询
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 返回map结果集
     */
    public List<Map> commonQuery(String columns, String conditions, String relation) {
        return baseMapper.commonQuery(columns, conditions, relation);
    }

    /**
     * @param sqlParams
     * @return 返回map结果集
     */
    public List<Map> commonQueryByParams(String sqlParams) {
        return baseMapper.commonQueryByParams(sqlParams);
    }


    /**
     * 获取总条数
     *
     * @param conditions
     * @param relation
     * @return 返回查询结果总条数
     */
    public Long commonQueryCount(String conditions, String relation) {
        return baseMapper.commonQueryCount(conditions, relation);
    }


    /**
     * 多表通用查询 返回entity
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 返回实体结果集
     */
    public List<T> commonQueryReturnEntity(String columns, String conditions, String relation) {
        return baseMapper.commonQueryReturnEntity(columns, conditions, relation);
    }

    /**
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 返回实体结果集
     */
    public List<Object> commonQueryReturnObject(String columns, String conditions, String relation) {
        return baseMapper.commonQueryReturnObject(columns, conditions, relation);
    }
}
