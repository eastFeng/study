package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;

import java.lang.annotation.ElementType;


/**
 * {@link Component}注解学习
 *
 * <p>
 * 学习完{@link Component}注解之后就可以学习一下注解：
 * <ol>
 *     <li>
 *         {@link Controller}注解，在{@link ConfigurationStudy}中。
 *     </li>
 * </ol>
 * </p>
 * @author eastFeng
 * @date 2022-11-22 21:34
 */
public class ComponentStudy {

    /**
     * <p>
     * {@link Component}注解：
     * 标注Spring管理的Bean，使用{@link Component}注解标注在一个类上，
     * 表示将此类标记为Spring容器中的一个Bean，作用就是实现bean的注入。
     * 相当于相当于xml配置中的：<bean id=" " class=" "></bean>
     * </p>
     *
     * <p>
     * {@link Component}注解可用于标注类、接口、枚举类型等 ({@link Component}注解Target类型是{@link ElementType#TYPE})。
     * </p>
     *
     * <p>
     * 当前类不属于各种归属类@controller @service等的时候，就可以使用@component来标注这个类。
     * </p>
     *
     * <p>
     *  {@link Component}注解的三个派生注解：
     *  <ol>
     *      <li>{@link Controller}</li>
     *      <li>{@link Service}</li>
     *      <li>{@link Repository}</li>
     *  </ol>
     *  这三个派生注解的目的均是将类标记为需要构造并注入的Bean，
     *  这三者是用来标记一些特殊功能和用途的，并能够为编码进行明确的分层结构，
     *  为第三方框架留了很多的想象空间。
     *  可以简单理解为：
     *  {@link Controller}、{@link Service}、{@link Repository} = {@link Component} +一些其他特殊功能
     * </p>
     */
    public static void main(String[] args) {
    }
}
