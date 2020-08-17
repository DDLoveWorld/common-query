package com.hld.query.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author huald
 * @date 2019/8/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
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

    /**
     * 列拼接SQL
     */
    private String filedSql;

    /**
     * 标识当前列是否是查询列
     */
    private boolean queryFlag;
}
