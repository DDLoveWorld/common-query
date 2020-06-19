package com.hld.query.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解用于在自定义查询中配置各个字段所属表、表别名和entity中column name对应数据库字段是否一致
 *
 * @author huald
 * @date 2019/8/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFiledInfo {
    /**
     * 表名（默认为空，读取类上注解TableRelations 参数mTableName
     * 若与mTableName参数值相同可以不再二次声明）
     *
     * @return
     */
    String tableName() default "";

    /**
     * 表名（默认为空，读取类上注解TableRelations mTableAlias
     * 若与mTableAlias参数值相同可以不再二次声明）
     *
     * @return
     */
    String tableAlias() default "";

    /**
     * 当前字段命名规范非数据库字段转为驼峰格式命名的情况下，将此参数赋值数据库字段名称
     *
     * @return
     */
    String filedName() default "";

    /**
     * 字段上直接拼接查询SQL 示例：（select name form sys_dept id = 2）deptName
     * 此项不为空时，tableName、tableAlias、filedName属性失效
     *
     * @return
     */
    String filedSql() default "";

    /**
     * 此字段与主表的表间关系，可以在需要此字段返回的时候进行表关联，减少性能开销
     *
     * @return
     */
    String relation() default "";


}
