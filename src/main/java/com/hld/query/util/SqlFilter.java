package com.hld.query.util;


import com.hld.query.exception.CommonException;
import com.hld.query.exception.ErrorCode;

import java.util.List;

/**
 * SQL过滤
 *
 * @author hua huald@ibuaa.com
 */
public class SqlFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        String strLower = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (strLower.contains(keyword)) {
                throw new CommonException(ErrorCode.INVALID_SYMBOL, keyword);
            }
        }

        return str;
    }

    /**
     * @param strings
     * @return
     */
    public static List<String> sqlInject(List<String> strings) {
        if (strings == null || strings.size() == 0) {
            return strings;
        }
        int length = strings.size();
        for (int i = 0; i < length; i++) {
            strings.set(i, sqlInject(strings.get(i)));
        }
        return strings;
    }

    /**
     * @param objects
     * @return
     */
    public static List<Object> sqlInjectObject(List<Object> objects) {
        if (objects == null || objects.size() == 0) {
            return objects;
        }
        int length = objects.size();
        for (int i = 0; i < length; i++) {
            String s = objects.get(i).toString();
            if (!StringUtils.isNumber(s)) {
                objects.set(i, sqlInject(s));
            }
        }
        return objects;
    }
}
