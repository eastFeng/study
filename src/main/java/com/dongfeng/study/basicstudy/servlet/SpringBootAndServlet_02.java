package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SpringBoot整合Servlet不需要用xml方式进行配置，用javaConfig配置。
 * <p> SpringBoot整合Servlet方式一：通过注解扫描完成Servlet组件的注册
 * <p> 1. 创建Servlet，并且在类上@WebServlet注解
 * <p> 2. 修改启动类 ：加上@ServletComponentScan注解
 * <p> @ServletComponentScan // 在SpringBoot启动时会扫描有@WebServlet、@WebFilter注解的类，并将该类实例化
 * @author eastFeng
 * @date 2021-09-27 0:08
 */
@WebServlet(name = "FirstServlet", urlPatterns = "/firstServlet")
public class SpringBootAndServlet_02 extends HttpServlet {
    private static final long serialVersionUID = 2302748873038021982L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        System.out.println("First Servlet...");
        System.out.println("First Servlet Request URI="+req.getRequestURI());
        System.out.println("First Servlet Request URL="+req.getRequestURL().toString());
    }
}
