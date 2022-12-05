package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Import;

import org.springframework.beans.factory.FactoryBean;

import com.dongfeng.study.basicstudy.spring.annotation.importstudy.ImportStudy;
import com.dongfeng.study.basicstudy.spring.annotation.ConfigurationStudy;


/**
 * Spring中配置bean的三种方式
 *
 * @author eastFeng
 * @date 2022-12-03 0:38
 */
public class BeanConfigure_02 {
    /**
     * 配置/加载bean的五种方式
     *
     * <p> 一. xml配置
     * <p> 使用xml文件方式。
     * <p> 缺点：繁琐，不建议使用。
     *
     * <p> 二. xml+注解配置
     * <p> 在xml文件中定义一个扫描包的位置，
     *     然后通过{@link Component}、{@link Controller}、{@link Configuration}等注解标注在类上
     *     告诉Spring哪些类是需要交给Spring创建对象并管理。
     * <p> 缺点：对于第三方的类（依赖的jar里面的类）不适用，
     *     不可能在第三方类上的源代码上标注{@link Component}、{@link Controller}等注解。
     *
     * <p> 三. javaConfig配置
     * <p> 使用Java代码，主要是通过{@link Configuration}注解+{@link Bean}注解。（纯Java代码 + 注解）
     * <p> 不存在前两种方式存在的缺点，推荐使用。
     * <p> {@link Configuration}注解学习：{@link ConfigurationStudy}
     *
     * <p> 1. javaConfig配置/加载bean —— 扩展1 —— @Bean定义{@link FactoryBean}接口
     * <p> 初始化实现{@link FactoryBean}接口的类，实现对bean加载到容器之前的批处理操作。
     * <p> 【注】该方式主要用来创建复杂对象，简单的对象没必要使用这种方式。
     * <p> 该扩展的学习：{@link BookFactoryBean}和{@link BeanConfiguration}
     *
     * <p> 2. javaConfig配置/加载bean —— 扩展2 —— {@link ImportResource}注解
     * <p> 加载配置类并加载配置文件（系统迁移）。
     * <p> 系统迁移：比如，在原有系统上做二次开发，原有系统是用xml文件形式声明的bean，
     *     现在二次开发准备用注解的形式声明bean。
     *     如何在使用注解的形式中把之前用xml声明的bean加载到Spring IoC容器？
     *     使用{@link ImportResource}注解。
     *
     * <p> 3. javaConfig配置/加载bean —— 扩展3 —— {@link Configuration}注解的proxyBeanMethods属性
     * <p> {@link Configuration}注解中设置proxyBeanMethods=true
     *    可以保障调用此方法得到的对象是从容器中获取的而不是重新创建的。
     *
     * <p> 四. {@link Import}注解把实例对象加入到Spring IoC容器当中
     * <p> 用{@link Import}注解的方式比使用{@link Configuration}注解+{@link Bean}注解更简单快捷。
     * <p> 使用{@link Import}注解导入要注入的bean对应的字节码。
     * <p> 通过快速导入的方式把实例对象加入到Spring IoC容器当中，
     *    对于第三方的class来说尤其方便。
     * <p> {@link Import}注解学习：{@link ImportStudy}
     *
     * <p> 五. 使用上下文对象在容器初始化完毕后注入bean
     * <p> 这种方式在平时开发时不常用，做框架时有可能会用到。
     */
    public static void main(String[] args) {
        // 初始化容器
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(BeanConfiguration.class);

        // 五. 使用上下文对象在容器初始化完毕后注入bean
        context.register(Book_A.class);

        String[] names = context.getBeanDefinitionNames();
        for (String name : names){
            System.out.println(name);
        }

        System.out.println("==============================================");
        // javaConfig配置/加载bean —— 扩展1 —— @Bean定义FactoryBean接口
        // 初始化实现FactoryBean接口的类，实现对bean加载到容器之前的批处理操作。
        Object book = context.getBean("book");
        System.out.println(book);


    }
}
