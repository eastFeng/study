package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.dongfeng.study.StudyApplication;

/**
 * {@link org.springframework.context.annotation.Configuration}注解学习
 *
 * @author eastFeng
 * @date 2022-11-22 20:03
 */
@Configuration
public class ConfigurationStudy {
    /**
     *
     * <p>1. 在Spring框架中，会有大量的xml配置文件，或者需要做很多繁琐的配置。
     * 从Spring3开始，Spring就支持了两种bean的配置方式， 一种是基于xml文件方式、另一种就是JavaConfig 。
     * SpringBoot框架是为了能够帮助使用Spring框架的开发者快速高效的构建一个基于Spring框架以及Spring生态体系的应用解决方案。
     * 它是对约定优于配置这个理念下的一个最佳实践。因此它是一个服务于框架的框架，服务的范围是简化配置文件。
     * Spring4以后，官方推荐我们使用JavaConfig声明将Bean交给容器管理。
     * 任何一个标注了{@link Configuration}的Java类定义都是一个JavaConfig配置类。
     *
     * <p>2. {@link Configuration}用于定义配置类。
     * <p> {@link Configuration}注解标记在类上（代表告诉springboot这个类是一个配置类）。
     * 相当于我们原来写的spring的配置文件。
     *
     * <p>3. {@link Configuration}标注在类上，配置Spring容器(应用上下文)。
     *    相当于把该类作为Spring的xml配置文件中的<beans>。
     * <p> 在被{@link Configuration}注解标注的类中，使用{@link Bean}注解标注的方法，方法返回的类型都会直接注册为bean。
     * 说白了，被{@link Configuration}注解的类可以生产多个对象，自身类也会被实例化。
     * 而{@link Controller}，{@link Service}等这些注解只是生成当前类一个对象。
     *
     * <p>4. {@link Configuration}注解被{@link Component}注解所修饰（标注），
     * 说明被{@link Configuration}标注的类本身也是一个组件/Bean,也会被实例化。
     *
     * <p>5. 被{@link Configuration}注解标注的配置类有如下要求：
     * <ul>
     *     <li>不可以是final类型</li>
     *     <li>不可以是匿名类</li>
     *     <li>如果是嵌套的类，必须是静态类</li>
     * </ul>
     */
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(ConfigurationStudy.class);
        // 从容器中获取bean
        ConfigurationAndBeanTest test1 =
                context.getBean("test1", ConfigurationAndBeanTest.class);
        System.out.println("test1 = "+test1);
        System.out.println(test1.msg);

    }

    /**
     * <p> {@link Configuration}注解和{@link Bean}注解配合使用
     *
     * <p>1. {@link Configuration}用于定义配置类，可替换xml配置文件，
     * 被注解的类内部包含有一个或多个被{@link Bean}注解的方法来定义bean，
     * 这些方法将会被{@link AnnotationConfigApplicationContext}或{@link AnnotationConfigWebApplicationContext}类进行扫描，
     * 并用于构建bean定义，初始化Spring容器。
     *
     * <p> 说白了，{@link Configuration}注解可以生产多个对象，自身类也会被实例化。
     * 而{@link Controller}，{@link Service}等这些注解只是生成当前类一个对象。
     *
     * <p>2. {@link Configuration}与{@link Bean}结合使用。
     * {@link Configuration}可理解为用spring的时候xml里面的<beans> beans标签，
     * {@link Bean}可理解为用spring的时候xml里面的<bean> bean标签。
     *
     * <p> 注意：使用@Bean可以定义第三方bean。
     *
     * <p>3. SpringBoot不是Spring的加强版，所以{@link Configuration}与{@link Bean}同样可以用在普通的spring项目中，
     * 而不是SpringBoot特有的，只是在Spring用的时候，注意加上扫包配置，例如<context:component-scan base-package="com.xxx.xxx" />。
     * SpringBoot则不需要，主要你保证你的启动SpringBoot的启动类main入口，在这些类的上层包就行。
     * 例如本项目的启动类为{@link StudyApplication}。
     *
     */
    @Bean
    public ConfigurationAndBeanTest test1(){
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
