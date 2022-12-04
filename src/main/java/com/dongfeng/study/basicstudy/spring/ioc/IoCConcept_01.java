package com.dongfeng.study.basicstudy.spring.ioc;

/**
 * Spring的IoC容器
 *
 * @author eastFeng
 * @date 2022-12-03 0:38
 */
public class IoCConcept_01 {

    /**
     * 1. IoC（Inversion of Control）容器优点
     * <p> 降低组件之间的耦合度。
     *
     * <p> 2. IoC（Inversion of Control，控制反转）
     * <p> 传统方式下，创建对象的控制权由程序员通过代码去创建。
     * Spring中，对象的创建以及管理权限由原来代码的形式转移给Spring Ioc容器进行管理。
     * <b>【Spring Ioc容器会根据配置（xml配置、javaConfig配置等）
     * 去创建实例以及管理各个实例之间的依赖管理】</b>，
     * 这样，对象与对象之间是松耦合的管理，更有利于对象的复用。
     *
     * <p> 3. Bean
     * <p> 在Spring中，构成应用程序主干并由<b>【Spring IoC容器管理的对象称为bean】</b>。
     * bean是由Spring IoC容器实例化、组装和管理的对象。
     * <p>Spring Ioc容器管理着一个或者多个bean。
     *
     */
    public static void main(String[] args) {
    }
}
