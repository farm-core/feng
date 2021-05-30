package com.yun.bean.enums;

@SuppressWarnings("ALL")
public enum DataSourceEnum {
    /**
     * @Description 数据库1
     */
    DB1("db1"),
    /**
     * @Description 数据库2
     */
    DB2("db2"),
    /**
     * @Description 数据库3
     */
    DB3("db3");
    /**
     * @Description 数据库名称
     */
    private String value;

    DataSourceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
