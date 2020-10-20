package com.flyingideal.sharding.readwrite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flyingideal.sharding.core.mapper")
public class ShardingReadWriteSeparation {
    public static void main(String[] args) {
        SpringApplication.run(ShardingReadWriteSeparation.class, args);
    }
}