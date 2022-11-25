package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SpringBoot整合Servlet不需要用xml方式进行配置，用javaConfig配置。
 * <p> 整合Servlet方式二：通过方法完成Servlet组件的注册
 * <p> 1. 创建servlet
 * <p> 2. 创建servlet配置类：{@link com.dongfeng.study.config.configuration.ServletConfig}
 * @author eastFeng
 * @date 2021-09-27 0:23
 */
public class SpringBootAndServlet_03 extends HttpServlet {
    private static final long serialVersionUID = 5641127950925276950L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        System.out.println("Second Servlet...");
    }
}
