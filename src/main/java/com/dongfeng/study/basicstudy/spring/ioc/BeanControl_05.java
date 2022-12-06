package com.dongfeng.study.basicstudy.spring.ioc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.*;

import com.dongfeng.study.config.properties.ConfigurationPropertiesDemo;

/**
 * bean的加载控制：指根据特定情况对bean进行选择性加载以达到适用于项目的目标
 *
 * @author eastFeng
 * @date 2022-12-06 0:53
 */
public class BeanControl_05 {

    /**
     * <b>bean的加载控制</b>
     * <p> 一. 使用{@link Conditional}注解的派生注解设置各种组合条件控制bean的加载。
     * <p> 使用@ConditionalOn***注解为bean的加载设置条件。
     *
     * <p> 1.{@link ConditionalOnClass}注解
     * <p>  匹配指定类
     * <p> @ConditionalOnClass(A.class) ：有A这个类就加载bean
     *
     * <p> 2. {@link ConditionalOnMissingClass}注解
     * <p> 未匹配指定类
     *
     * <p> 3. {@link ConditionalOnBean}注解
     * <p>  匹配指定类型的bean：@ConditionalOnBean(Mouse.class)
     * <p> 匹配指定名称的bean：@ConditionalOnBean(name="com.itheima.bean.Mouse")
     *         ， @ConditionalOnBean(name="jerry")
     *
     * <p> 4. 匹配指定环境
     * <p> {@link ConditionalOnNotWebApplication}注解
     *
     * <p> 二. bean依赖的属性配置
     * <p> 使用{@link ConfigurationProperties}注解
     *     读取Springboot的application配置文件里面的配置
     * <p> 学习使用：{@link ConfigurationPropertiesDemo}
     */
    public static void main(String[] args) {

    }
}
