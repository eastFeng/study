package com.dongfeng.study.basicstudy.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cookie学习
 *
 * @author eastFeng
 * @date 2022-11-27 1:24
 */
@Slf4j
@WebServlet(name = "Cookie_10", urlPatterns = "/servlet/10")
public class Cookie_10 extends HttpServlet {
    private static final long serialVersionUID = -4772525330120370101L;

    /**
     * Cookie介绍
     * <ol>
     *     <li>Cookie来自于Servlet规范中一个工具类。</li>
     *     <li>如果两个Servlet来自于【同一个网站】，并且为【同一个浏览器/用户】提供服务，
     *         此时借助于Cookie对象进行数据共享。</li>
     *     <li>Cookie存放当前用户的私人数据，在共享数据过程中提高服务质量。</li>
     * </ol>
     *
     * <p> Cookie是浏览器提供的一种技术，通过服务器的程序能将一些只须保存在客户端，
     * 或者在客户端进行处理的数据，放在本地的计算机上，不需要通过网络传输，
     * 因而提高网页处理的效率，并且能够减少服务器的负载，
     * 但是由于Cookie是服务器端保存在客户端的信息，所以其安全性也是很差的。
     * 例如常见的记住密码则可以通过 Cookie 来实现。
     * <p> 有一个专门操作Cookie的类 javax.servlet.http.Cookie。
     * 随着服务器端的响应发送给客户端，保存在浏览器。
     * 当下次再访问服务器时把Cookie再带回服务器。
     * <p> Cookie的格式：键值对用“=”链接，多个键值对间通过“；”隔开。
     *
     * <p> 原理：
     * 用户通过浏览器第一次向【网站，比如myWeb网站】发送请求，访问OneServlet。
     * OneServlet在运行期间创建一个Cookie存储与当前用户相关数据，
     * OneServlet工作完毕后，【将Cookie写入到响应头】交还给当前浏览器。
     * 浏览器收到响应包之后，将cookie存储在浏览器的缓存，
     * 一段时间之后，用户通过【同一个浏览器】再次向【myWeb网站】发送请求，访问TwoServlet时。
     * 【浏览器需要无条件的将myWeb网站之前推送过来的Cookie，写入到请求头】发送过去，
     * 此时TwoServlet在运行时，就可以通过读取请求头中cookie中信息，得到OneServlet提供的共享数据。
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**
         * cookie相当于一个map
         * 一个cookie中只能存放一个键值对
         * 这个键值对的key与value只能是String
         * 键值对中key不能是中文
         */
        // 创建Cookie
        Cookie userNameCookie = new Cookie("userName", "eastFeng");
        Cookie passwordCookie = new Cookie("password", "123");
        // 发送Cookie，交给浏览器
        response.addCookie(userNameCookie);
        response.addCookie(passwordCookie);

        // cookie可以在浏览器中看到，浏览器中F12进入开发者模式。

        // 获取Cookie
        Cookie[] cookies = request.getCookies();
        // 判断数组是否为空
        if (cookies!=null && cookies.length>0){
            // 遍历数组
            for (Cookie cookie : cookies){
                System.out.println(cookie.getName()+"="+cookie.getValue());
            }
        }

        /**
         * Cookie销毁时机:
         * 1.在默认情况下，Cookie对象存放在浏览器的缓存中。
         * 	 因此只要浏览器关闭，Cookie对象就被销毁掉
         * 2.在手动设置情况下，可以要求浏览器将接收的Cookie存放在客户端计算机上硬盘上，
         * 同时需要指定Cookie在硬盘上存活时间。
         * 在存活时间范围内，关闭浏览器、关闭客户端计算机、关闭服务器，都不会导致Cookie被销毁。
         * 在存活时间到达时，Cookie自动从硬盘上被删除。
         *
         * 通过 setMaxAge(int time);方法设定 cookie 的最大有效时间，以秒为单位。
         * 到期时间的取值
         * 1.负整数
         * 若为负数，表示不存储该 cookie。
         * cookie的maxAge属性的默认值就是-1，表示只在浏览器内存中存活，
         * 一旦关闭浏览器窗口，那么cookie就会消失。
         * 2.正整数
         * 若大于0的整数，表示存储的秒数。
         * 表示cookie对象可存活指定的秒数。当生命大于0时，浏览器会把Cookie保存到硬盘上，
         * 就算关闭浏览器，就算重启客户端电脑，cookie也会存活相应的时间。
         * 3.零
         * 若为0，表示删除该cookie。
         * cookie生命等于0是一个特殊的值，它表示cookie被作废！
         * 也就是说，如果原来浏览器已经保存了这个 Cookie，那么可以通过Cookie的setMaxAge(0)来删除这个 Cookie。
         * 无论是在浏览器内存中，还是在客户端硬盘上都会删除这个 Cookie。
         */
        // 创建Cookie对象
        Cookie testCookie = new Cookie("setMagAgeTest", "setMagAgeValue");
        // 设置该Cookie3天后失效。cookie的最大有效时间，以秒为单位。
        testCookie.setMaxAge(3*24*60*60);
        // 发送Cookie，交给浏览器
        response.addCookie(testCookie);





    }
}
