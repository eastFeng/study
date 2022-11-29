package com.dongfeng.study.basicstudy.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 *
 * HttpSession学习
 *
 * @author eastFeng
 * @date 2022-11-29 19:35
 */
@Slf4j
@WebServlet(name = "HttpSession_11", urlPatterns = "servlet/11")
public class HttpSession_11 extends HttpServlet {
    private static final long serialVersionUID = -4306953010499005088L;

    /**
     * HttpSession介绍
     * <ol>
     *     <li>HttpSession接口来自于Servlet规范。其实现类由Http服务器提供。</li>
     *     <li>如果两个Servlet来自于同一个网站，并且为同一个浏览器/用户提供服务，
     *        此时借助于HttpSession对象进行数据共享</li>
     *     <li>开发人员习惯于将HttpSession接口修饰对象称为【会话作用域对象】。</li>
     * </ol>
     * <p> HttpSession对象是{@link javax.servlet.http.HttpSession}接口的实例，
     * 该接口并不像HttpServletRequest或HttpServletResponse还存在一个父接口，该接口只是一个纯粹的接口。
     * 这因为session本身就属于HTTP协议的范畴。
     * <p> 对于服务器而言，每一个连接到它的客户端都是一个session，
     * servlet容器使用此接口创建 HTTP客户端和HTTP服务器之间的会话。
     * 会话将保留指定的时间段，跨多个连接或来自用户的页面请求。
     * 一个会话通常对应于一个用户，该用户可能多次访问一个站点。
     * 可以通过此接口查看和操作有关某个会话的信息，比如会话标识符、创建时间和最后一次访问时间。
     * 在整个 session 中，最重要的就是属性的操作。
     * <p> session无论客户端还是服务器端都可以感知到，若重新打开一个新的浏览器，新浏览器则无法取得之前设置
     * 的session，因为每一个session只保存在当前的浏览器当中，并在相关的页面取得。
     * <p> Session的作用就是为了标识一次会话，或者说确认一个用户；
     * 并且在一次会话（一个用户的多次请求）期间共享数据。
     *
     * <p> HttpSession与Cookie区别：
     * <ol>
     *     <li>存储位置：Cookie存放在客户端计算机（浏览器内存/硬盘），HttpSession存放在服务端计算机内存。</li>
     *     <li>数据类型：Cookie对象存储共享数据类型只能是String，HttpSession对象可以存储任意类型的共享数据Object。</li>
     *     <li>数据数量：一个Cookie对象只能存储一个共享数据，HttpSession使用map集合存储共享数据，所以可以存储任意数量共享数据。</li>
     * </ol>
     *
     * <p> 标识符JSESSIONID
     * <p> Session既然是为了标识一次会话，那么此次会话就应该有一个唯一的标志，这个标志就是sessionId。
     * <p> 每当一次请求到达服务器，如果开启了会话（访问了session），服务器第一步会查看是否从客户端
     * 回传一个名为JSESSIONID的cookie，如果没有则认为这是一次新的会话，会创建 一个新的 session 对
     * 象，并用唯一的 sessionId 为此次会话做一个标志。如果有 JESSIONID 这 个cookie回传，服务器则会根
     * 据 JSESSIONID 这个值去查看是否含有id为JSESSION值的session 对象，如果没有则认为是一个新的会
     * 话，重新创建一个新的 session 对象，并标志此次会话； 如果找到了相应的 session 对象，则认为是之
     * 前标志过的一次会话，返回该 session 对象，数据达到共享。
     * <p> 这里提到一个叫做 JSESSIONID 的 cookie，这是一个比较特殊的 cookie，当用户请求服务器时，如果
     * 访问了 session，则服务器会创建一个名为 JSESSIONID，值为获取到的 session（无论是获取到的还是
     * 新创建的）的 sessionId 的 cookie 对象，并添加到 response 对象中，响应给客户端，有效时间为关闭
     * 浏览器。
     * <p> 所以Session的底层依赖Cookie来实现。
     *
     * <p> Session用来表示一次会话，在一次会话中数据是可以共享的，这时session 作为域对象存在，可以
     * 通过 setAttribute(name,value) 方法向域对象中添加数据，通过 getAttribute(name) 从域对象中获取
     * 数据，通过 removeAttribute(name) 从域对象中移除数据。
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()){
            String key = attributeNames.nextElement();
            Object value = session.getAttribute(key);
        }
    }
}
