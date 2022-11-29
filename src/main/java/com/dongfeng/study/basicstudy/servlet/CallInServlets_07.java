package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 多个servlet之间调用规则
 *
 * @author eastFeng
 * @date 2022-11-29 2:45
 */
public class CallInServlets_07 {
    public static void main(String[] args) {
        /**
         * 多个Servlet之间调用规则:
         *
         * 1.前提条件：
         * 	 些来自于浏览器发送请求，往往需要服务端中多个Servlet协同处理。
         * 	 但是浏览器一次只能访问一个Servlet，导致用户需要手动通过浏览器发起多次请求才能得到服务。
         * 	 这样增加用户获得服务难度，导致用户放弃访问当前网站。
         *
         * 2.提高用户使用感受规则：
         *  无论本次请求涉及到多少个Servlet,用户只需要【手动】通知浏览器发起一次请求即可。
         *
         * 3.多个Servlet之间调用规则：
         * 	  1）重定向解决方案
         *    2）请求转发解决方案
         */
        // 重定向解决方案
        redirect();
        // 请求转发解决方案
        forward();
    }

    /**
     * 一.重定向解决方案工作原理
     * <p> 用户第一次通过【手动方式】通知浏览器访问OneServlet。
     * OneServlet工作完毕后，将TwoServlet地址写入到响应头location属性中，
     * 导致Tomcat将302状态码写入到状态行。
     * 在浏览器接收到响应包之后，会读取到302状态。
     * 此时浏览器自动根据响应头中location属性地址发起第二次请求，
     * 访问TwoServlet去完成请求中剩余任务
     *
     * <p> 二.实现命令
     * <p> response.sendRedirect("请求地址")  // 将地址写入到响应包中响应头中location属性
     *
     * <p> 三.特征
     * <ol>
     *     <li>请求地址：既可以把当前网站内部的资源文件地址发送给浏览器（/网站名/资源文件名，比如/servlet/02），
     *         也可以把其他网站资源文件地址发送给浏览器(http://ip地址:端口号/网站名/资源文件名，比如http://www.baidu.com)。
     *     </li>
     *     <li>请求次数： 浏览器至少发送两次请求，但是只有第一次请求是用户手动发送。
     *         后续请求都是浏览器自动发送的。</li>
     *     <li>请求方式（请求方法）：重定向解决方案中，通过地址栏通知浏览器发起下一次请求，因此，
     *         通过重定向解决方案调用的资源文件接收的请求方式一定是【GET】。</li>
     * </ol>
     *
     * <p> 四.缺点
     * <p> 重定向解决方案需要在浏览器与服务器之间进行多次往返，
     * 	  大量时间消耗在往返次数上，增加用户等待服务时间。
     *
     * <p>五.用法举例：{@link RequestAndResponse_05#responseTest3(HttpServletResponse)}</p>
     */
    public static void redirect(){
    }


    /**
     * 一.请求转发解决方案原理
     * <p> 用户第一次通过手动方式要求浏览器访问OneServlet。
     * OneServlet工作完毕后，通过当前的请求对象（request）代替浏览器向Tomcat发送请求，
     * 申请调用TwoServlet。
     * Tomcat在接收到这个请求之后，自动调用TwoServlet来完成剩余任务。
     * <p> 二.实现命令
     * <p> 请求对象代替浏览器向Tomcat发送请求：
     * <pre>
     * // 1.通过当前请求对象生成资源文件申请报告对象
     * RequestDispatcher  report = request.getRequestDispatcher("/资源文件名");一定要以"/"为开头
     * //2.将报告对象发送给Tomcat
     * report.forward(当前请求对象，当前响应对象)
     * </pre>
     *
     * <p> 三.优点：
     * <ol>
     *     <li>无论本次请求涉及到多少个Servlet,用户只需要手动通过浏览器发送一次请求。</li>
     *     <li>Servlet之间调用发生在服务端计算机上，节省服务端与浏览器之间往返次数，增加处理服务速度。</li>
     * </ol>
     * <p> 四.特征：
     * <ol>
     *     <li>请求次数：在请求转发过程中，浏览器只发送一次请求。</li>
     *     <li>请求地址：只能向Tomcat服务器申请调用当前网站的资源文件地址。
     * 		   request.getRequestDispatcher("/资源文件名") 【注：不要写网站名】
     * 	   </li>
     * 	   <li>请求方式（请求方法）：在请求转发过程中，浏览器只发送一个了个Http请求协议包。
     * 		   参与本次请求的所有Servlet共享同一个请求协议包，因此，
     * 		   这些Servlet接收的请求方式与浏览器发送的请求方式保持一致。</li>
     * </ol>
     *
     * <p> 五.用法举例：{@link RequestAndResponse_05#requestTest6(HttpServletRequest, HttpServletResponse)}
     *
     */
    public static void forward(){
    }
}
