package com.flyingideal.sharding.core.bean;

import lombok.Data;

/**
 * @author yanchao
 * @date 2019-10-31 11:07
 */
@Data
public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
