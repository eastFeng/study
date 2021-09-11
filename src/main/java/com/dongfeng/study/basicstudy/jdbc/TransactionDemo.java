package com.dongfeng.study.basicstudy.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库事务操作
 *
 * 数据一旦提交, 就不可回滚;
 * DDL(主要是表结构变动)操作一旦执行, 就会自动提交: SET AutoCommit = false 对DDL操作失效;
 * DML(对数据表的增、删、改、查)操作默认情况下一旦执行, 就会自动提交: SET AutoCommit = false 取消去DML操作的自动提交;
 * 在关闭数据库连接的时候, 默认会自动提交数据;
 *
 * 事务的ACID属性:
 * 1. 原子性(Atomicity): 原子性是指事务是不可分割的整体,要么全都执行,要么全都不执行;
 * 2. 一致性(Consistency): 事务必须使数据库从一个一致性状态转换为另一个一致性状态;
 * 3. 隔离性(Isolation): 隔离性是指一个事务的执行不能被其他事务干扰,
 *    即一个事务内部的操作与使用的数据对并发的其他事务是隔离的, 并发执行的各个事务之间不能相互干扰;
 * 4. 持久性(Durability): 持久性是指事务一旦提交, 它对数据库中数据的改变是永久性的, 接下来的其他操作和数据库故障不应该对其有任何影响;
 * @author eastFeng
 * @date 2020/4/22 - 21:09
 */
public class TransactionDemo {
    public static void main(String[] args) {

    }


    /**
     * 转账
     */
    private static void test_JDBC_Transaction() {
        Connection connection = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 开启事务
            connection.setAutoCommit(false);

            //3. 进行数据库操作
            String sql1 = "UPDATE user_table SET balance = balance - 100 WHERE id = ?";
            commonUpdate(connection, sql1, 2);

            String sql2 = "UPDATE user_table SET balance = balance + 100 WHERE id = ?";
            commonUpdate(connection, sql2, 3);

            //4. 若没有异常, 则提交事务
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //5. 若有异常, 则回滚事务
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            //6. 恢复每次DML操作的自动提交功能
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //7. 关闭连接
            JDBCUtil.closeConnection(connection,null);
        }
    }

    /**
     * 通用的增删改方法
     */
    private static void commonUpdate(Connection connection, String sql, Object... args){
        PreparedStatement preparedStatement = null;
        try {
            // 预编译SQL语句, 并获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            // 填充占位符
            for (int i=0; i<args.length; i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            // 执行操作(执行SQL语句)
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 关闭资源
            JDBCUtil.closeConnection(null, preparedStatement);
        }
    }

}
