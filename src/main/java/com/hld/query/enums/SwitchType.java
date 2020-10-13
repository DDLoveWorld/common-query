package com.hld.query.enums;

/**
 * @author huald
 * @date 2020/10/13
 */
public enum SwitchType {

    /**
     * 字符串转数组，采用分隔符
     * 例： 1,2,3 -> [1,2,3]
     */
    STR_TO_ARRAY,

    /**
     * 字符串数据转数组
     * 例： '[1,2,3]' -> [1,2,3]
     */
    JSON_TO_ARRAY,

    /**
     * 字符串转对象
     * 例:  '{}' -> {}
     */
    JSON_TO_OBJ,

    /**
     * 字符串对象数据转集合
     * 例: '[{},{}]' -> [{},{}]
     */
    JSON_TO_LIST;

}
