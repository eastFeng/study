package com.dongfeng.study.basicstudy.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author eastFeng
 * @date 2020/4/24 - 16:01
 */
public abstract class BaseDao<T> {
    private QueryRunner queryRunner = new QueryRunner();
    /**
     * 定义一个变量来接收泛型的类型
     */
    private Class<T> type;

    /**
     * 空参构造方法
     * 获取T的Class对象(获取泛型的类型), 泛型是被子类继承时才确定
     */
    public BaseDao(){
        // 获取子类的类型: this指代的是BaseDao<T>的子类
        Class<? extends BaseDao> aClass = this.getClass();

        /**
         * 获取父类的类型: BaseDao<T>
         * getGenericSuperclass: 用来获取当前类带泛型的父类的类型
         * ParameterizedType: 表示带泛型类型
         */
        ParameterizedType parameterizedType = (ParameterizedType) aClass.getGenericSuperclass();

        /**
         * 获取类型: BaseDao<T>中的泛型T
         * getActualTypeArguments: 获取具体的泛型类型, 该方法返回Type类型的数组;
         */
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        //
        this.type = (Class<T>) actualTypeArguments[0];
    }

    /**
     * 通用的增删改操作
     */
    public int commonUpdate(Connection connection, String sql, Object... params){
        int count = 0;
        try {
            count = queryRunner.execute(connection, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取一个对象
     */
    public T getBean(Connection connection, String sql, Object... params){
        T t = null;
        try {
            t = queryRunner.query(connection, sql, new BeanHandler<>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取所有对象
     */
    public List<T> getBeanList(Connection connection, String sql, Object... params){
        List<T> tList = null;
        try {
            tList = queryRunner.query(connection, sql, new BeanListHandler<>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tList;
    }


}
