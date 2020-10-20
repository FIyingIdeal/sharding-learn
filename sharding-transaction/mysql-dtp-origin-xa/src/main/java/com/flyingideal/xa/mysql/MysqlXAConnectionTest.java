package com.flyingideal.xa.mysql;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author yanchao
 * @date 2020-07-13 12:05
 */
public class MysqlXAConnectionTest {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3360/test";
        String url2 = "jdbc:mysql://localhost:3360/test1";
        String username = "root";
        String password = "rootroot";

        Connection conn1 = getConnection(url, username, password);
        Connection conn2 = getConnection(url2, username, password);

        XAConnection xaConn1 = getXAConnection(conn1);
        XAConnection xaConn2 = getXAConnection(conn2);

        XAResource resource1 = xaConn1.getXAResource();
        XAResource resource2 = xaConn2.getXAResource();

        byte[] gtrid = "g12345".getBytes();
        int formatId = 1;

        try {
            // ================= 分别执行 RM1 和 RM2 上的事务分支 ==================
            byte[] bqual1 = "b00001".getBytes();
            Xid xid1 = new MysqlXid(gtrid, bqual1, formatId);
            resource1.start(xid1, XAResource.TMNOFLAGS);
            PreparedStatement ps1 = conn1.prepareStatement("insert into user(name, age, email) values ('xa-test11', 11, 'xa-teste')");
            ps1.execute();
            resource1.end(xid1, XAResource.TMSUCCESS);

            byte[] bqual2 = "b00002".getBytes();
            Xid xid2 = new MysqlXid(gtrid, bqual2, formatId);
            resource2.start(xid2, XAResource.TMNOFLAGS);
            PreparedStatement ps2 = conn2.prepareStatement("insert into user(name, age, email) values ('xa-test22', 12, 'xa-teste')");
            ps2.execute();
            resource2.end(xid2, XAResource.TMSUCCESS);


            // ================ 两阶段提交 ==============
            // phase1：询问所有 RM，准备提交事务分支
            int rm1Prepare = resource1.prepare(xid1);
            int rm2Prepare = resource2.prepare(xid2);

            // phase2：提交所有事务分支
            // 该变量用于是否可以优化为一阶段提交：当TM判断有2个事务分支，所以不能优化为一阶段提交，这里我们手动指定了 false
            boolean onePhase = false;

            // 所有事务分支都 prepare 成功，则提交所有事务分支
            if (rm1Prepare == XAResource.XA_OK && rm2Prepare == XAResource.XA_OK) {
                resource1.commit(xid1, onePhase);
                resource2.commit(xid2, onePhase);
            } else {
                resource1.rollback(xid1);
                resource2.rollback(xid2);
            }
            //
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static XAConnection getXAConnection(Connection connection) {
        if (!(connection instanceof JdbcConnection)) {
            throw new IllegalArgumentException("The argument connection is not instanceof JdbcConnection");
        }
        return new MysqlXAConnection((JdbcConnection) connection, true);
    }

    private static Connection getConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


}
