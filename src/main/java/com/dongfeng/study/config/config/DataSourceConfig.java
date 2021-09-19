package com.dongfeng.study.config.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author eastFeng
 * @date 2021-09-19 17:19
 */
@Configuration
public class DataSourceConfig {

    // 多数据源
    // 两个数据源，提供两个DataSource

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSourceOne(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.two")
    DataSource dataSourceTwo(){
        return DruidDataSourceBuilder.create().build();
    }
}
