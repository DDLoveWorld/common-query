package com.hld.query.interfaces;

import com.hld.query.wrapper.OrderBySqlWrapper;

public interface IOrderBySql<T extends OrderBySqlWrapper> {


    T desc( String column);

    T asc( String column);

    T desc(boolean condition,  String column);

    T asc(boolean condition,  String column);

    T desc( String... columns);

    T asc( String... columns);

    T desc(boolean condition,  String... columns);

    T asc(boolean condition,  String... columns);

}
