package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.context.annotation.Configuration}注解学习
 *
 * @author eastFeng
 * @date 2022-11-22 20:03
 */
@Configuration
public class ConfigurationAnnotation {
    /**
     * <p>
     * 在Spring框架中，会有大量的xml配置文件，或者需要做很多繁琐的配置。
     * 从Spring3开始，Spring就支持了两种bean的配置方式， 一种是基于xml文件方式、另一种就是JavaConfig 。
     * SpringBoot框架是为了能够帮助使用Spring框架的开发者快速高效的构建一个基于Spring框架以及Spring生态体系的应用解决方案。
     * 它是对约定优于配置这个理念下 的一个最佳实践。因此它是一个服务于框架的框架，服务的范围是简化配置文件。
     * Spring4以后，官方推荐我们使用JavaConfig声明将Bean交给容器管理。
     * 任何一个标注了{@link org.springframework.context.annotation.Configuration}的Java类定义都是一个JavaConfig配置类。
     * </p>
     *
     * <p>
     * {@link org.springframework.context.annotation.Configuration}注解，
     * 标记在类上(代表告诉springboot这个类是一个配置类)。相当于我们原来写的spring的配置文件。
     * </p>
     * <p>
     * {@link org.springframework.context.annotation.Configuration}标注在类上，配置Spring容器(应用上下文)。
     * 相当于把该类作为Spring的xml配置文件中的<beans>。
     * {@link org.springframework.context.annotation.Configuration}注解标注的类中，
     * 使用{@link org.springframework.context.annotation.Bean}注解标注的方法，方法返回的类型都会直接注册为bean。
     * 说白了，{@link org.springframework.context.annotation.Configuration}注解可以生产多个对象，自身类也会被实例化。
     * 而{@link org.springframework.stereotype.Controller}，{@link org.springframework.stereotype.Service}等这些注解只是生成当前类一个对象。
     * </p>
     * <p>
     * {@link org.springframework.context.annotation.Configuration}注解
     * 是一个被{@link org.springframework.stereotype.Component}注解修饰的注解,
     * 说明被{@link org.springframework.context.annotation.Configuration}标注的类本身也是一个组件/Bean,也会被实例化。
     * </p>
     *
     * <p>
     * {@link org.springframework.context.annotation.Configuration}注解的配置类有如下要求：
     * <ol>
     *     <li>不可以是final类型</li>
     *     <li>不可以是匿名类</li>
     *     <li>如果是嵌套的类，必须是静态类</li>
     * </ol>
     * </p>
     */
    public static void main(String[] args) {
    }

    /**
     * <p>
     * {@link org.springframework.context.annotation.Configuration}注解和
     * {@link org.springframework.context.annotation.Bean}注解配合使用
     * </p>
     *
     * <p>
     * {@link org.springframework.context.annotation.Configuration}用于定义配置类，可替换xml配置文件，
     * 被注解的类内部包含有一个或多个被注解的方法，
     * 这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，
     * 并用于构建bean定义（一般使用{@link org.springframework.context.annotation.Bean}注解标注在方法上来定义bean），初始化Spring容器。
     *
     * 说白了，{@link org.springframework.context.annotation.Configuration}注解可以生产多个对象，自身类也会被实例化。
     * 而{@link org.springframework.stereotype.Controller}，{@link org.springframework.stereotype.Service}等这些注解只是生成当前类一个对象。
     * </p>
     *
     * <p>
     * {@link org.springframework.context.annotation.Configuration}与{@link org.springframework.context.annotation.Bean}结合使用。
     * {@link org.springframework.context.annotation.Configuration}可理解为用spring的时候xml里面的<beans> beans标签，
     * {@link org.springframework.context.annotation.Bean}可理解为用spring的时候xml里面的<bean> bean标签。
     * SpringBoot不是Spring的加强版，所以@Configuration和@Bean同样可以用在普通的spring项目中，
     * 而不是SpringBoot特有的，只是在Spring用的时候，注意加上扫包配置，例如<context:component-scan base-package="com.xxx.xxx" />。
     * SpringBoot则不需要，主要你保证你的启动SpringBoot的启动类main入口，在这些类的上层包就行，
     * 本项目的启动类为{@link com.dongfeng.study.StudyApplication}。
     * </p>
     *
     * <p>
     *
     * </p>
     *
     */
    @Bean
    public ConfigurationAnnotation.ConfigurationAndBeanTest test1(){
        return new ConfigurationAndBeanTest("TEST");
    }

    // 静态内部类
    public static class ConfigurationAndBeanTest{
        private String msg;
        public ConfigurationAndBeanTest(String msg){
            this.msg = msg;
        }
    }
}
