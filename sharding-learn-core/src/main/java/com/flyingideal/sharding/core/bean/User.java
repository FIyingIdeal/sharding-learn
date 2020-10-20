package com.flyingideal.sharding.core.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yanchao
 * @date 2019-10-28 23:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {

    // @TableId(value = "id", type = IdType.AUTO)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    private String name;
    private Integer age;
    private String email;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
