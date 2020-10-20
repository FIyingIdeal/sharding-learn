package com.flyingideal.sharding.transaction.xa.service;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yanchao
 * @date 2020-07-16 11:51
 */
@Service
public class XaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(value = TransactionType.XA)
    public void testXa() {
        jdbcTemplate.execute("insert into user values (0, 'sharding-xa', 22, 'laji')");
    }
}
