package com.flyingideal.atomikos.transaction.essentials.springmybatis.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author yanchao
 * @date 2020-07-14 18:04
 */
@Configuration
@MapperScan(value = "com.flyingideal.atomikos.transaction.essentials.springmybatis.mapper.dbaccount",
        sqlSessionFactoryRef = AccountDataSourceConfig.ACCOUNT_SQL_SESSION_FACTORY_BEAN_NAME)
public class AccountDataSourceConfig {

    public static final String ACCOUNT_DATASOURCE_BEAN_NAME = "accountDataSource";
    public static final String ACCOUNT_SQL_SESSION_FACTORY_BEAN_NAME = "accountSqlSessionFactory";

    @Bean(name = ACCOUNT_DATASOURCE_BEAN_NAME)
    public DataSource accountDataSource() {
        DruidXADataSource accountDataSource = new DruidXADataSource();
        accountDataSource.setUrl("jdbc:mysql://localhost:3360/db_account");
        accountDataSource.setUsername("root");
        accountDataSource.setPassword("rootroot");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(accountDataSource);
        atomikosDataSourceBean.setUniqueResourceName("accountResource");
        atomikosDataSourceBean.setPoolSize(5);
        return atomikosDataSourceBean;
    }

    @Bean(name = ACCOUNT_SQL_SESSION_FACTORY_BEAN_NAME)
    public SqlSessionFactory accountSqlSessionFactory(@Qualifier(ACCOUNT_DATASOURCE_BEAN_NAME) DataSource dataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
}
