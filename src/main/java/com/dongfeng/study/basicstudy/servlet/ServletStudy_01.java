package com.dongfeng.study.basicstudy.servlet;

import com.dongfeng.study.util.IOUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet学习
 *
 * @author eastFeng
 * @date 2022-11-25 16:54
 */
public class ServletStudy_01 extends HttpServlet {

    private static final long serialVersionUID = -7181578763212092521L;

    /**
     * <p>
     * Servlet（Server Applet）是Java Servlet的简称，称为小服务程序或服务连接器，用Java编写的服务器端程序，
     * 具有独立于平台和协议的特性，主要功能在于【交互式地浏览和生成数据，生成动态Web内容】。
     * 狭义的Servlet是指Java语言实现的一个接口，广义的Servlet是指任何实现了这个Servlet接口的类，
     * 一般情况下，人们将Servlet理解为后者。
     * Servlet运行于支持Java的应用服务器中。
     * 从原理上讲，Servlet可以响应任何类型的请求，但绝大多数情况下Servlet只用来扩展基于HTTP协议的Web服务器。
     * </p>
     * <p> 要实现Web开发，需要实现 Servlet 标准。
     * 提供了Servlet功能的服务器，叫做Servlet容器，其常见容器有很多，如 Tomcat, Jetty等等。</p>
     *
     * <p> 一. 关于Servlet, 就学习一下三点：
     * <ol>
     *   <li>在Servlet规范中，指定了【动态资源文件】开发步骤</li>
     *   <li>在Servlet规范中，指定了HTTP服务器调用动态资源文件规则</li>
     *   <li>在Servlet规范中，指定了HTTP服务器管理动态资源文件实例对象规则</li>
     * </ol>
     *
     * <p>
     * 二. 在Servlet规范中，HTTP服务器(比如Tomcat)能调用的【动态资源文件】
     * 必须是{@link javax.servlet.Servlet}接口的实现类。
     * </p>
     *
     * <p> 三. {@link javax.servlet.Servlet}接口实现类开发步骤 </p>
     * <p>【注意】</p>
     * <p> Servlet可以在任何协议下访问,写的servlet必须实现Servlet接口 </p>
     * {@link javax.servlet.Servlet}接口的实现类，
     * 最主要的是实现其中的{@link javax.servlet.Servlet#service(ServletRequest, ServletResponse)}方法，
     * 因为该方法是收到请求后处理请求的方法（负责响应客户端的请求），
     * 当容器接收到客户端要求访问特定Servlet对象的请求时，会调用该Servlet对象的service()方法，每次请求都会执行。
     * <p> 而为了简化开发，官方中提供了一个实现Servlet接口的抽象类{@link javax.servlet.GenericServlet}，
     * 该类对{@link GenericServlet#init(ServletConfig)}、{@link GenericServlet#getServletConfig()}、
     * {@link GenericServlet#getServletInfo()}、{@link GenericServlet#destroy()}这4个方法提供了默认空实现，
     * 但是对{@link GenericServlet#service(ServletRequest, ServletResponse)}没有提供默认实现。
     * 所以，我们可以不直接实现（implements）Servlet接口，而去继承GenericServlet抽象类，实现service方法即可。
     *
     * <p>
     * 官方针对HTTP协议（Web访问）专门提供了一个抽象类：{@link javax.servlet.http.HttpServlet}，
     * 该类继承于{@link javax.servlet.GenericServlet}类，在其基础上针对HTTP的特点进行扩充。
     * 该类重写了{@link javax.servlet.http.HttpServlet#service(ServletRequest, ServletResponse)}方法【一定要看看源码】，
     * 将{@link javax.servlet.Servlet}定义的参数{@link javax.servlet.ServletRequest}和{@link ServletResponse}
     * 分别转换成了{@link javax.servlet.http.HttpServletRequest}和{@link javax.servlet.http.HttpServletResponse}，
     * 并调用了自己的方法：{@link javax.servlet.http.HttpServlet#service(HttpServletRequest, HttpServletResponse)}，
     * 在{@link javax.servlet.http.HttpServlet#service(HttpServletRequest, HttpServletResponse)}方法中
     * 根据不同的请求方式进行分发，分别调用了
     * {@link javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}，
     * {@link javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)}，
     * {@link javax.servlet.http.HttpServlet#doHead(HttpServletRequest, HttpServletResponse)}等方法处理请求。
     * 【因此】，我们一般情况下写Servlet接口实现类都是继承自HttpServlet抽象类的，在写请求处理逻辑时，
     * 只需要重写HttpServlet抽象类的doGet和doPost等方法即可，并在doGet和doPost等方法中写请求处理逻辑（响应客户端请求）。
     * 父类HttpServlet会决定何种情况下调用子类的doGet和doPost等方法。模板设计模式）
     * <p>
     * 所以开发步骤是：
     * <ol>
     *     <li>创建一个Java类继承{@link javax.servlet.http.HttpServlet}抽象类，
     *         使之成为{@link javax.servlet.Servlet}接口实现类。
     *     </li>
     *     <li>重写父类{@link javax.servlet.http.HttpServlet}的doGet、doPost等方法。</li>
     *     <li>将Servlet接口实现类信息【注册】到Tomcat服务器</li>
     * </ol>
     *
     * <p> 如何将Servlet接口实现类信息【注册】到Tomcat服务器？ </p>
     * <p> 注册就是通知的意思。如何通知？ </p>
     * <p> web ——> WEB-INF ——> web.xml </p>
     * 【注意】IDEA新建的springboot项目是默认没有web的，所以要自己添加配置出来。
     * web要放在src/main/目录下。
     * 教程:https://blog.csdn.net/weixin_46713508/article/details/119804112
     * <p> 将Servlet实现类的信息写入web.xml就完成了注册/通知。
     * 写法示例：{@link ServletStudy_01#registerServletShow()} 或者 直接看WEB-INF下的web.xml文件</p>
     * <p></p>
     * <p> 在SpringBoot项目中，xml形式配置方式已经被淘汰了，已经换成了JavaConfig或注解来配置进行注册。 </p>
     *
     *
     *
     */
    public static void main(String[] args) {
    }

