package com.flyingideal.atomikos.origin;

import java.sql.*;

/**
 * 非 JTA 情况下的事务测试
 *
 * @author yanchao
 * @date 2020-07-13 18:14
 */
public class CommonTransactionExample {

    private static final String userSql = "insert into user(name) values (?)";
    private static final String accountSql = "insert into account(user_id, money) values (?, ?)";

    public static void main(String[] args) {

        try (Connection conn1 = DriverManager.getConnection("jdbc:mysql://localhost:3360/db_user", "root", "rootroot");
             Connection conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3360/db_account", "root", "rootroot")) {
            try {
                // 开启事务
                conn1.setAutoCommit(false);
                conn2.setAutoCommit(false);
                try (PreparedStatement ps1 = conn1.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement ps2 = conn2.prepareStatement(accountSql)) {

                    ps1.setString(1, "atomikos-test");
                    ps1.executeUpdate();
                    ResultSet generatedKeys = ps1.getGeneratedKeys();

                    int userId = 0;
                    while (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                        System.out.println("============= userId is " + userId);
                    }

                    // 开始执行 db2 上的 sql
                    ps2.setInt(1, userId);
                    ps2.setDouble(2, 100000);
                    ps2.executeUpdate();
                }

                // 两阶段提交
                conn1.commit();

                // 模拟异常，如果异常出现在这中间（一般不会）或 conn2.commit() 异常，则会破坏一致性
                // int i = 10 / 0;

                conn2.commit();
            } catch (Exception e) {
                e.printStackTrace();
                conn1.rollback();
                conn2.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
