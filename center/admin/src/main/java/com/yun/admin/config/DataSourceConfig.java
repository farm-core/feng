package com.yun.admin.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.yun.idb.annotation.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 * 有几个就配置几个--目前上限为三个，如果需要可继续设置
 */
//@Configuration
public class DataSourceConfig {
    /**
     * 数据源1
     */
    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource Db1() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 数据源2
     */
    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource Db2() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 数据源3
     */
    @Bean(name = "db3")
    @ConfigurationProperties(prefix = "spring.datasource.db3")
    public DataSource Db3() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 数据源切换: 通过AOP在不同数据源之间动态切换
     */
    @Primary
    @Bean
    public DataSource dynamicDataSource() {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(Db1());
        //配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("db1", Db1());
        dsMap.put("db2", Db2());
        dsMap.put("db3", Db3());

        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    /**
     * 配置@Transactional注解事务
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
