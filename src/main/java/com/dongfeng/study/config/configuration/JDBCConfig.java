package com.dongfeng.study.config.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * <p> {@link Configuration}: 指示一个类声明 一个或者多个{@link Bean}方法。
 * <p> {@link Configuration}用于定义配置类，定义的配置类可以替换xml文件，一般和{@link Bean}注解联合使用。
 * {@link PropertySource}注解：加载指定的properties配置文件
 *
 * @author eastFeng
 * @date 2021-09-27 1:38
 */
@PropertySource("classpath:/jdbc.properties")
@Configuration
public class JDBCConfig {
    @Value("${driverClass}")
    private String driverClass;
    @Value("${url}")
    private String url;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;

    private DataSource dataSource(){
        DruidDataSource source = new DruidDataSource();
        source.setDriverClassName(this.driverClass);
        source.setUrl(this.url);
        source.setUsername(this.user);
        source.setPassword(this.password);
        return source;
    }
}
