package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;

/**
 * {@link Bean}注解学习
 *
 * @author eastFeng
 * @date 2022-11-22 19:27
 */
public class BeanStudy {
    /**
     * <p> 一. {@link Bean}注解基础概念
     *
     * <p>1. Spring的Bean注解用于告诉方法，产生一个Bean对象，然后这个Bean对象交给Spring管理。
     * 产生这个Bean对象的方法Spring只会调用一次，随后这个Spring将会将这个Bean对象放在自己的IOC容器中。
     * <p>2. Bean是一个方法级别上的注解，主要用在{@link Configuration}注解的类里，也可以用在{@link Component}注解的类里。
     *      添加的bean的id为方法名。
     * <p>3. 使用Bean时，即是把已经在xml文件中配置好的Bean拿来用，完成属性、方法的组装。
     * 比如Autowired注解、Resource注解，可以通过byTYPE（@Autowired）、byNAME（@Resource）的方式获取Bean。
     * <p>4. 注册Bean时，{@link Component}, {@link Controller}, {@link Service}, {@link Repository}, {@link Configuration}
     *       这些注解都是把要实例化的对象转化成一个Bean，放在IoC容器中，
     *       等要用的时候，它会和上面的Autowired,Resource配合到一起，把对象、属性、方法完美组装。
     *
     * <p>二. Bean注解的属性
     * <p> {@link Bean#value()}：bean别名和name是相互依赖关联的，value,name如果都使用的话值必须要一致。
     * <p> {@link Bean#name()}：bean名称，如果不写会默认为注解的方法名称。
     * <p> {@link Bean#autowire()}：自定装配默认是不开启的，建议尽量不要开启，因为自动装配不能装配基本数据类型、字符串、数组等，
     *     这是自动装配设计的局限性，并且自动装配不如依赖注入精确。
     * <p> {@link Bean#initMethod()}：bean的初始化之前的执行方法，该参数一般不怎么用，因为完全可以在代码中实现。
     * <p> {@link Bean#destroyMethod()}：默认使用javaConfig配置的bean，如果存在close或者shutdown方法，
     *         则在bean销毁时会自动执行该方法，如果你不想执行该方法，则添加@Bean(destroyMethod="")来防止出发销毁方法。
     */
    public static void main(String[] args) {
    }
}
