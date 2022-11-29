package com.dongfeng.study.basicstudy.servlet;

/**
 * 多个Servlet之间数据共享实现方案
 *
 * @author eastFeng
 * @date 2022-11-29 2:50
 */
public class ShareDataInServlets_08 {

    /**
     * 多个Servlet之间数据共享实现方案
     * <p> 数据共享：是指OneServlet工作完毕后，将产生数据交给TwoServlet来使用。
     *
     * <p> Servlet规范中提供四种数据共享方案：
     * <ol>
     *     <li>ServletContext接口。{@link ServletContext_09}</li>
     *     <li>Cookie类。{@link Cookie_10}</li>
     *     <li>HttpSession接口</li>
     *     <li>HttpServletRequest接口</li>
     * </ol>
     *
     */
    public static void main(String[] args) {
    }

}
