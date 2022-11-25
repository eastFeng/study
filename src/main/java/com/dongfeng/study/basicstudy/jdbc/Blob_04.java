package com.dongfeng.study.basicstudy.jdbc;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * PreparedStatement操作Blob数据
 *
 * @author eastFeng
 * @date 2020/4/22 - 15:17
 */
public class Blob_04 {
    public static void main(String[] args) {
        test_query();
    }


    /**
     * 向customers表中插入Blob类型字段
     */
    private static void test_insert(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        FileInputStream fileInputStream = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement对象
            String sql = "INSERT INTO customers(`name`, email, birth, photo) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            preparedStatement.setObject(1, "猫");
            preparedStatement.setObject(2, "cat@qq.com");
            preparedStatement.setObject(3, new Date());

            fileInputStream = new FileInputStream(new File("E:\\个人文件\\图片\\meitui.jpg"));
            preparedStatement.setBlob(4, fileInputStream);

            //4. 执行操作
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection,preparedStatement);
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 查询customers表中Blob类型字段
     */
    public static void test_query(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream binaryStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement对象
            String sql = "SELECT id, `name`, email, birth, photo FROM customers WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            preparedStatement.setInt(1, 7);

            //4. 执行操作, 获取结果集
            resultSet = preparedStatement.executeQuery();

            //5. 处理结果集
            if (resultSet.next()){
//                // 方式一:
//                int id = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                String email = resultSet.getString(3);
//                Date birth = resultSet.getDate(4);

                // 方式二:
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");

                System.out.println(new Customers(id, name, email, birth));

                // 获取Blob类型字段数据, 并以文件形式保存在本地
                Blob photo = resultSet.getBlob("photo");
                binaryStream = photo.getBinaryStream();
                fileOutputStream = new FileOutputStream(new File("C:\\Users\\eastFeng\\Desktop" +
                        "\\test22.jpg"));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = binaryStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement, resultSet);
            if (binaryStream != null){
                try {
                    binaryStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
