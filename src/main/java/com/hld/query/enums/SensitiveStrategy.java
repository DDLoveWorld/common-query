package com.hld.query.enums;

import com.hld.query.interfaces.Desensitizer;
import lombok.Getter;

/**
 * 具体脱敏策略的实现
 * 脱敏功能的实现借鉴了CSDN账号：【码农小胖哥】的思想，具体博文如下：
 * https://blog.csdn.net/qq_35067322/article/details/107925609
 *
 * @author huald
 * @date 2021/1/4
 */
public enum SensitiveStrategy {
    /**
     * Username sensitive strategy.
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
    /**
     * Id card sensitive type.
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    /**
     * Phone sensitive type.
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * Address sensitive type.
     */
    ADDRESS(s -> s.replaceAll("(\\S{8})\\S{4}(\\S*)\\S{4}", "$1****$2****"));


    @Getter
    private final Desensitizer desensitizer;

    SensitiveStrategy(Desensitizer desensitizer) {
        this.desensitizer = desensitizer;
    }
}
