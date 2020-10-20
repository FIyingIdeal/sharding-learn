package com.flyingideal.sharding.readwrite.table;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yanchao
 * @date 2019-10-31 11:02
 */
@SpringBootApplication
@MapperScan("com.flyingideal.sharding.core.mapper")
public class ShardingReadWriteAndTableSeparation {
    public static void main(String[] args) {
        SpringApplication.run(ShardingReadWriteAndTableSeparation.class, args);
    }
}
