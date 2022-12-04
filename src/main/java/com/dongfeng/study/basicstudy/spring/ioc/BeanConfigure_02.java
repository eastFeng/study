package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.ImportResource;

import org.springframework.beans.factory.FactoryBean;


/**
 * Spring中配置bean的三种方式
 *
 * @author eastFeng
 * @date 2022-12-03 0:38
 */
public class BeanConfigure_02 {
    /**
     * 一. 配置/加载bean的三种方式
     *
     * <p> 1. xml配置
     * <p> 使用xml文件方式。
     * <p> 缺点：繁琐，不建议使用。
     *
     * <p> 2. xml+注解 配置
     * <p> 在xml文件中定义一个扫描包的位置，
     *     然后通过{@link Component}、{@link Controller}、{@link Configuration}等注解标注在类上
     *     告诉Spring哪些类是需要交给Spring创建对象并管理。
     * <p> 缺点：对于第三方的类（依赖的jar里面的类）不适用，
     *     不可能在第三方类上的源代码上标注{@link Component}、{@link Controller}等注解。
     *
     * <p> 3. javaConfig配置
     * <p> 使用Java代码，主要是通过{@link Configuration}注解。（纯Java代码 + 注解）
     * <p> 推荐使用。
     *
     *
     * <p> 二. 配置/加载bean的其他扩展方式
     * <p> 4. bean的加载/配置方式 —— 扩展1
     * <p> 初始化实现{@link FactoryBean}接口的类，实现对bean加载到容器之前的批处理操作。
     *
     * <p> 5. bean的加载方式——扩展2
     * <p> 加载配置类并加载配置文件（系统迁移）。 {@link ImportResource}注解
     *
     *
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(BeanConfiguration.class);

        String[] names = context.getBeanDefinitionNames();
        for (String name : names){
            System.out.println(name);
        }

        System.out.println("==============================================");
        // bean的加载/配置方式 —— 扩展1
        // 初始化实现{@link FactoryBean}接口的类，实现对bean加载到容器之前的批处理操作。
        Object book = context.getBean("book");
        System.out.println(book);



    }
}
