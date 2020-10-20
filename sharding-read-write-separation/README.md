# sharding-read-write-separation

该项目模块主要用来测试 sharding 提供的**主从库读写分离**功能，不涉及分表读写分离；分表的读写分离请参考 `sharding-readwrite-table-separation` 模块；

项目在 `ReadWriteSeparationDataSourceConfig` 类中配置了两个数据源：

| 主/从 | 数据源名称 | 数据源URL |
| --- | --- | --- |
主 | ds_master | jdbc:mysql://localhost:**3360**/test
从 | ds_slave | jdbc:mysql://localhost:**3361**/test

ShardingSphere 只支持一个主库，从库可以配置 1 或多个；

## 测试
通过 test 包中的单元测试进行插入和查询的验证。

为了能够直观的看出 SQL 在哪个数据源上执行，需要将 Sharding SQL 日志打印功能开启，其默认为未开启状态，开启方式为：

- 非 Spring Boot Starter 项目配置（未引入 `sharding-jdbc-spring-boot-starter`）：在配置数据源的时候设置 Properties 属性 -- sql.show=true；
配置示例请参考 `com.flyingideal.sharding.readwrite.config.ReadWriteSeparationDataSourceConfig#shardingMasterSlaveDataSource()` 方法；

- 如果是 Spring Boot Starter 项目就比较简单了，直接在配置文件中配置：`spring.shardingsphere.props.sql.show=true`；

通过上述配置，则在执行 SQL 的时候就会将使用的 `Rule`，`DataSource`，`SQL` 打印出来，如下所示：
```text
2020-07-06 23:56:33.147  INFO 36797 --- [main] ShardingSphere-SQL  : Rule Type: master-slave
2020-07-06 23:56:33.148  INFO 36797 --- [main] ShardingSphere-SQL  : SQL: SELECT id,name,email,age FROM user WHERE id=?  ::: DataSources: ds_slave
```

## 参考
1. [shardingsphere-jdbc Java API 方式数据分片](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/usage/sharding/java-api/)；
2. [shardingsphere-jdbc Java API 方式读写分离配置](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/configuration/java-api/read-write-split/)；
3. [shardingsphere-jdbc 属性配置](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/configuration/props/)；