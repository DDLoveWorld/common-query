package com.hld.query.wrapper;

import com.hld.query.interfaces.IJoin;
import com.hld.query.interfaces.IOrderBySql;
import com.hld.query.util.StringUtils;

public class OrderBySqlWrapper<T extends OrderBySqlWrapper> extends AbstractWrapper implements IOrderBySql<T> {

    protected String orderBySql = IJoin.ORDER_BY;

    @Override
    public T desc(String column) {
        return desc(true, column);
    }

    @Override
    public T asc(String column) {
        return asc(true, column);
    }

    @Override
    public T desc(boolean condition, String column) {
        if (condition && StringUtils.isNotBlank(column))
            this.orderBySql += splitComma() + column + addSpace(IJoin.DESC);
        return (T) this;
    }

    @Override
    public T asc(boolean condition, String column) {
        if (condition && StringUtils.isNotBlank(column))
            this.orderBySql += splitComma() + column + addSpace(IJoin.ASC);
        return (T) this;
    }

    @Override
    public T desc(String... columns) {
        return asc(true, columns);
    }

    @Override
    public T asc(String... columns) {
        return asc(true, columns);
    }

    @Override
    public T desc(boolean condition, String... columns) {
        if (condition && columns != null && columns.length > 0)
            for (String column : columns) {
                desc(column);
            }
        return (T) this;
    }

    @Override
    public T asc(boolean condition, String... columns) {
        if (condition && columns != null && columns.length > 0)
            for (String column : columns) {
                asc(column);
            }
        return (T) this;
    }


    @Override
    public boolean isFirstAdd() {
        return IJoin.ORDER_BY.equals(this.orderBySql);
    }

    @Override
    public String getSql() {
        return this.orderBySql;
    }
}
