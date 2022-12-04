package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.EnvironmentCapable;

import org.springframework.context.MessageSource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Spring的IoC Container（Ioc容器）
 * <p> 简称为Spring容器
 *
 * @author eastFeng
 * @date 2022-12-03 0:54
 */
public class IoCContainer_03 {

    /**
     * 一. <b>{@link BeanFactory}接口</b>
     * <p> 用于访问Spring的Ioc容器的根接口，提供了完整的Ioc支持，
     * 能够管理任何类型的对象，提供了基本功能。
     * <p> {@link BeanFactory}接口有很多的实现类，每个实现类都有不同的职责，
     * 最强大的实现类是：{@link DefaultListableBeanFactory}，Spring底层就是使用它来生产bean的。
     *
     * <p> 二. <b>{@link ApplicationContext}（应用程序上下文）接口</b>
     * <p> {@link ApplicationContext}是{@link BeanFactory}的子接口。
     * <p> 它添加/补充了以下功能/特性：
     *   <ul>
     *      <li>更容易与Spring的AOP特性集成。</li>
     *      <li>消息资源处理（用于国际化）。</li>
     *      <li>事件发布。</li>
     *      <li>应用程序层特定上下文，如web应用程序中使用的WebApplicationContext。</li>
     *   </ul>
     * {@link ApplicationContext}添加的这些功能是【通过继承其他接口（增加其他接口的功能）】来实现的。
     * 看源码就可以看到继承的其他接口。
     * <ul>
     *     <li>{@link EnvironmentCapable} ：获取环境信息</li>
     *     <li>{@link MessageSource}：支持国际化</li>
     *     <li>{@link  ApplicationEventPublisher}：发布事件，也就是把某个事件告诉所有与这个事件相关的监听器</li>
     * </ul>
     * <p> 简而言之，BeanFactory提供了基本功能，ApplicationContext添加了企业特定的功能。
     *
     * <p> ApplicationContext创建单例对象的时候，默认使用恶汉模式，当容器启动后就会创建对象。
     * BeanFactory默认则是懒汉模式，获取对象的时候才去实例化对象。
     *
     * <p> Spring提供了ApplicationContext接口的几个实现类，常用的实现类有一下3个：
     * <p> 1. {@link ClassPathXmlApplicationContext}：
     * 该类可以加载类路径下的配置文件，要求配置文件必须在类路径下。不在的话，加载不了。
     *
     * <p> 2. {@link FileSystemXmlApplicationContext}：它可以加载磁盘任意路径下的配置文件。
     * <p> 3. {@link AnnotationConfigApplicationContext}：它是用于读取注解创建容器的。
     */
    public static void main(String[] args) {
    }
}
