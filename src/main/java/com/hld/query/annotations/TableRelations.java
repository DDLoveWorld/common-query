package com.hld.query.annotations;

import java.lang.annotation.*;

/**
 * 表关系注解
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @date 2019/8/23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableRelations {

    /**
     * 表关系，例：sys_user T left join sys_dept A on A.id = T.dept_id
     *
     * @return
     */
    String mRelation() default "";

    /**
     * 表关系中主表：sys_user
     *
     * @return
     */
    String mTableName() default "";

    /**
     * 主表别名默认T
     *
     * @return
     */
    String mTableAlias() default "T";
}
