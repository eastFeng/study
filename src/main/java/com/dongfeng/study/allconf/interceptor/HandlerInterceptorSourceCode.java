package com.dongfeng.study.allconf.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b> {@link org.springframework.web.servlet.HandlerInterceptor}拦截器接口源码学习 </b>
 *
 * <p> interceptor[ˌɪntəˈseptə(r)] : 拦截器。
 *
 * <p> spring中拦截器主要分两种，一个是HandlerInterceptor，一个是MethodInterceptor。
 *
 * <p> {@link org.springframework.web.servlet.HandlerInterceptor}是springMVC项目中的拦截器，它拦截的目标是请求的地址，
 * 比MethodInterceptor先执行。
 * {@link org.springframework.cglib.proxy.MethodInterceptor}
 *
 * <p> 实现一个HandlerInterceptor拦截器可以直接实现{@link org.springframework.web.servlet.HandlerInterceptor}接口，
 * 也可以继承{@link org.springframework.web.servlet.handler.HandlerInterceptorAdapter}类。
 *
 * @author eastFeng
 * @date 2021-04-08 23:14
 */
public interface HandlerInterceptorSourceCode {
    /**
     * <b> default方法: 实现接口时，可以选择不对default修饰的方法重写。
     * <p> 默认方法使用default关键字来修饰
     * <p> default修饰方法只能在接口中使用，在接口种被default标记的方法为普通方法，必须要写方法体。
     * <p> 接口中支持定义静态方法，将关键字换成static即可 </b>
     *
     * <p> 拦截处理程序的执行。在HandlerMapping确定适当的处理程序对象之后调用，但在HandlerAdapter调用处理程序之前调用。
     * <p> DispatcherServlet处理由任意数量的拦截器组成的执行链中的处理程序，处理程序本身位于末尾。
     * 使用此方法，每个拦截器可以决定中止执行链，通常发送HTTP错误或编写自定义响应。
     * <p> 注意：异步请求处理需要特别考虑。有关更多详细信息，请参阅{@link org.springframework.web.servlet.AsyncHandlerInterceptor}。
     * <p> 默认实现返回true。
     *
     * @param request 当前HTTP请求
     * @param response 当前HTTP响应
     * @param handler 为类型和/或实例计算选择要执行的处理程序
     * @return 如果执行链应继续处理下一个拦截器或处理程序本身，则为true。否则，DispatcherServlet假设这个拦截器已经处理了响应本身。
     * @throws Exception 如果出现错误
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {
    }
}
