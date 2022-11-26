package com.dongfeng.study.config.configuration;

import com.dongfeng.study.basicstudy.servlet.SpringBootAndServlet_03;
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
    public ServletRegistrationBean<SpringBootAndServlet_03> registrationBean(){
        ServletRegistrationBean<SpringBootAndServlet_03> bean =
                new ServletRegistrationBean<>(new SpringBootAndServlet_03());
        bean.addUrlMappings("/servlet/03");
        return bean;
    }
}























