package com.dongfeng.study.config.configuration;

import com.dongfeng.study.bean.enums.MultipleDataSourceEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源/数据库、多数据源/数据库切换
 * <p> 通过继承{@link AbstractRoutingDataSource}（路由数据源）来实现动态数据源的功能
 * <p> {@link AbstractRoutingDataSource}抽象类有三个重要的成员变量：
 * <ol>
 *     <li>{@link AbstractRoutingDataSource#targetDataSources} : 所有数据源（需指定），是个Map集合</li>
 *     <li>{@link AbstractRoutingDataSource#defaultTargetDataSource} : 默认数据源（需指定），是个Object类型</li>
 *     <li>{@link AbstractRoutingDataSource#resolvedDataSources} : 是个Map集合。
 *     会赋值上{@link AbstractRoutingDataSource#targetDataSources}的值，我们不用管。
 *     在继承{@link AbstractRoutingDataSource}的类对象创建/初始化的时候初始化的时候，
 *     会调用{@link AbstractRoutingDataSource#afterPropertiesSet()}方法将
 *     {@link AbstractRoutingDataSource#targetDataSources}的值赋值给
 *     {@link AbstractRoutingDataSource#resolvedDataSources}
 *     </li>
 * </ol>
 *
 * <p>
 *     {@link org.springframework.beans.factory.InitializingBean}接口为bean提供了初始化方法的方式，
 *     它只包括{@link InitializingBean#afterPropertiesSet()}方法，
 *     凡是继承该接口的类，在初始化bean的时候都会执行该方法。
 * </p>
 *
 * <p>
 *     {@link AbstractRoutingDataSource#determineTargetDataSource()}方法会确定目标（最终）的数据源，
 *     该方法通过调用抽象方法{@link AbstractRoutingDataSource#determineCurrentLookupKey()}方法决定采用最终的Connection
 *     （数据库连接），抽象方法{@link AbstractRoutingDataSource#determineCurrentLookupKey()}方法
 *     需要我们继承{@link AbstractRoutingDataSource}抽象类时来实现它。
 * </p>
 *
 *
 * <p>
 *     在{@link DataSourceConfig}配置类中也有两个{@link javax.sql.DataSource}类型的类，
 *     加上本类{@link DynamicDataSource}就有三个{@link javax.sql.DataSource}类型的类，
 *     注入的时候需要指定主要的@link javax.sql.DataSource}类型的类，就需要{@link Primary}注解。
 * </p>
 * <p>
 *     {@link Primary}注解：意思是在众多相同的bean中，优先使用用@Primary注解的bean。
 * </p>
 * @author eastFeng
 * @date 2022-11-22 13:30
 */
@Component
@Primary // 该项目有多个Primary注解会将该Bean设置为主要的DataSource注入Bean
public class DynamicDataSource extends AbstractRoutingDataSource {


    // 当前使用的数据源标识
    public static ThreadLocal<String> name=new ThreadLocal<>();

    /**
     * 多个数据源，在{@link DataSourceConfig}配置类中配置好了
     */
    // 数据源1
    DataSource dataSourceOne;
    // 数据源2
    DataSource dataSourceTwo;
    // 构造方法注入
    DynamicDataSource(DataSource dataSourceOne, DataSource dataSourceTwo){
        this.dataSourceOne = dataSourceOne;
        this.dataSourceTwo = dataSourceTwo;
    }

    /**
     * 需要实现的方法。
     * <p>
     *   该方法用来返回当前数据源标识
     * </p>
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    /**
     * 该方法是初始化方法，在该方法中设置
     * {@link AbstractRoutingDataSource#targetDataSources}（所有数据源）和
     * {@link AbstractRoutingDataSource#defaultTargetDataSource} : 默认数据源的值。
     * <p>
     *     {@link org.springframework.beans.factory.InitializingBean}接口为bean提供了初始化方法的方式，
     *    它只包括{@link InitializingBean#afterPropertiesSet()}方法，
     *    凡是继承该接口的类，在初始化bean的时候都会执行该方法。
     * </p>
     */
    @Override
    public void afterPropertiesSet(){

        // 1. 设置targetDataSources(所有数据源)的值
        Map<Object, Object> targetDataSources=new HashMap<>();
        targetDataSources.put(MultipleDataSourceEnum.DATA_SOURCE_1.name(),dataSourceOne);
        targetDataSources.put(MultipleDataSourceEnum.DATA_SOURCE_2.name(), dataSourceTwo);
        // 通过调用父类（AbstractRoutingDataSource）的setTargetDataSources方法
        // 设置targetDataSources(所有数据源)的值
        super.setTargetDataSources(targetDataSources);

        // 2. 设置defaultTargetDataSource(默认数据源)的值
        // 通过调用父类（AbstractRoutingDataSource）的setDefaultTargetDataSource方法
        // 设置defaultTargetDataSource(默认数据源)的值
        super.setDefaultTargetDataSource(dataSourceOne);

        super.afterPropertiesSet();
    }

}
