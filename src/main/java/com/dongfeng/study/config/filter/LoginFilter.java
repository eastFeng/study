package com.dongfeng.study.config.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.dongfeng.study.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器
 *
 * @author eastFeng
 * @date 2021-09-27 0:36
 */
@Slf4j
//@WebFilter(filterName = "LoginFilter", urlPatterns = {"*.do", "*.jsp"})
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    private RedisUtil redisUtil;
    // 构造器注入
    @Autowired
    public LoginFilter(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    // 可以放行（不用登录校验）的URL，静态域只会在类加载的时候创建一次
    public static final String[] NOT_CHECK_URLS = {
            "/employee/login",
            "/employee/login",
            "/backend/**",
            "/front/**",
            "/user/sendMsg",
            "/user/login"
    };

    //
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * 主要方法
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param chain 过滤器链条
     * @throws IOException IO异常
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter 进入LoginFilter过滤器...");
        // 强转为HTTP请求对象和HTTP响应对象类型
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 从请求对象中获取URL
        String URL = req.getRequestURL().toString();
        log.info("doFilter request URL:{}", URL);

        boolean checkURL = checkURL(NOT_CHECK_URLS, URL);
        if (checkURL){
        }else {
        }

        // 放行
        chain.doFilter(request, response);
        log.info("doFilter 离开LoginFilter过滤器...");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    /**
     * @param notCheckUrls 可以放行的url地址数组
     * @param requestURL 客户端传来的请求URL（要检查的URL）
     * @return true：放行，false：不放行
     */
    public boolean checkURL(String[] notCheckUrls, String requestURL){
        for (String noCheckURL : notCheckUrls){
            if (ANT_PATH_MATCHER.match(noCheckURL, requestURL)) {
                // 匹配到了可以放行的路径
                return true;
            }
        }
        return false;
    }
}
