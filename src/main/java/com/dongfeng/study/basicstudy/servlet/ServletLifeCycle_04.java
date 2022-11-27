package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet对象（Servlet接口实现类的对象）生命周期
 *
 *
 * @author eastFeng
 * @date 2022-11-26 2:05
 */
@WebServlet(name = "ServletStudy_04", urlPatterns = "/servlet/04")
public class ServletLifeCycle_04 extends HttpServlet {

    private static final long serialVersionUID = -6460868546878657349L;

    /*
     *
     * Servlet对象（Servlet接口实现类的对象）生命周期
     *
     * 1. 所有的Servlet接口实现类的实例对象，只能由Http服务器负责创建，
     *    开发人员不能手动创建Servlet接口实现类的实例对象。
     * 2. 在默认的情况下，Http服务器接收到对于当前Servlet接口实现类第一次请求时，
     *    自动创建这个Servlet接口实现类的实例对象。
     *    可以手动配置，要求Http服务器在启动时自动创建某个Servlet接口实现类的对象。
     * 3. 在Http服务器运行期间，一个Servlet接口实现类只能被创建出一个实例对象。（单例模式）
     * 4. 在Http服务器关闭时刻，自动将所有的Servlet对象(Servlet接口实现类的对象)进行销毁。
     *
     *
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //
        System.out.println("ServletStudy_04 doGet");
        System.out.println("ServletStudy_04 Request URI="+req.getRequestURI());
        System.out.println("ServletStudy_04 Request URL="+req.getRequestURL().toString());

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("你好，GET请求，我是Servlet_02");
    }
}
