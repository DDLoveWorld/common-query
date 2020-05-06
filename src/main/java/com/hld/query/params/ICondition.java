package com.hld.query.params;

/**
 * @author huald
 * @email 869701411@qq.com
 * @version 1.0.0
 * @Description: 查询条件枚举值
 * @date 2019年1月11日 下午2:13:41
 */

public enum ICondition {
    EQUAL("=", 0),
    UN_EQUAL("<>", 1),
    IN("关联", 2),
    NO_IN("不关联", 3),
    LIKE("LIKE", 4),
    NO_LIKE("NOT LIKE", 5),
    GE(">=", 6),
    LE("<=", 7),
    LT("<", 8),
    GT(">", 9),
    NOT_NULL("NOT_NULL", 10),
    NULL("NULL", 11),
    BETWEEN("BETWEEN", 12),
    NOT_BETWEEN("NOT_BETWEEN", 13),
    LEFT_LIKE("LEFT_LIKE", 14),
    RIGHT_LIKE("RIGHT_LIKE", 15);


    private String name;
    private Integer type;


    private ICondition(String name, Integer type) {

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
