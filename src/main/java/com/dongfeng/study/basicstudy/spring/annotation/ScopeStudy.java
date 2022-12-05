package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * {@link Scope}注解学习
 *
 * @author eastFeng
 * @date 2022-12-05 0:32
 */
public class ScopeStudy {
    /**
     * {@link Scope}注解作用：控制简单对象创建次数。
     * <p> 如果不添加{@link Scope}注解，Spring提供默认值：
     * {@link ConfigurableBeanFactory#SCOPE_SINGLETON} ，单例模式，只创建一次。
     */
    public static void main(String[] args) {
    }
}