    /**
     * 重写父类doGet方法，处理Get请求
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     *             一个HttpServletRequest对象，包含客户端对servlet的请求
     *
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     *             一个HttpServletResponse对象，包含servlet发送给客户端的响应
     *
     * @throws ServletException if the request for the GET could not be handled
     *                          如果无法处理GET请求
     *
     * @throws IOException if an input or output error is detected
     *                     when the servlet handles the GET request
     *                     如果servlet处理GET请求时检测到输入或输出错误
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletStudy_01类针对客户端发送的GET请求进行处理");

        PrintWriter writer = resp.getWriter();
        writer.write("你好，GET请求，我是ServletStudy_01");
        IOUtil.close(writer);
    }

    /**
     * 重写父类doPost方法，处理Post请求
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletStudy_01类针对客户端发送的POST请求进行处理");

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("你好，GET请求，我是ServletStudy_01");
    }



    private void registerServletShow(){
        /*
         *
         * 在web.xml写俩信息就可以将Servlet实现类的信息注册到（通知给）Tomcat服务器了。
         * 第一步：
         * <!--将Servlet接口实现类类路径地址交给Tomcat-->
         * <servlet>
         *    <!--将Servlet接口实现类类路径地址交给Tomcat-->
         *    <!--声明一个变量存储servlet接口实现类类路径-->
         * 	  <servlet-name>ServletStudy_01</servlet-name>
         * 	  <!--Servlet接口实现类类路径-->
         *    <servlet-class>com.dongfeng.study.basicstudy.servlet.ServletStudy_01</servlet-class>
         * </servlet>
         *
         * 第二步：
         * <!--声明一个映射：将一个servlet-name（也就是一个Servlet接口实现类）和一个请求路径映射起来。-->
         * <!--也就是给Servlet接口实现类设置一个请求路径（url）-->
         * <servlet-mapping>
         *    <servlet-name>ServletStudy_01</servlet-name>
         *    <!--设置简短请求别名（路径）,别名（路径）在书写时必须以"/"为开头-->
         * 	  <url-pattern>/servletStudy/01</url-pattern>
         * </servlet-mapping>
         *
         *
         *
         */
    }
}
