package com.dongfeng.study.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author eastFeng
 * @date 2021-09-27 0:36
 */
@Slf4j
//@WebFilter(filterName = "firstFilter", urlPatterns = {"*.do", "*.jsp"})
@WebFilter(filterName = "firstFilter", urlPatterns = "/firstFilter")
public class FirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * 主要方法
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("进入FirstFilter...");
        chain.doFilter(request, response);
        log.info("离开FirstFilter...");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
