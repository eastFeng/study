package com.dongfeng.study.basicstudy.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author eastFeng
 * @date 2020/4/27 - 19:39
 */
public class Druid_06 {
    public static void main(String[] args) {

    }

    private static void test(){
        try {
            Properties properties = new Properties();

            final DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            System.out.println(dataSource.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
