package com.flyingideal.sharding.readwrite.table.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.flyingideal.sharding.core.bean.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.core.metadata.table.ShardingTableMetaData;
import org.apache.shardingsphere.core.optimize.sharding.segment.insert.ShardingInsertColumns;
import org.apache.shardingsphere.core.parse.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yanchao
 * @date 2019-10-31 19:49
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(getUserTableRuleConfiguration());
        shardingRuleConfiguration.setMasterSlaveRuleConfigs(getMasterSlaveConfigurations());
        Properties properties = new Properties();
        properties.setProperty("sql.show", "true");
        DataSource finalDataSource = ShardingDataSourceFactory.createDataSource(
                createDataSourceMap(), shardingRuleConfiguration, properties);
        return finalDataSource;
    }

    /**
     * 配置分表规则，注意：在后续版本中，{@link TableRuleConfiguration} 已经被重命名为了 ShardingTableRuleConfiguration
     * @see <a href="https://github.com/apache/shardingsphere/pull/5909/commits/a509389f3d233f8198230ca4d7c4e6f8e482f03b">rename TableRuleConfiguration to ShardingTableRuleConfiguration</a>
     */
    private TableRuleConfiguration getUserTableRuleConfiguration() {
        TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration(
                "user", "ds_${2019..2020}.user_${0..1}");
        // 分库策略，暂时没有分库策略
        userTableRuleConfig.setDatabaseShardingStrategyConfig(
                //new InlineShardingStrategyConfiguration("id", "ds0")

            new StandardShardingStrategyConfiguration("create_time", new YearPreciseShardingAlgorithm()));

        // 分表策略
        userTableRuleConfig.setTableShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("id", "user_${id % 2}"));
        // 主键生成策略。与 mybatis-plus 配合使用，将主键生成交给 mybatis-plus 来生成，不然两者会有冲突...
        // userTableRuleConfig.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return userTableRuleConfig;
    }

    /**
     * 根据插入时间年份进行数据分片
     */
    private class YearPreciseShardingAlgorithm implements PreciseShardingAlgorithm<LocalDateTime> {
        @Override
        public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
            log.info("availableTargetNames is {}, shardingValue is {}", availableTargetNames, shardingValue);
            for (String targetName : availableTargetNames) {
                if (targetName.endsWith(String.valueOf(shardingValue.getValue().getYear()))) {
                    return targetName;
                }
            }
            return null;
        }
    }

    /**
     * 主键生成策略。注意：Sharding 是否生成主键是有一定规则的，不是一定会生成（如果 insert 语句中插入的列包含主键列，则不会生成主键值）。具体规则参考：
     * {@link org.apache.shardingsphere.core.optimize.sharding.engnie.dml.ShardingInsertOptimizeEngine#optimize(ShardingRule, ShardingTableMetaData, String, List, InsertStatement)}
     * {@link org.apache.shardingsphere.core.optimize.sharding.segment.insert.GeneratedKey#getGenerateKey(ShardingRule, List, InsertStatement, ShardingInsertColumns, Collection)}
     *
     * @see <a href="https://blog.csdn.net/qiaoqiyu6416/article/details/107044664/">ShardingSphere报Sharding value must implements Comparable.的解决过程</a>
     * @return  {@link KeyGeneratorConfiguration}
     */
    private KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        return keyGeneratorConfiguration;
    }

    /**
     * 分库数据源配置
     */
    private List<MasterSlaveRuleConfiguration> getMasterSlaveConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration1 = new MasterSlaveRuleConfiguration(
                "ds_2019", "ds_master_0", Collections.singleton("ds_master_0_slave_0"));
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration2 = new MasterSlaveRuleConfiguration(
                "ds_2020", "ds_master_1", Collections.singleton("ds_master_1_slave_0"));
        return Arrays.asList(masterSlaveRuleConfiguration1, masterSlaveRuleConfiguration2);
    }

    private Map<String, DataSource> createDataSourceMap() {
        final Map<String, DataSource> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("ds_master_0", masterDataSource());
        dataSourceMap.put("ds_master_0_slave_0", slaveDataSource());

        dataSourceMap.put("ds_master_1", masterDataSource1());
        dataSourceMap.put("ds_master_1_slave_0", slaveDataSource1());
        return dataSourceMap;
    }

    private DataSource masterDataSource() {
        DataSourceProperties masterDataSourceProperties = new DataSourceProperties();
        masterDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSourceProperties.setUrl("jdbc:mysql://localhost:3360/test");
        masterDataSourceProperties.setUsername("root");
        masterDataSourceProperties.setPassword("rootroot");
        return getNormalDataSource(masterDataSourceProperties);
    }

    private DataSource masterDataSource1() {
        DataSourceProperties masterDataSourceProperties = new DataSourceProperties();
        masterDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDataSourceProperties.setUrl("jdbc:mysql://localhost:3360/test1");
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

    private DataSource slaveDataSource1() {
        DataSourceProperties slaveDataSourceProperties = new DataSourceProperties();
        slaveDataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDataSourceProperties.setUrl("jdbc:mysql://localhost:3361/test1");
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

}
