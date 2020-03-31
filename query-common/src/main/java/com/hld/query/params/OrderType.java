package com.hld.query.params;

/**
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @Description: 排序类型
 * @date 2019/7/22
 */
public enum OrderType {
    /**
     * 正序
     */
    ASC("ASC"),
    /**
     * 倒序
     */
    DESC("DESC");
    private String name;

    OrderType(String type) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
