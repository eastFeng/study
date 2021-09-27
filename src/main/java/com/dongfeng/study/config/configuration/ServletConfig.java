package com.dongfeng.study.config.configuration;

import com.dongfeng.study.basicstudy.servlet.SecondServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet配置类
 * @author eastFeng
 * @date 2021-09-27 0:27
 */
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<SecondServlet> registrationBean(){
        ServletRegistrationBean<SecondServlet> bean =
                new ServletRegistrationBean<>(new SecondServlet());
        bean.addUrlMappings("/secondServlet");
        return bean;
    }
}























