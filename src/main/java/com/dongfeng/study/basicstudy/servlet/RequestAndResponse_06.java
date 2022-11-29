package com.dongfeng.study.basicstudy.servlet;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 请求对象（HttpServletRequest接口实现类的实例对象）和
 * 响应对象（HttpServletResponse接口实现类的实例对象）的生命周期
 *
 * @author eastFeng
 * @date 2022-11-28 0:24
 */
public class RequestAndResponse_06 {
    /**
     * 请求对象和响应对象生命周期
     * <p>
     * 1.在Http服务器接收到浏览器发送的【Http请求协议包】之后，
     *  自动为当前的【Http请求协议包】生成一个【请求对象】和一个【响应对象】。
     * <p>
     * 2.在Http服务器调用doGet/doPost方法时，负责将【请求对象】和【响应对象】
     * 	 作为实参传递到方法，确保doGet/doPost正确执行。
     * <p>
     * 3.在Http服务器准备推送Http响应协议包之前，负责将本次请求关联的【请求对象】和【响应对象】销毁。
     * <p>
     * ***【请求对象】和【响应对象】生命周期贯穿一次请求的处理过程中 ***
     * <p>
     * ***【请求对象】和【响应对象】相当于用户在服务端的代言人 ***
     *
     */

    public void difference() throws ServletException, IOException {
        /**
         * 请求转发与重定向的区别:
         *
         *           请求转发                                 重定向
         * req.getRequestDispatcher().forward()      resp.sendRedirect()
         * 一次请求，数据在request域中共享               两次请求，request域中数据不共享
         * 服务端行为                                  客户端行为
         * 地址栏不发生变化                             地址栏发生变化
         * 绝对地址定位到站点后                          绝对地址可写到http://
         *
         * 【注】两者都可进行跳转，根据实际需求选取即可。
         */
        // 重定向
        new RequestAndResponse_05().responseTest3(null);
        // 请求转发
        new RequestAndResponse_05().requestTest6(null, null);

    }





}
