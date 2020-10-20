package com.flyingideal.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flyingideal.sharding.mapper")
public class ShardingTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingTableApplication.class, args);
    }

}
