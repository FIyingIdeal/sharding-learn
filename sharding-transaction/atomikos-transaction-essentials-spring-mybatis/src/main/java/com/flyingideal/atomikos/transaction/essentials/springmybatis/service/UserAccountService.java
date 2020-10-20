package com.flyingideal.atomikos.transaction.essentials.springmybatis.service;

import com.flyingideal.atomikos.transaction.essentials.springmybatis.bean.Account;
import com.flyingideal.atomikos.transaction.essentials.springmybatis.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yanchao
 * @date 2020-07-14 18:48
 */
@Service
@Slf4j
public class UserAccountService {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserAndAccount() {
        User user = new User();
        user.setName("atomikos-spring-mybatis-test");
        boolean userStatus = userService.save(user);
        log.info("user save status : {}, userId is {}", userStatus, user.getId());

        // 模拟异常，测试事务回滚
        // int i = 1 / 0;

        Account account = new Account();
        account.setUserId(user.getId());
        account.setMoney(1000d);
        boolean accountStatus = accountService.save(account);
        log.info("account save status : {}", accountStatus);

        return userStatus && accountStatus;
    }
}
