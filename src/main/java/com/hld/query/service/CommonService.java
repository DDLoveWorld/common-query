package com.hld.query.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 扩展mybatis-plus基础Service接口
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @date 2019/8/26
 */
public interface CommonService<T> extends IService<T> {
    /**
     * 多表通用查询
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return
     */
    List<Map> commonQuery(String columns, String conditions, String relation);

    /**
     * sql
     *
     * @param sqlParams 完整版SQL查询语句
     * @return
     */
    List<Map> commonQueryByParams(String sqlParams);

    /**
     * 获取总条数
     *
     * @param conditions
     * @param relation
     * @return
     */
    Long commonQueryCount(String conditions, String relation);

    /**
     * 多表通用查询 返回entity
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return
     */
    List<T> commonQueryReturnEntity(String columns, String conditions, String relation);

}
