package com.dongfeng.study.allconf.config;

import com.dongfeng.study.allconf.interceptor.LoginInterceptor;
import com.dongfeng.study.allconf.interceptor.TraceIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * <b>MVC配置</b>
 *
 * @author eastFeng
 * @date 2020-11-18 16:22
 */
@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    // field注入
//    @Autowired
    private LoginInterceptor loginInterceptor;
//    @Autowired
    private TraceIdInterceptor traceIdInterceptor;

    /**
     * 构造器注入
     * @param loginInterceptor 登录拦截器
     * @param traceIdInterceptor 日志traceId拦截器
     */
    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor, TraceIdInterceptor traceIdInterceptor){
        this.loginInterceptor = loginInterceptor;
        this.traceIdInterceptor = traceIdInterceptor;
    }

    /**
     * <b>添加拦截器</b>
     *
     * @param registry 拦截注册器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器
        registry.addInterceptor(loginInterceptor);
//                .excludePathPatterns("/test/**");

        // 添加日志traceId拦截器
        registry.addInterceptor(traceIdInterceptor);
    }
}
