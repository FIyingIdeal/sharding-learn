package com.flyingideal.sharding.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanchao
 * @date 2019-10-28 23:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sharding_user")
public class User implements Serializable {

    @TableId(value = "id")
    private Long id;
    private String name;
    private Integer age;
    private String email;

}
