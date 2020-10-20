package com.flyingideal.sharding.readwrite.table.mapper;

import com.flyingideal.sharding.core.bean.User;
import com.flyingideal.sharding.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

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
        // User user = new User(10L, "user1", 21, "user1.com");
        User user = new User();
        user.setName("Id generate test");
        user.setAge(33);
        user.setEmail("test.com");
        user.setCreateTime(LocalDateTime.now().plusYears(-1));
        int saveCount = userMapper.insert(user);
        log.info("{}", saveCount);
    }

    @Test
    public void getUser() {
        int id = 9;
        User user = userMapper.selectById(id);
        log.info("{}", user);
    }
}
