package com.flyingideal.atomikos.transaction.essentials.springmybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flyingideal.atomikos.transaction.essentials.springmybatis.bean.User;
import com.flyingideal.atomikos.transaction.essentials.springmybatis.mapper.dbuser.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author yanchao
 * @date 2020-07-14 18:52
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
