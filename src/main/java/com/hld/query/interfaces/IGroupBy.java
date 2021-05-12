package com.hld.query.interfaces;

import com.hld.query.wrapper.GroupByWrapper;

public interface IGroupBy<T extends GroupByWrapper> {

    T groupBy( String column);

    T groupBy(boolean condition,  String column);

    T groupBy( String... columns);

    T groupBy(boolean condition,  String... columns);



}
