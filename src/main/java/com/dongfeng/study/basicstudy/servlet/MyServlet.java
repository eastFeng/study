package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author eastFeng
 * @date 2022-11-24 16:20
 */
public class MyServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("我接受到了您的请求");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
