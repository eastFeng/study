package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.ImportResource;

/**
 *
 * {@link ImportResource}注解学习
 *
 * @author eastFeng
 * @date 2022-12-05 16:15
 */
public class ImportResourceStudy {

    /**
     * <p> 一. 使用场景
     * <p> 系统迁移：比如，在原有系统上做二次开发，原有系统是用xml文件形式声明的bean，
     *     现在二次开发准备用注解的形式声明bean。
     *     如何在使用注解的形式中把之前用xml声明的bean加载到Spring IoC容器？
     *     使用{@link ImportResource}注解。
     *
     * <p> 二. {@link ImportResource}注解：导入xml配置文件。
     * <p> 可以额外分为两种模式：相对路径classpath，绝对路径（真实路径）file。
     *
     * <p> 相对路径（classpath）
     * <ul>
     *     <li>引入单个xml配置文件：@ImportSource("classpath : xxx/xxxx.xml")</li>
     *     <li>引入多个xml配置文件：@ImportSource(locations={"classpath : xxxx.xml" , "classpath : yyyy.xml"})</li>
     * </ul>
     *
     * <p> 绝对路径（file）
     * <ul>
     *     <li>引入单个xml配置文件：@ImportSource(locations= {"file : d:/hellxz/dubbo.xml"})</li>
     *     <li>引入多个xml配置文件：@ImportSource(locations= {"file : d:/hellxz/application.xml" , "file : d:/hellxz/dubbo.xml"})</li>
     * </ul>
     *
     *
     */
    public static void main(String[] args) {
    }
}
