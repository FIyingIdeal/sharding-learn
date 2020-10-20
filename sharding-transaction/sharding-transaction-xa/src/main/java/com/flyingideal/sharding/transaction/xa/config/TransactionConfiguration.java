package com.flyingideal.sharding.transaction.xa.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author yanchao
 * @date 2020-07-16 11:41
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {

    @Bean("shardingDataSource")
    public DataSource dataSource() {
        DataSourceProperties masterDataSourceProperties = new DataSourceProperties();
        masterDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSourceProperties.setUrl("jdbc:mysql://localhost:3360/test");
        masterDataSourceProperties.setUsername("root");
        masterDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(masterDataSourceProperties);
    }

    private DataSource getNormalDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager txManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
