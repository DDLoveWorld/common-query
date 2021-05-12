package com.hld.query.wrapper;

import com.hld.query.interfaces.IGroupBy;
import com.hld.query.interfaces.IJoin;
import com.hld.query.util.StringUtils;

public class GroupByWrapper<T extends GroupByWrapper> extends AbstractWrapper implements IGroupBy<T> {

    protected String groupBySql = IJoin.GROUP_BY;

    public GroupByWrapper() {
    }


    @Override
    public T groupBy(String column) {
        return groupBy(true, column);
    }

    @Override
    public T groupBy(boolean condition, String column) {
        if (condition && StringUtils.isNotBlank(column))
            this.groupBySql += splitComma() + column;
        return (T) this;
    }

    @Override
    public T groupBy(String... columns) {
        return groupBy(true, columns);
    }

    @Override
    public T groupBy(boolean condition, String... columns) {
        if (condition && columns != null && columns.length > 0)
            for (String column : columns) {
                groupBy(true, column);
            }
        return (T) this;
    }

    @Override
    public boolean isFirstAdd() {
        return IJoin.GROUP_BY.equals(this.groupBySql);
    }

    @Override
    public String getSql() {
        return this.groupBySql;
    }
}
