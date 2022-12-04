package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eastFeng
 * @date 2022-12-02 17:50
 */
@Configuration
public class BeanConfiguration {

    /**
     * {@link Bean}：默认添加的bean的id为方法名。
     */
    @Bean
    public Person_04 getPerson_04(){
        return new Person_04();
    }

}
