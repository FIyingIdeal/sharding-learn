package com.flyingideal.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yanchao
 * @date 2019-10-28 18:27
 */
@Configuration
public class DataSourceConfig {


    private DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
        dataSource.setMaxActive(10);
        dataSource.setInitialSize(2);
        return dataSource;
    }

    @Bean
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>(16);
        DataSource realDataSource = dataSource();
        dataSourceMap.put("ds0", realDataSource);

        TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration("sharding_user", "ds0.sharding_user_${0..1}");
        userTableRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("id", "sharding_user_${id % 2}"));
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(userTableRuleConfig);

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, new Properties());
        return dataSource;
    }
}
