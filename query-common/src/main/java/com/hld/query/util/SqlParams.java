package com.hld.query.util;

/**
 * sql 拼接参数
 *
 * @author huald
 * @date 2019/9/17
 */
public interface SqlParams {
    String SQL_POINT = ".";
    String SQL_OPEN_PAREN = "(";
    String SQL_CLOSE_PAREN = ")";
    String SQL_NO_WHERE_SQL = " 1=1 ";
    String SQL_LIMIT = " limit ";
    String SQL_COMMA = " , ";
    String SQL_SELECT = "SELECT ";
    String SQL_FROM = " FROM ";
    String SQL_WHERE = " WHERE ";
    String SQL_AND = " AND ";

    String SQL_SINGLE_QUOTE = "'";

    String SQL_ORACLE_PAGING1 = "SELECT *  FROM (SELECT tt.*, ROWNUM AS rowno FROM (";
    String SQL_ORACLE_PAGING2 = " )tt WHERE ROWNUM <= ";
    String SQL_ORACLE_PAGING3 = " ) table_alias WHERE table_alias.rowno > ";
}
