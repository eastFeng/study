package com.dongfeng.study.config.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * <b> {@link javax.servlet.Filter}过滤器接口源码学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-08 23:25
 */
public interface FilterSourceCode {

    public default void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException;

    public default void destroy() {}
}
