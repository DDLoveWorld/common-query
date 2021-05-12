package com.hld.query.interfaces;

import com.hld.query.wrapper.SqlWrapper;

public interface IJoin<T extends SqlWrapper> {

    String AND = "AND";

    String OR = "OR";

    String AND_NEW_START = "AND (";

    String AND_NEW_END = ") ";

    String OR_NEW_START = "OR (";

    String OR_NEW_END = ") ";

    String IN = "IN";

    String NOT = "NOT";

    String LIKE = "LIKE";

    String EQ = "=";

    String NE = "<>";

    String GT = ">";

    String GE = ">=";

    String LT = "<";

    String LE = "<=";

    String IS_NULL = "IS NULL";

    String IS_NOT_NULL = "IS NOT NULL";

    String GROUP_BY = "GROUP BY";

    String HAVING = "HAVING";

    String ORDER_BY = "ORDER BY";

    String EXISTS = "EXISTS";

    String BETWEEN = "BETWEEN";

    String ASC = "ASC";

    String DESC = "DESC";

    String SPACE = " ";

    String COMMA = ",";

    String LEFT_PARENTHESES = "(";

    String RIGHT_PARENTHESES = ")";

    String WHERE = "WHERE";


    T and();

    T or();

    T andNew();

    T orNew();

    T andNew(String childSql);

    T orNew(String childSql);

    T andNew(T t);

    T orNew(T t);

}
