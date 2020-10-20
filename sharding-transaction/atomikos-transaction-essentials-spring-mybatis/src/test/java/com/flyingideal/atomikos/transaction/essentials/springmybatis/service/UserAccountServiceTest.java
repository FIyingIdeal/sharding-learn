package com.flyingideal.atomikos.transaction.essentials.springmybatis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yanchao
 * @date 2020-07-14 18:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @Test
    public void saveUserAndAccount() {
        boolean b = this.userAccountService.saveUserAndAccount();
        log.info("Save user and account status is {}", b);
    }
}
