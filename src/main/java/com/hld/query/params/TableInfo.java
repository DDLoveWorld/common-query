package com.hld.query.params;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huald
 * @date 2019/8/23
 */
@Data
@Accessors(chain = true)
public class TableInfo {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表 别名
     */
    private String tableAlias;
    /**
     * 表 真实字段
     */
    private String tableFieldName;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * entity 字段名称
     */
    private String columnName;

    /**
     * 表间关系
     */
    private String relation;
}
