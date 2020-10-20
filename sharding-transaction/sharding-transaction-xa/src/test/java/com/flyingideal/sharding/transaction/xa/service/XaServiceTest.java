package com.flyingideal.sharding.transaction.xa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanchao
 * @date 2020-07-16 12:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class XaServiceTest {

    @Autowired
    private XaService xaService;

    @Test
    public void shardingXaTest() {
        this.xaService.testXa();
    }
}
