package com.dongfeng.study.basicstudy.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author eastFeng
 * @date 2020/4/27 - 13:52
 */
public class C3P0Demo {
    public static void main(String[] args) {
        test();
    }


    private static void test(){
        try {
            // 获取c3p0数据库连接池
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
            comboPooledDataSource.setUser("root");
            comboPooledDataSource.setPassword("root");

            // 设置初始时数据库连接池中的连接数
            comboPooledDataSource.setInitialPoolSize(10);

            Connection connection = comboPooledDataSource.getConnection();
            System.out.println(connection);
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用配置文件
     */
    private static void test_config(){
        try {
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource("helloc3p0");
            final Connection connection = comboPooledDataSource.getConnection();
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
