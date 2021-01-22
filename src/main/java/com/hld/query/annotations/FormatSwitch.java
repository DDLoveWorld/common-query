package com.hld.query.annotations;

import com.hld.query.enums.SwitchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解用于返回数据时进行数据结构转换，定义
 *
 * @author huald
 * @date 2020/10/13
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatSwitch {
    /**
     * 分隔符
     * 例： 1,2,3  采用逗号分隔
     * 默认分隔符 ,
     * 分隔符，当type = STR_TO_ARRAY 时使用生效
     *
     * @return
     */
    String separation() default ",";

    /**
     * 转换类型
     *
     * @return
     */
    SwitchType type() default SwitchType.STR_TO_ARRAY;

    /**
     * 需要转换的对象类型
     * 当 type = JSON_TO_OBJ,JSON_TO_LIST 生效
     */
    Class<?> cls() default Object.class;

    /**
     * 当需要处理的值是null或者""时，是否进行处理为[]
     *
     * @return
     */
    boolean handleEmptyAndNull() default true;
}
