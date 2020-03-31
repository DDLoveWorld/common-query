package com.hld.query.enums;


import com.hld.query.util.StringUtils;

/**
 * 字段属性值
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @date 2019/9/19
 */
public enum AttrType {
    STRING("java.lang.String"),
    INT("int"),
    INTEGER("java.lang.Integer"),
    BASE_LONG("long"),
    LONG("java.lang.Long"),
    BASE_DOUBLE("double"),
    DOUBLE("java.lang.Double"),
    BASE_FLOAT("float"),
    FLOAT("java.lang.Float"),
    CHAR("char"),
    CHARACTER("java.lang.Character"),
    LOCAL_DATE_TIME("java.time.LocalDateTime"),
    LOCAL_DATE("java.time.LocalDate"),
    DATE("java.util.Date");


    private String name;

    AttrType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttrType(String attrType) {
        if (StringUtils.isBlank(attrType)) {
            return false;
        }
        for (AttrType type : AttrType.values()) {
            if (type.getName().equals(attrType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStringType(String attrType) {
        if (StringUtils.isBlank(attrType)) {
            return false;
        }
        if (AttrType.STRING.getName().equals(attrType)) {
            return true;
        }
        return false;
    }

    public boolean isDateType(String attrType) {
        if (StringUtils.isBlank(attrType)) {
            return false;
        }
        if (AttrType.LOCAL_DATE_TIME.getName().equals(attrType)
                || AttrType.LOCAL_DATE.getName().equals(attrType)
                || AttrType.DATE.getName().equals(attrType)) {
            return true;
        }
        return false;
    }


}
