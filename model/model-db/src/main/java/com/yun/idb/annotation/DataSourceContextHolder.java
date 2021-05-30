package com.yun.idb.annotation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    //设置数据源名称
    public static void setDataSource(String dataSource) {
        log.info("切换到{" + dataSource + "}数据源");
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    //清除数据源
    public static void clearDataSource() {
        contextHolder.remove();
    }
}
