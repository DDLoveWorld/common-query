package com.hld.query.annotations;

import com.hld.query.enums.SensitiveStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏字段标记
 * 脱敏功能的实现借鉴了CSDN账号：【码农小胖哥】的思想，具体博文如下：
 * https://blog.csdn.net/qq_35067322/article/details/107925609
 *
 * @author huald
 * @email 1185291074@qq.com
 * @date 2021/1/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sensitive {
    SensitiveStrategy strategy();
}
