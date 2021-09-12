package com.dongfeng.study.basicstudy.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

/**
 * 使用PreparedStatement实现批量数据操作;
 *
 * @author eastFeng
 * @date 2020/4/22 - 17:25
 */
public class BatchOperationDemo {
    public static void main(String[] args) {
        batchInsert3();
    }

    /**
     * 批量插入方式一: 时间太慢
     */
    private static void batchInsert1(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            long start = System.currentTimeMillis();

            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement连接
            String sql = "INSERT INTO goods(`name`, price, `number`, total_price, create_time) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //3. 执行两万次
            for (int i=0; i<20000; i++){
                preparedStatement.setString(1, "戴尔MS116有线商务办公鼠标");
                preparedStatement.setDouble(2, 30.90);
                preparedStatement.setInt(3, 1);
                preparedStatement.setDouble(4, 30.90);
                preparedStatement.setDate(5,new Date(System.currentTimeMillis()));

                preparedStatement.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间是: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }

    /**
     * 批量插入方式二:
     * 1. addBatch(), executeBatch(), clearBatch()
     * 2.
     * 3.
     */
    private static void batchInsert_2(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            long start = System.currentTimeMillis();

            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement连接
            String sql = "INSERT INTO goods(`name`, price, `number`, total_price, create_time) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //3. 执行两万次
            for (int i=0; i<20000; i++){
                preparedStatement.setString(1, "戴尔MS116有线商务办公鼠标");
                preparedStatement.setDouble(2, 30.90);
                preparedStatement.setInt(3, 1);
                preparedStatement.setDouble(4, 30.90);
                preparedStatement.setDate(5,new Date(System.currentTimeMillis()));

                // "攒"sql
                preparedStatement.addBatch();

                // "攒"500个执行一次
                if (i % 500 == 0){
                    // 执行batch
                    int[] ints = preparedStatement.executeBatch();
                    System.out.println(ints.length);

                    // 清空batch
                    preparedStatement.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间是: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }

    /**
     * 批量插入方式三: 三种方式中时间最快
     */
    private static void batchInsert3(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            long start = System.currentTimeMillis();

            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            // 设置不允许自动提交数据
            connection.setAutoCommit(false);

            //2. 预编译SQL语句, 并获取PreparedStatement连接
            String sql = "INSERT INTO goods(`name`, price, `number`, total_price, create_time) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //3. 执行两万次
            for (int i=0; i<20000; i++){
                preparedStatement.setString(1, "戴尔MS116有线商务办公鼠标");
                preparedStatement.setDouble(2, 30.90);
                preparedStatement.setInt(3, 1);
                preparedStatement.setDouble(4, 30.90);
                preparedStatement.setDate(5,new Date(System.currentTimeMillis()));

                // "攒"sql
                preparedStatement.addBatch();

                // "攒"500个执行一次
                if (i % 500 == 0){
                    // 执行batch
                    int[] ints = preparedStatement.executeBatch();
                    System.out.println(ints.length);

                    // 清空batch
                    preparedStatement.clearBatch();
                }
            }

            //提交数据
            connection.commit();

            long end = System.currentTimeMillis();
            System.out.println("花费的时间是: "+(end-start)+"毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }


}
