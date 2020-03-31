package com.hld.query.params;

/**
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @description: SQL 拼接关键字
 * @date 2019/1/10
 */

public enum FilterType {
    /**
     * 默认and
     */
    DEFAULT("默认and", 0),
    /**
     * 并且
     */
    AND("并且", 1),
    /**
     * 或者
     */
    OR("或者", 2),
    /**
     * 并且换行
     */
    AND_NEW("并且换行", 3),
    /**
     * 或者换行
     */
    OR_NEW("或者换行", 4);

    private String name;
    private Integer type;


    FilterType(String name, Integer type) {

        this.name = name;
        this.type = type;
    }


    public String getName() {

        return name;
    }


    public void setName(String name) {

        this.name = name;
    }


    public Integer getType() {

        return type;
    }


    public void setType(Integer type) {

        this.type = type;
    }

}
