package com.flyingideal.sharding.mapper;

import com.flyingideal.sharding.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanchao
 * @date 2019-10-29 00:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = userMapper.selectById(112);
        log.info("{}", user);
    }

    @Test
    public void insert() {
        User user = new User(9L, "sharding_name", 22, "sharding@163.com");
        int i = userMapper.insert(user);
        log.info("{}", i);
    }
}
