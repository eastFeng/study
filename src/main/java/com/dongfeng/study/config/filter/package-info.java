/**
 * @author eastFeng
 * @date 2021-04-08 23:03
 */
package com.dongfeng.study.config.filter;
/*
 *
 * 过滤器
 *
 * 原理：过滤器是基于函数回调来实现
 * 使用方法：实现{@link javax.servlet.Filter}接口。
 * 使用场景：设置字符编码，URL级别的权限访问控制，过滤敏感词汇，压缩响应信息。
 *
 * 过滤器Filter依赖于Servlet容器，基于回调函数，过滤范围大，还能进行资源过滤。
 * 拦截器Interceptor依赖于框架容器，基于反射机制，只过滤请求。
 * tomcat容器中执行顺序: Filter -> Servlet -> Interceptor -> Controller
 *
 * 1. 过滤器（Filter）介绍
 *    1）来自于Servlet规范下接口。
 *    2）Filter接口实现类由开发人员负责提供，Http服务器不负责提供。
 *    3)Filter接口在Http服务器调用资源文件之前，对Http服务器进行拦截
 * 2. 具体作用：
 *    1）拦截Http服务器，帮助Http服务器检测当前请求合法性
 *    2）拦截Http服务器，对当前请求进行增强操作
 */
