package com.hld.query.interfaces;

public interface ISql {

    String getSql();

    String addSpace(String val);

    String splitComma();

    boolean isFirstAdd();
}
