package com.flyingideal.atomikos.origin;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.*;
import java.util.Properties;

/**
 * @author yanchao
 * @date 2020-07-13 18:14
 */
public class AtomikosExample {

    public static AtomikosDataSourceBean createAtomikosDataSourceBean(String dbName) {
        Properties p = new Properties();
        p.setProperty("url", "jdbc:mysql://localhost:3360/" + dbName);
        p.setProperty("user", "root");
        p.setProperty("password", "rootroot");

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(dbName);
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        ds.setXaProperties(p);
        return ds;
    }

    public static void main(String[] args) {
        AtomikosDataSourceBean ds1 = createAtomikosDataSourceBean("db_user");
        AtomikosDataSourceBean ds2 = createAtomikosDataSourceBean("db_account");

        Connection conn1 = null;
        Connection conn2 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        String userSql = "insert into user(name) values (?)";
        String accountSql = "insert into account(user_id, money) values (?, ?)";

        UserTransaction userTransaction = new UserTransactionImp();
        try {
            // 开启事务
            userTransaction.begin();

            // 开始执行 db1 上的 sql
            conn1 = ds1.getConnection();
            ps1 = conn1.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, "atomikos-test");
            ps1.executeUpdate();
            ResultSet generatedKeys = ps1.getGeneratedKeys();
            int userId = -1;
            while (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
                System.out.println("============= userId is " + userId);
            }

            // 模拟异常，用于测试分布式事务回滚
            // int i = 10 / 0;

            // 开始执行 db2 上的 sql
            conn2 = ds2.getConnection();
            ps2 = conn2.prepareStatement(accountSql);
            ps2.setInt(1, userId);
            ps2.setDouble(2, 100000);
            ps2.executeUpdate();

            // 两阶段提交
            userTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (ps2 != null) {
                    ps2.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (conn1 != null) {
                    conn1.close();
                }
                ds1.close();
                ds2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
