package com.flyingideal.sharding.readwrite.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.flyingideal.sharding.core.bean.DataSourceProperties;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author yanchao
 * @date 2019-10-30 16:24
 */
@Configuration
public class ReadWriteSeparationDataSourceConfig {

    private DataSource masterDataSource() {
        DataSourceProperties masterDataSourceProperties = new DataSourceProperties();
        masterDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSourceProperties.setUrl("jdbc:mysql://localhost:3360/test");
        masterDataSourceProperties.setUsername("root");
        masterDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(masterDataSourceProperties);
    }

    private DataSource slaveDataSource() {
        DataSourceProperties slaveDataSourceProperties = new DataSourceProperties();
        slaveDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDataSourceProperties.setUrl("jdbc:mysql://localhost:3361/test");
        slaveDataSourceProperties.setUsername("root");
        slaveDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(slaveDataSourceProperties);
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
    @Primary
    public DataSource shardingMasterSlaveDataSource() throws SQLException {
        String masterDataSourceName = "ds_master";
        // 现在只有一个slave，所以只配了一个 slave 名称
        String slaveDataSourceName = "ds_slave";
        Map<String, DataSource> dataSourceMap = new HashMap<>(16);
        dataSourceMap.put(masterDataSourceName, masterDataSource());
        dataSourceMap.put(slaveDataSourceName, slaveDataSource());

        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration =
                new MasterSlaveRuleConfiguration("ds_master_slave", masterDataSourceName, Collections.singleton(slaveDataSourceName));
        Properties properties = new Properties();
        // 开启日志打印，其他参数配置请参考 https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/configuration/props/
        properties.setProperty("sql.show", "true");
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfiguration, properties);
    }
}
