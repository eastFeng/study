package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eastFeng
 * @date 2022-12-04 14:44
 */
@Configuration
public class BeanConfiguration {

    /**
     * 会创建Book对象 而不是 BookFactoryBean对象。
     */
    @Bean
    public BookFactoryBean book(){
        return new BookFactoryBean();
    }
}
