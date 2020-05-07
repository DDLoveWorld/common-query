package com.hld.query.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 扩展性强查询方法
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @description: 扩展性强查询方法
 * @date 2019/8/26
 */
public interface CommonMapper<T> extends BaseMapper<T> {
    /**
     * 多表通用查询
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 查询结果集
     */
    @Select("select ${columns} from ${relation} where ${conditions}")
    List<Map> commonQuery(@Param("columns") String columns, @Param("conditions") String conditions, @Param("relation") String relation);

    /**
     * 自定义查询
     * 所有拼接参数由代码生成
     *
     * @param sqlParams 完整版sql语句
     * @return 查询结果集
     */
    @Select("${sqlParams}")
    List<Map> commonQueryByParams(@Param("sqlParams") String sqlParams);

    /**
     * 获取总条数
     *
     * @param conditions 查询条件
     * @param relation   表关系
     * @return 返回结果总条数
     */
    @Select("select count(*) from ${relation} where ${conditions}")
    Long commonQueryCount(@Param("conditions") String conditions, @Param("relation") String relation);

    /**
     * 多表通用查询 返回entity
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 查询结果实体
     */
    @Select("select ${columns} from ${relation} where ${conditions}")
    List<T> commonQueryReturnEntity(@Param("columns") String columns, @Param("conditions") String conditions, @Param("relation") String relation);

    /**
     * 多表通用查询 返回Object
     *
     * @param columns    需要返回的字段
     * @param conditions 需要拼接的where条件 以及分页SQL
     * @param relation   表间关系
     * @return 查询结果实体
     */
    @Select("select ${columns} from ${relation} where ${conditions}")
    List<Object> commonQueryReturnObject(@Param("columns") String columns, @Param("conditions") String conditions, @Param("relation") String relation);

}
