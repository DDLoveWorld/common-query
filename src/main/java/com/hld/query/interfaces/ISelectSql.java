package com.hld.query.interfaces;

import com.hld.query.wrapper.SqlWrapper;

public interface ISelectSql<T extends SqlWrapper> {

    String getSelectColumn();

    T select(String column);

    T select(String... columns);

}
