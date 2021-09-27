package com.dongfeng.study.basicstudy.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * <p> JDBC(Java Database Connectivity)是一个独立于特定数据库管理系统、通用的SQL数据库存取和操作的公共接口（一组API），
 * 定义了用来访问数据库的标准Java类库（java.sql,javax.sql）,使用这些类库可以以一种标准的方法、方便地访问数据库资源。
 *
 * <p> JDBC为访问不同的数据库提供了一种统一的途径，为开发者屏蔽了一些细节问题。
 *
 * <p> JDBC接口（API）包括两个层次：
 * <lo>
 *     <li> 面向应用的API：Java API，抽象接口，供应用程序开发人员使用（连接数据库，执行SQL语句，获得结果）。
 *     <li> 面向数据库的API：Java Driver API，供开发商开发数据库驱动程序用。
 * </lo>
 *
 * <p> JDBC是sun公司提供一套用于数据库操作的接口，java程序员只需要面向这套接口编程即可。
 * 不同的数据库厂商，需要针对这套接口，提供不同实现。不同的实现的集合，即为不同数据库的驱动.------面向接口编程
 *
 * <p> java.sql.Driver: 是所有JDBC驱动程序需要实现的接口,这个接口是提供给数据库厂商使用的，不同数据库厂商提供不同的实现。
 *
 * @author eastFeng
 * @date 2020/4/18 - 14:52
 */
public class ConnectionDemo {

    /**
     * 获取数据库连接: 方式五最好
     * @param args
     */
    public static void main(String[] args) {
        try {
            test_connection_4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接方式一
     * <p> java.sql.Driver: 是所有JDBC驱动程序需要实现的接口,这个接口是提供给数据库厂商使用的，不同数据库厂商提供不同的实现。
     * @throws Exception 发生异常
     */
    private static void test_connection_1()throws Exception{
        // 1. 获取驱动: 创建Driver实现类对象
        //com.mysql.jdbc.Driver(): MySQL数据库实现的驱动类
        Driver driver = null;
//        Driver driver = new com.mysql.jdbc.Driver();

        /*
         * 提供要连接的数据库
         * jdbc:mysql  协议
         * localhost   ip地址
         * 3306        端口号(3306是数据库默认的端口号)
         * test        数据库
         */
        String url = "jdbc:mysql://localhost:3306/dongfeng_test";
        Properties info = new Properties();
        // 用户名和密码封装在Properties中
        info.put("user","root");
        info.put("password","zdf123");
        // 2. 获取数据库连接
        final Connection connect = driver.connect(url, info);
        // 获取Connection对象成功(不为null)说明连接数据库成功
        System.out.println(connect);
    }

    /**
     * 获取数据库连接方式二: 在如下的程序中不出现第三方的api,使程序有更好的移植性
     * <p> 方式一种的缺点:  获取Driver实现类对象的时候引入了第三方的类
     * @throws Exception 发生异常
     */
    private static void test_connection_2()throws Exception{
        // 1. 获取驱动: 创建Driver实现类对象(使用反射)
        final Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        final Driver driver = (Driver) aClass.newInstance();

        // 提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/dongfeng_test";
        // 提供连接数据库需要的用户名和密码
        Properties info = new Properties();
        info.put("user","root");
        info.put("password","zdf123");

        // 2. 获取数据库连接
        final Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    /**
     * 获取数据库连接方式三: 使用DriverManager
     * <p> DriverManager: 用于管理一组JDBC驱动程序的基本服务。
     */
    private static void  test_connection_3()throws Exception{
        // 1. 获取驱动: 创建Driver实现类对象(使用反射)
        final Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        final Driver driver = (Driver) aClass.newInstance();

        // 2. 注册驱动
        DriverManager.registerDriver(driver);

        //获取连接所需要的url,用户名和密码
        String url = "jdbc:mysql://localhost:3306/dongfeng_test";
        String user = "root";
        String password = "zdf123";
        /*
         * 3. 获取数据库连接
         * 当调用方法getConnection时， DriverManager将尝试从初始化中加载的驱动程序中找到合适的驱动程序，
         * 并使用与当前小程序或应用程序相同的类加载器显式加载驱动程序。
         */
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }


    /**
     * <p> 获取数据库连接方式四: 对方式三的优化, 只用加载驱动而不用显示的注册驱动;
     *
     * <p> MySQ驱动类(com.mysql.jdbc.Driver)源码:
     * <pre>
     * public class Driver extends NonRegisteringDriver implements java.sql.Driver {
     *     public Driver() throws SQLException {
     *     }
     *     // 静态代码块，会在类加载时执行
     *     static {
     *         try {
     *             DriverManager.registerDriver(new Driver());
     *         } catch (SQLException var1) {
     *             throw new RuntimeException("Can't register driver!");
     *         }
     *     }
     * }</pre>
     *
     *  通过源码可知,当通过反射Class.forName("com.mysql.jdbc.Driver")加载了MySQ驱动类(Driver)之后,
     *  MySQ驱动类会自动用DriverManager.registerDriver(new Driver())帮我们创建并注册驱动,所以我们就不用再创建并注册一遍了。
     */
    private static void  test_connection_4()throws Exception{
        //1. 加载驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 获取连接所需要的url,用户名和密码
        String url = "jdbc:mysql://localhost:3306/dongfeng_test?serverTimezone=UTC";
        String user = "root";
        String password = "zdf123";

        //2. 获取数据库连接
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }


    /**
     * 获取数据库连接方式五(final版): 将数据库连接所需的四个基本信息声明在配置文件中,读取配置文件
     * 此方式的好处:
     *      1. 实现了数据与代码的分离: 实现了解耦;
     *      2. 如果需要修改配置文件信息,可以避免配置文件重新打包;
     */
    private static void test_connection_5()throws Exception{

        // 1. 读取配置文件中的信息
        // 用当前类的类加载器(ConnectionDemo.class.getClassLoader()): 应用类加载器(Application ClassLoader)加载配置文件
        final InputStream in = ConnectionDemo.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(in);

        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");
        final String url = properties.getProperty("url");
        final String driverClass = properties.getProperty("driverClass");

        //2. 加载驱动类
        Class.forName(driverClass);

        //3. 获取数据库连接
        final Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

}

