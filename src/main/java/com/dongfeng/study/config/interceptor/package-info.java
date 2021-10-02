/**
 * <b> HandlerInterceptor拦截器 </b>
 * <p> 拦截器是Spring MVC中的技术
 * <p> 原理：拦截器是基于java反射机制(动态代理)实现
 * <p> 使用方法：实现{@link org.springframework.web.servlet.HandlerInterceptor}接口。
 * 也可以继承{@link org.springframework.web.servlet.handler.HandlerInterceptorAdapter}类。
 * <p> 使用场景：处理所有请求的共同问题(乱码问题，权限验证问题)
 *
 * <p> 拦截器与过滤器区别：
 * <p> 1. 过滤器Filter依赖于Servlet容器，基于回调函数，过滤范围大，还能进行资源过滤。
 * <p> 2. 拦截器Interceptor依赖于框架容器，基于反射机制，只过滤请求
 * <p> 3. tomcat容器中执行顺序: Filter -> Servlet -> Interceptor -> Controller
 *
 * <p> Filter是Servlet规范规定的，不属于spring框架，也是用于请求的拦截。
 * 但是它适合更粗粒度的拦截，在请求前后做一些编解码处理、日志记录等。
 * 而拦截器则可以提供更细粒度的，更加灵活的，针对某些请求、某些方法的组合的解决方案。
 *
 * @author eastFeng
 * @date 2021-04-08 22:58
 */
package com.dongfeng.study.config.interceptor;
