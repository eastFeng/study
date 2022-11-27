package com.dongfeng.study.basicstudy.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【推荐】
 * <p> SpringBoot整合Servlet不需要用xml方式进行配置，用javaConfig配置。
 * <p> SpringBoot整合Servlet方式一：通过注解扫描完成Servlet组件的注册
 * <p> 1. 创建Servlet，并且在类上@WebServlet注解
 * <p> 2. 修改启动类 ：加上@ServletComponentScan注解
 * <p> @ServletComponentScan注解：在SpringBoot启动时会扫描有@WebServlet、@WebFilter注解的类，并将该类实例化
 * @author eastFeng
 * @date 2021-09-27 0:08
 */
@WebServlet(name = "Servlet_02", urlPatterns = "/servlet/02")
public class SpringBootAndServlet_02 extends HttpServlet {
    private static final long serialVersionUID = 2302748873038021982L;

    // 访问该类的URL http://localhost:8081/servlet/02

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("SpringBootAndServlet_02 doGet URL="+req.getRequestURL().toString());

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("你好，GET请求，我是Servlet_02");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("SpringBootAndServlet_02 doPost URL="+req.getRequestURL().toString());

        // 获取Request域对象内容
        Object attribute = req.getAttribute(RequestAndResponse_05.REQUEST_ATTRIBUTE_1);
        System.out.println("SpringBootAndServlet_02 doPost 获取Request域中的数据，"
                +RequestAndResponse_05.REQUEST_ATTRIBUTE_1 + "=" + attribute);

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("你好，Post请求，我是Servlet_02");
    }
}
