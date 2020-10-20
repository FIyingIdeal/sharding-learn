package com.flyingideal.atomikos.transaction.essentials.springmybatis.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yanchao
 * @date 2020-07-14 16:19
 */
@Data
@TableName("account")
public class Account {

    private Integer userId;
    private Double money;

}
