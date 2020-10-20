package com.flyingideal.sharding.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author yanchao
 * @date 2019-10-29 18:53
 */
//@Configuration
@Deprecated
public class DataSourceConfig {

    private DataSource getNormalDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        return druidDataSource;
    }

    //@Bean("masterDataSource")
    public DataSource masterDataSource() {
        DataSourceProperties masterDataSourceProperties = new DataSourceProperties();
        masterDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSourceProperties.setUrl("jdbc:mysql://localhost:3360/test");
        masterDataSourceProperties.setUsername("root");
        masterDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(masterDataSourceProperties);
    }

    //@Bean("slaveDataSource")
    public DataSource slaveDataSource() {
        DataSourceProperties slaveDataSourceProperties = new DataSourceProperties();
        slaveDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDataSourceProperties.setUrl("jdbc:mysql://localhost:3361/test");
        slaveDataSourceProperties.setUsername("root");
        slaveDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(slaveDataSourceProperties);
    }

    @Data
    private static class DataSourceProperties {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }
}
