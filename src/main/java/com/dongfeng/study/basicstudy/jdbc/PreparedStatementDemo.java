package com.dongfeng.study.basicstudy.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> PreparedStatement: 表示预编译的SQL语句的对象。
 * <p> SQL语句已预编译并存储在PreparedStatement对象中。 然后可以使用该对象多次有效地执行此语句。
 *
 * PreparedStatement优点:
 * <lo>
 * <li> 1. 防止SQL注入;
 * <li> 2. PreparedStatement可以操作Blob数据, 而Statement做不到;
 * <li> 3. PreparedStatement可以实现更高效的批量操作;
 * </lo>
 * @author eastFeng
 * @date 2020/4/20 - 17:00
 */
@Slf4j
public class PreparedStatementDemo {

    public static void main(String[] args) {
        test_select();
    }

    private static void test_insert(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句,获取PreparedStatement对象
            //?: 占位符
            String sql = "INSERT INTO customers(`name`, email, birth) VALUES(?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            preparedStatement.setString(1, "哪吒");
            preparedStatement.setString(2, "nezha@tiangong.com");
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));

            //4. 执行操作
            final boolean execute = preparedStatement.execute();
            System.out.println(execute);
        }catch (Exception e){
            e.printStackTrace();
            log.info("test_insert error:{}",e.getMessage(),e);
        }finally {
            //5. 关闭资源
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }


    private static void test_update(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句,获取PreparedStatement对象
            String sql = "UPDATE customers SET name = ?, update_time = NOW() WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            preparedStatement.setString(1, "三太子");
            preparedStatement.setInt(2, 3);

            //4. 执行操作
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5. 关闭资源
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }


    /**
     * 通用的增删改方法
     */
    private static void test_common_update(String sql, Object... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句,获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int i=0; i<args.length; i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            //4. 执行操作
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5. 关闭资源
            JDBCUtil.closeConnection(connection, preparedStatement);
        }
    }

    private static void test_select(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            // 2. 预编译SQL语句, 获取PreparedStatement对象
            String sql = "SELECT * FROM user_info";
            preparedStatement = connection.prepareStatement(sql);

            // 3. 执行并返回结果集
            resultSet = preparedStatement.executeQuery();

            // ArrayList<Customers> customersArrayList = new ArrayList<>(10);
            // 4. 处理结果集
            while (resultSet.next()){
                // 获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                int userLoginId = resultSet.getInt(2);
                String name = resultSet.getString(3);
                System.out.println("id="+id+" userLoginId="+userLoginId+" name="+name);

                // Customers customers = new Customers(id, name, email, birth);

                // customersArrayList.add(customers);
            }
            // System.out.println(customersArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 5. 关闭资源
            JDBCUtil.closeConnection(connection, preparedStatement, resultSet);
        }
    }

    private static void test_selectForCustomers(){
        String sql1 = "SELECT name, id, birth, email FROM customers WHERE id = ?";
        List<Customers> customersList1 = selectForCustomers(sql1, 3);
        customersList1.forEach(System.out::println);
        System.out.println();

        String sql2 = "SELECT name, id FROM customers";
        List<Customers> customersList2 = selectForCustomers(sql2);
        customersList2.forEach(System.out::println);
    }

    /**
     * customers表通用的查询方法
     */
    private static List<Customers> selectForCustomers(String sql, Object... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Customers> customersList = new ArrayList<>(10);
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int x=0; x<args.length; x++){
                preparedStatement.setObject(x+1, args[x]);
            }

            //4. 执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 通过元数据获取结果集的列数
            int columnCount = metaData.getColumnCount();


            //5. 处理结果集
            while (resultSet.next()){
                Customers customers = new Customers();
                // 处理结果集一行数据中的每一列
                for (int i=0; i<columnCount; i++){
                    // 获取当前列的值
                    Object columnValue = resultSet.getObject(i + 1);
                    // 获取当前列的列名
                    String columnName = metaData.getColumnName(i + 1);

                    // 给Customers对象指定的columnName属性,赋值为columnValue, 通过反射
                    Field field = Customers.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customers, columnValue);
                }
                customersList.add(customers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement, resultSet);
        }
        return customersList;
    }

    private static void test_selectForOrder(){
        String sql = "SELECT order_id orderId, order_type orderType, order_name orderName, create_time createTime " +
                "FROM `order` WHERE order_id = ?";
        final List<Order> orderList = selectForOrder(sql, 1);
        orderList.forEach(T -> System.out.println(T));
    }

    /**
     * 针对order表的通用查询
     *
     * 针对表的列名与类的属性名不相同的情况:
     * 1. 声明SQL时, 必须使用类的属性名来命名列名的别名;
     * 2. 使用ResultSetMetaData时, 需要使用getColumnLabe()方法来替换getColumnName()方法,
     *    来获取列的别名;
     *    说明: 如果在SQL中没有给类名取别名, getColumnLabe()获取的就是列名
     * @param sql
     * @param args
     * @return
     */
    private static List<Order> selectForOrder(String sql, Object... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Order> orderList = new ArrayList<>(10);
        try {
            //1. 获取连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int x=0; x<args.length; x++){
                preparedStatement.setObject(x+1, args[x]);
            }

            //4. 执行并返回结果集
            resultSet = preparedStatement.executeQuery();
            // 获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 通过元数据获取结果集的列数
            int columnCount = metaData.getColumnCount();

            // 处理结果集
            while (resultSet.next()){
                Order order = new Order();
                // 处理结果集一行数据中的每一列
                for (int i=0; i<columnCount; i++){
                    // 获取每一列的值
                    Object columnValue = resultSet.getObject(i + 1);
                    // getColumnName(): 获取每个列的列名(表的列名)----不推荐
                    // getColumnLabe(): 获取每个列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    // 反射
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement, resultSet);
        }
        return orderList;
    }

    private static void test_commonSelect(){
        String sql1 = "SELECT order_id orderId, order_type orderType, order_name orderName, create_time createTime " +
                "FROM `order` WHERE order_id = ?";
        List<Order> orderList = commonSelect(Order.class, sql1, 1);
        orderList.forEach(T -> System.out.println(T));

        System.out.println();

        String sql2 = "SELECT name, id FROM customers";
        List<Customers> customersList = commonSelect(Customers.class, sql2);
        customersList.forEach(T -> System.out.println(T));
    }


    /**
     * 针对不同表的通用查询语句
     * @param clazz Class类对象
     * @param sql 要执行查询的SQL语句
     * @param args 填充占位符需要的参数
     * @param <T> 泛型
     * @return List<T>
     */
    private static <T> List<T> commonSelect(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> tList = new ArrayList<>(10);
        try {
            //1. 获取数据库连接
            connection = JDBCUtil.getConnection();

            //2. 预编译SQL语句, 并获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int x=0; x<args.length; x++){
                preparedStatement.setObject(x+1, args[x]);
            }

            //4. 执行操作, 并获取结果集
            resultSet = preparedStatement.executeQuery();
            // 获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 获取结果集列数
            int columnCount = metaData.getColumnCount();

            //5. 处理结果集
            while (resultSet.next()){
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一列: 给t对象指定的属性赋值;
                for (int i=0; i<columnCount; i++){
                    // 获取当前列的值
                    Object columnValue = resultSet.getObject(i + 1);
                    // 获取当前列的别名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    // 通过反射, 为类T的属性名为columnLabel的属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                tList.add(t);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtil.closeConnection(connection, preparedStatement, resultSet);
        }
        return tList;
    }

}
