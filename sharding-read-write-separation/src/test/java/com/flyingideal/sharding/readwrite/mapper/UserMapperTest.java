package com.flyingideal.sharding.readwrite.mapper;

import com.flyingideal.sharding.core.mapper.UserMapper;
import com.flyingideal.sharding.core.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanchao
 * @date 2019-10-30 18:00
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertUser() {
        User user = new User(null, "user3", 23, "user3.com");
        int saveCount = userMapper.insert(user);
        log.info("{}", saveCount);
    }

    @Test
    public void getUser() {
        int id = 3;
        User user = userMapper.selectById(id);
        log.info("{}", user);
    }
}
