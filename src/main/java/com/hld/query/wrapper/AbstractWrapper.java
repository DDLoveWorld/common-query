package com.hld.query.wrapper;

import com.hld.query.interfaces.IJoin;
import com.hld.query.interfaces.ISql;
import com.hld.query.util.StringUtils;

public abstract class AbstractWrapper implements ISql {

    @Override
    public String addSpace(String value) {
        return IJoin.SPACE + value + IJoin.SPACE;
    }

    @Override
    public abstract boolean isFirstAdd();

    @Override
    public String splitComma() {
        return isFirstAdd() ? IJoin.SPACE : IJoin.COMMA + IJoin.SPACE;
    }

    protected String addValue(Object value) {
        if (value == null) return "";
        return "'" + value.toString() + "'";
    }

    @Override
    public String getSql() {
        return null;
    }

    protected String replaceSqlVariable(String sql, Object... values) {
        if (StringUtils.isNotBlank(sql) && values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                String str = "{" + i + "}";
                sql = sql.replace(str, values[i].toString());
            }
        }
        return sql;
    }
}
