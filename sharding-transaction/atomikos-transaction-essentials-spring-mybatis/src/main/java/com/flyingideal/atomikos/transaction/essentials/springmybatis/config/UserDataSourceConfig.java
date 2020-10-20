package com.flyingideal.atomikos.transaction.essentials.springmybatis.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/**
 * @author yanchao
 * @date 2020-07-14 17:29
 */
@Configuration
@MapperScan(value = "com.flyingideal.atomikos.transaction.essentials.springmybatis.mapper.dbuser",
        sqlSessionFactoryRef = UserDataSourceConfig.USER_SQL_SESSION_FACTORY_BEAN_NAME)
public class UserDataSourceConfig {

    public static final String USER_DATASOURCE_BEAN_NAME = "userDataSource";
    public static final String USER_SQL_SESSION_FACTORY_BEAN_NAME = "userSqlSessionFactory";

    @Bean(name = USER_DATASOURCE_BEAN_NAME)
    @Primary
    public DataSource userDataSource() {
        DruidXADataSource dataSource = new DruidXADataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3360/db_user");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(dataSource);
        atomikosDataSourceBean.setUniqueResourceName("userResource");
        atomikosDataSourceBean.setPoolSize(5);
        return atomikosDataSourceBean;
    }

    @Bean(name = "transactionManager")
    @Primary
    public JtaTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    @Bean(name = USER_SQL_SESSION_FACTORY_BEAN_NAME)
    @Primary
    public SqlSessionFactory userSqlSessionFactory(@Qualifier(USER_DATASOURCE_BEAN_NAME) DataSource dataSource)
            throws Exception {
        // 如果使用 mybatis-plus 的话，必须使用 MybatisSqlSessionFactoryBean，而不能使用 SqlSessionFactoryBean，否则会报错
        // https://mp.baomidou.com/guide/faq.html#%E5%87%BA%E7%8E%B0-invalid-bound-statement-not-found-%E5%BC%82%E5%B8%B8
        final MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
}
