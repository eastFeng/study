package com.dongfeng.study.basicstudy.servlet;


import com.dongfeng.study.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * {@link javax.servlet.http.HttpServletResponse} 和
 * {@link javax.servlet.http.HttpServletRequest} 学习
 *
 * @author eastFeng
 * @date 2022-11-26 3:09
 */
@Slf4j
//@MultipartConfig
@WebServlet(name = "ServletStudy_05", urlPatterns = "/servlet/05")
public class ServletStudy_05 extends HttpServlet {
    private static final long serialVersionUID = -7522205658353885240L;

    // 公共接口HttpServletRequest继承自ServletRequest。
    // 客户端浏览器发出的请求被封装成为一个HttpServletRequest对象。
    // 服务器的响应被封装成为一个HttpServletResponse对象。

    // HttpServletRequest对象（接口实现类的对象）和HttpServletResponse在service方法中，
    // 都是由servlet容器传过来的，我们需要做的就是使用它们，取出对象中的数据进行分析、处理。



    /**
     * HttpServletResponse接口介绍:
     * <ol>
     *     <li>HttpServletResponse接口来自于Servlet规范中。</li>
     *     <li>HttpServletResponse接口实现类由Http服务器负责提供。</li>
     *     <li>HttpServletResponse接口负责将处理请求结果
     *     （doGet/doPost方法执行结果）写入到【响应体】交给浏览器。</li>
     *     <li>开发人员习惯于将HttpServletResponse接口实现类的对象称为【响应对象】</li>
     * </ol>
     *
     * HttpServletResponse主要功能:
     * <ol>
     *     <li>将执行结果以二进制形式写入到【响应体】。</li>
     *     <li>
     *     设置响应头中【content-type】属性值，告诉浏览器发送的响应体中的数据属于什么类型，
     *     从而控制浏览器使用对应/正确的编译器将【响应体二进制数据】编译为【文字，图片，视频，命令】。
     *     content-type：发送到客户端的响应的内容类型。
     *     </li>
     *     <li>
     *     设置响应头中【location】属性，将一个请求地址赋值给location。
     *     从而控制浏览器向指定服务器发送请求。【重定向】
     *     </li>
     * </ol>
     *
     * <p>【注】HTTP响应（Response）由三部分组成: 响应行 + 响应头 + 响应体。
     * <ol>
     *     <li>响应行（状态行）: HTTP协议版本（如HTTP/1.1）, 状态码（如200）, 状态描述(如OK)</li>
     *     <li>响应头（注：不区分大小写）的语法格式 key:value ,其中Content-Type是描述响应体中的数据类型（字，图片，视频，命令...）。</li>
     *     <li>响应体: 绝大多数不为空。（请求成功：会发数据；请求失败：会发错误信息）</li>
     * </ol>
     * <p>浏览器根据服务器返回的HTTP响应状态码，就能知道这次HTTP请求的结果是成功还是失败了。
     * HTTP状态码由三个十进制数字组成: 第一个十进制数字定义了状态码的类型，后两个数字用来对状态码进行细分。
     * <ul>
     *     <li>1** : 信息响应，服务器收到请求，需要请求者继续执行操作。</li>
     *     <li>2** : 成功响应，操作被成功接收并处理。</li>
     *     <li>3** : 重定向，需要进一步的操作以完成请求。</li>
     *     <li>4** : 客户端响应，请求包含语法错误或无法完成请求。</li>
     *     <li>5** : 服务器响应，服务器在处理请求的过程中发生了错误。</li>
     * </ul>
     *
     * HTTP响应中的常用响应头:
     * <ul>
     *     <li>Allow: 服务器支持哪些请求方法（如GET、POST等）。</li>
     *     <li>
     *         Location: 服务器通过这个头，来告诉浏览器跳到哪里（重定向）。
     *         Location通常不是直接设置的，而是通过HttpServletResponse的sendRedirect方法，
     *         该方法同时设置状态代码为302。
     *     </li>
     *     <li>Server: 服务器通过这个头，告诉浏览器服务器名字。Servlet一般不设置这个值，而是由Web服务器自己设置。</li>
     *     <li>Content-Encoding: 服务器通过这个头，告诉浏览器，响应体中数据的压缩格式。</li>
     *     <li>Content-Length: 服务器通过这个头，告诉浏览器回送数据（响应体中数据）的长度。</li>
     *     <li>Content-Type: 服务器通过这个头，告诉浏览器响应体中的数据类型（字，图片，视频，命令...）。</li>
     *     <li>Content-Disposition: 服务器通过这个头，告诉浏览器以下载方式打数据</li>
     *     <li>Refresh: 服务器通过这个头，告诉浏览器定时刷新，浏览器应该在多少时间之后刷新文档，以秒计。</li>
     * </ul>
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("doGet start 请求URL:{}", req.getRequestURL());
        // 1. 响应对象以二进制形式将执行结果写入响应体
//        responseTest1(resp);

        // 2. 设置响应头中[content-type]属性值，告诉浏览器发送的响应体中的数据属于什么类型，
        // 从而指定浏览器使用对应编译器将响应体二进制数据编译为【文字，图片，视频，命令】。
        // content-type：发送到客户端的响应的内容类型
//        responseTest2(resp);

        // 3. 设置响应头中【location】属性，将一个请求地址赋值给location。
        // 从而控制浏览器向指定服务器发送请求。【重定向】
        responseTest3(resp);

    }


    /**
     * HttpServletRequest接口介绍:
     * <ol>
     *     <li>HttpServletRequest接口来自于Servlet规范中。</li>
     *     <li>HttpServletRequest接口实现类由Http服务器负责提供。</li>
     *     <li>HttpServletRequest接口负责在doGet/doPost方法运行时读取Http请求协议包中信息。</li>
     *     <li>开发人员习惯于将HttpServletRequest接口实现类的对象称为【请求对象】。</li>
     * </ol>
     *
     * HttpServletRequest主要功能学习:
     * <ol>
     *     <li>可以读取Http请求协议包中【请求行】信息。</li>
     *     <li>可以读取保存在Http请求协议包中【请求头】或【请求体】中请求参数信息。</li>
     *     <li>可以代替浏览器向Http服务器申请资源文件调用。</li>
     * </ol>
     *
     * <p> 【注】HTTP请求（Request）由三部分组成: 请求行 + 请求头 + 请求体。
     * <ol>
     *     <li>请求行(Request Line): 包含HTTP版本、URL、请求方法。</li>
     *     <li>请求头（注：不区分大小写）的语法格式 key:value。请求头包含许多有关的客户端环境和请求正文的有用信息，如请求正文的长度等。</li>
     *     <li>
     *         请求体(Request Body): 请求头和请求体（请求正文）之间是一个空行，这个空行非常重要，它表示请求头已经结束，接下来的是请求体。
     *         请求体中可以包含客户提交的查询字符串信息，例如：username=zdf&password=zdf123
     *     </li>
     * </ol>
     * <p>不是所有的请求都有一个请求体(Request Body)，例如获取资源的请求：GET、HEAD、DELETE和OPTIONS，通常它们不需要 body。
     * 有些请求将数据发送到服务器以便更新数据：常见的的情况是POST请求（包含HTML表单数据）。
     * <p>GET请求和POST请求的对比:
     * <ul>
     *     <li>查询字符串（键值对）是在GET请求的URL中发送的（链接在URL后面）。</li>
     *     <li>查询字符串（键值对）是在POST请求的HTTP消息的请求体中发送的。</li>
     * </ul>
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("doPost start ===============");
        req.setCharacterEncoding("UTF-8");

        // http://localhost:8081/servlet/05?name=东东东&age=18
        // 1. 获取【请求行】中【URL】信息 （从http开始到?前面结束 http://localhost:8081/servlet/05）
        String Url = req.getRequestURL().toString();
        // 获取【请求行】中【Method】信息
        String method = req.getMethod();
        // 获取【请求行】中【URI】信息
        // URI: 资源文件精准定位地址（项目名称开始），在请求行中并没有URI这个属性，实际上是从URL中截取的一个字符串。
        // URI作用: 让HTTP服务器对被访问的资源文件进行定位
        String requestURI = req.getRequestURI();
        // 获取【请求行】中【HTTP版本】信息
        String protocol = req.getProtocol();

        // 获取客户端IP
        String remoteAddr = req.getRemoteAddr();
        // 获取客户端完整主机名
        String remoteHost = req.getRemoteHost();
        System.out.println("客户端IP: "+remoteHost+"  客户端主机名称: "+remoteHost);

        // 2. 获取此请求的所有【请求头Header】的name（名称）。如果请求没有请求头，则此方法返回空枚举。
        Enumeration<String> headerNames = req.getHeaderNames();
        System.out.println("HTTP请求中所有的请求头:");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            System.out.println(headerName + "=" + req.getHeader(headerName));
        }
        System.out.println();

        String contentType = req.getContentType();
        System.out.println("请求体内容类型: "+contentType);
        if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)){
            System.out.println("请求体内容类型contentType: "+contentType);
            // contentType是 multipart/form-data
            ServletInputStream in = req.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            String read = IOUtil.read(inputStreamReader, false);
            System.out.println(read);
        }

        // http://localhost:8081/servlet/05?name=东东东&age=18
        // 3. 获取请求的【查询字符串】（从?后面所有的字符串。 name=东东东&age=18）
        String queryString = req.getQueryString();
        System.out.println("查询字符串: "+queryString);
        System.out.println();

        // 4. 获取此请求的所有参数（从?后面所有的字符串的key）
        // 【注】getParameterNames方法不适用于contentType为multipart/form-data的情况
        // multipart 多部分
        Enumeration<String> parameterNames = req.getParameterNames();
        System.out.println("All Parameters:");
        while (parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            System.out.println(parameterName + "=" + req.getParameter(parameterName));
        }
        System.out.println();



        resp.setContentType(MediaType.TEXT_HTML_VALUE);
        String result =  "URL="+Url+"<br/>method="+method+"<br/>URI="+requestURI+"<br/>protocol="+protocol;
        // 写入响应体
        resp.getWriter().write(result);

    }

    // ------------------------- HttpServletRequest Test Start ----------------------------------

    private void responseTest1(HttpServletResponse resp)
            throws ServletException, IOException {
        // 1. 响应对象以二进制形式将执行结果写入响应体

        // 解决浏览器中文乱码
        resp.setCharacterEncoding("UTF-8");

        // 写入响应体
        // 获取响应对象的输出流并往输出流中写入内容
        resp.getWriter().write("执行结果测试");
        resp.getWriter().write(30);  // 浏览器接收到的数据不是30，
                                        // 因为write方法将int类型的30当做ASCII码写入了
        resp.getWriter().println(30);   // 换成println方法就行了，建议用这个方法
    }
    private void responseTest2(HttpServletResponse resp)
            throws ServletException, IOException {
        // 2. 设置响应头中[content-type]属性值，从而控制/指定浏览器使用
        // 对应编译器将响应体二进制数据编译为【文字，图片，视频，命令】
        // content-type：发送到客户端的响应体的内容类型

        // 2.1 设置发送到客户端的响应体的内容类型，指定浏览器使用正确编译器。
        // 这一步一定要在获取响应对象的输出流之前设置/执行/操作
        resp.setContentType(MediaType.TEXT_HTML_VALUE);

        // 解决浏览器中文乱码
        resp.setCharacterEncoding("UTF-8");

        // 执行结果既有文字信息又有HTML标签命令
        String result1 = "Java<br/>MySQL<br/>HTML<br/>";
        String result2 = "河南省<br/>商丘市<br/>夏邑县";
        // 2.2 获取响应对象的输出流并往输出流中写入内容
        // (响应对象以二进制形式将执行结果写入响应体)
        resp.getWriter().write(result1+result2);
    }

    private void responseTest3(HttpServletResponse resp)
            throws ServletException, IOException {
        // 3. 设置响应头中【location】属性，将一个请求地址赋值给location。
        // 从而控制浏览器向指定服务器发送请求。【重定向】

        String result = "https://www.baidu.com";

        // 设置响应头中【location】属性，location="https://www.baidu.com"
        // 重定向
        resp.sendRedirect(result);
    }


    // ------------------------- HttpServletRequest Test Start ----------------------------------

    private void requestTest1(HttpServletRequest req){

    }

}
