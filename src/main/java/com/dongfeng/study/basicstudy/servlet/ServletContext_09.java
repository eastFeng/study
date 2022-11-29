package com.dongfeng.study.basicstudy.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ServletContext接口学习
 *
 * @author eastFeng
 * @date 2022-11-29 17:44
 */
@Slf4j
@WebServlet(name = "ServletContext_09", urlPatterns = "/servlet/09")
public class ServletContext_09 extends HttpServlet {
    private static final long serialVersionUID = 5715354336766870063L;

    /**
     * ServletContext接口介绍：
     * <ol>
     *     <li>来自于Servlet规范中一个接口。Tomcat负责提供这个接口实现类。</li>
     *     <li>如果两个Servlet来自于同一个网站。彼此之间通过网站的ServletContext实例对象实现数据共享。</li>
     *     <li>开发人员习惯于将ServletContext对象（ServletContext接口实现类的实例对象）称为【全局作用域对象】。</li>
     * </ol>
     *
     * <p> 每一个web应用都有且仅有一个ServletContext对象，又称Application对象。
     * 从名称中可知，该对象是与应用程序相关的。
     * 在WEB容器启动的时候，会为每一个WEB应用程序创建一个对应的ServletContext对象。
     * <p> 这个全局作用域对象【相当于】一个Map。
     * 在这个网站中OneServlet可以将一个数据存入到全局作用域对象，
     * 当前网站中其他Servlet此时都可以从全局作用域对象得到这个数据进行使用。
     * 【注】不建议存放过多数据，因为ServletContext中的数据一旦存储进去没有手动移除将会一直保存。
     *
     * <p> 该对象有两大作用：
     * <ol>
     *     <li>作为域对象用来共享数据，此时数据在整个应用程序中共享。</li>
     *     <li>该对象中保存了当前应用程序相关信息。
     *        例如可以通过getServerInfo()方法获取当前服务器信息，
     *        getRealPath(String path)获取资源的真实路径等。</li>
     * </ol>
     *
     * <p> 全局作用域对象（ServletContext对象：ServletContext接口实现类的实例对象）生命周期：
     * <ol>
     *     <li>在Http服务器启动过程中，自动为当前网站在内存中创建一个全局作用域对象。</li>
     *     <li>在Http服务器运行期间时，一个网站只有一个全局作用域对象。</li>
     *     <li>在Http服务器运行期间，全局作用域对象一直处于存活状态。</li>
     *     <li>在Http服务器准备关闭时，负责将当前网站中全局作用域对象进行销毁处理。</li>
     * </ol>
     * 【注】全局作用域对象生命周期贯穿网站整个运行期间。
     *
     *
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取ServletContext对象的四种方式
        ServletContext servletContext = getServletContextFourWays(request);

        // 获取项目存放的真实路径
        String realPath = servletContext.getRealPath("/");
        // 获取当前服务器的版本信息
        String serverInfo = servletContext.getServerInfo();
        log.info("项目存放的真实路径:{}, 当前服务器的版本信息:{}", realPath, serverInfo);

        // 设置
        servletContext.setAttribute("name", "风从东来");
        // 获取
        String name = (String)servletContext.getAttribute("name");
        log.info("name:{}", name);
        // 移除
        servletContext.removeAttribute("name");
    }

    public ServletContext getServletContextFourWays(HttpServletRequest request){
        // 获取ServletContext对象的四种方式

        // 1.通过请求对象获取
        ServletContext servletContext = request.getServletContext();

        // 2.通过session对象获取
        ServletContext servletContext1 = request.getSession().getServletContext();

        // 3.通过ServletConfig对象获取
        ServletConfig servletConfig = getServletConfig();
        ServletContext servletContext2 = servletConfig.getServletContext();

        // 4.直接获取，Servlet 类中提供了直接获取 ServletContext 对象的方法
        ServletContext servletContext3 = getServletContext();
        return servletContext3;
    }
}
