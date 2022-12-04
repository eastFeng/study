package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

/**
 * Spring : Bean的加载解析
 *
 * @author eastFeng
 * @date 2022-12-03 15:28
 */
public class BeanLoad_04 {

    /**
     * <b>Spring : Bean的加载解析</b>
     *
     * <p> Bean的加载解析过程：
     * <p> xml配置、javaConfig配置等定义bean ——> BeanDefinition（bean的元数据）——>
     * BeanDefinitionReader（读取解析BeanDefinition）——> BeanDefinitionRegistry（注册BeanDefinition）
     * ——> BeanFactory或ApplicationContext
     *
     * <p> 1. {@link BeanDefinition}接口：对bean的一些元数据定义（描述了Bean实例）。
     * <p> 包括类名，父类名，bean的作用域，是否是抽象的等信息。
     *
     * <p> 2. {@link BeanDefinitionReader}接口：Bean定义读取器，读取Bean配置将之解析为BeanDefinition。
     * 结合BeanDefinitionRegistry将对应的BeanDefinition放入Spring IoC容器中。
     *
     *
     * <p>【注意】源码中介绍说，一个bean definition reader并非一定要实现这个接口，
     * 简单来说，BeanDefinitionReader这个接口提供了一个规范，但是并不强制执行，它仅作为reader定义的建议。
     * 因此，我们可以在Spring的源码中找到并未实现此接口的bean definition reader。
     * <p>一些bean definition reader具体是实现类（有的实现了{@link BeanDefinitionReader}接口，有的则没有）：
     * <ul>
     *     <li>{@link XmlBeanDefinitionReader}：可以从XML文件读取Bean定义信息。</li>
     *     <li>{@link PropertiesBeanDefinitionReader}：可以从properties文件读取Bean定义信息。</li>
     *     <li>{@link GroovyBeanDefinitionReader}：可以读取Groovy 语言写的Bean的定义信息。</li>
     *     <li>{@link AnnotatedBeanDefinitionReader}：先获取所有使用了@Configuration注解的类，
     *     然后通过AnnotatedBeanDefinitionReader生成与这些类对应的BeanDefinition。</li>
     * </ul>
     *
     * <p> 3. {@link BeanDefinitionRegistry}接口：注册BeanDefinition。
     *
     */
    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    }
}
