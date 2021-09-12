package com.dongfeng.study.basicstudy.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.*;

/**
 * @author eastFeng
 * @date 2020/4/22 - 15:17
 */
public class JDBCUtil {

    /**
     * 获取数据库连接
     * @return Connection
     */
    public static Connection getConnection()throws Exception{
        //1. 加载驱动
        Class.forName("com.mysql.jdbc.Driver");

        /*
         * 获取连接所需要的url,用户名和密码
         *
         * 提供要连接的数据库
         * jdbc:mysql  协议
         * localhost   ip地址
         * 3306        端口号(3306是数据库默认的端口号)
         * test        数据库
         */
        String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false";
        String user = "root";
        String password = "root";
        //2. 获取连接
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 使用c3p0数据库连接池技术获取数据库连接
     */
    private static  ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnectionByC3p0() throws SQLException {
        return comboPooledDataSource.getConnection();
    }

    /**
     * 关闭Connection与Statement连接
     * @param connection Connection
     * @param statement Statement
     */
    public static void closeConnection(Connection connection, Statement statement){
        closeConnection(connection, statement, null);
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if (null != connection){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (null != statement){
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (null != resultSet){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
