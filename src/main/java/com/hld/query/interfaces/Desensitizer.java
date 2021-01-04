package com.hld.query.interfaces;

import java.util.function.Function;

/**
 * 脱敏策略函数
 * 继承Function，实现lambda方式
 * 脱敏功能的实现借鉴了CSDN账号：【码农小胖哥】的思想，具体博文如下：
 * https://blog.csdn.net/qq_35067322/article/details/107925609
 *
 * @author huald
 * @date 2021/1/4
 */
public interface Desensitizer extends Function<String, String> {
}
