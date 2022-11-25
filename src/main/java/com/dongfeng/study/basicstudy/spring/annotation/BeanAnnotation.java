package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.Bean;

/**
 * {@link org.springframework.context.annotation.Bean}注解学习
 *
 * @author eastFeng
 * @date 2022-11-22 19:27
 */
public class BeanAnnotation {
    /**
     * <p>
     *     {@link org.springframework.context.annotation.Bean}注解基础概念
     *     <ol>
     *         <li>Spring的{@link org.springframework.context.annotation.Bean}注解用于告诉方法，
     *             产生一个Bean对象，然后这个Bean对象交给Spring管理。
     *             产生这个Bean对象的方法Spring只会调用一次，随后这个Spring将会将这个Bean对象放在自己的IOC容器中；</li>
     *         <li>SpringIOC 容器管理一个或者多个bean，这些bean都需要在{@link org.springframework.context.annotation.Configuration}注解下进行创建，
     *         在一个方法上使用@Bean注解就表明这个方法需要交给Spring进行管理；</li>
     *         <li>{@link org.springframework.context.annotation.Bean}是一个方法级别上的注解，
     *         主要用在{@link org.springframework.context.annotation.Configuration}注解的类里，
     *         也可以用在{@link org.springframework.stereotype.Component}注解的类里。添加的bean的id为方法名；</li>
     *         <li>使用Bean时，即是把已经在xml文件中配置好的Bean拿来用，完成属性、方法的组装；
     *         比如{@link org.springframework.beans.factory.annotation.Autowired},
     *         {@link javax.annotation.Resource}，可以通过byTYPE（@Autowired）、byNAME（@Resource）的方式获取Bean；</li>
     *         <li>注册Bean时，{@link org.springframework.stereotype.Component},
     *         {@link org.springframework.stereotype.Repository},
     *         {@link org.springframework.stereotype.Controller},
     *         {@link org.springframework.stereotype.Service},
     *         {@link org.springframework.context.annotation.Configuration}
     *         这些注解都是把你要实例化的对象转化成一个Bean，放在IoC容器中，
     *         等你要用的时候，它会和上面的{@link org.springframework.beans.factory.annotation.Autowired},
     *         {@link javax.annotation.Resource}配合到一起，把对象、属性、方法完美组装；</li>
     *     </ol>
     * </p>
     *
     * <p>
     *     {@link org.springframework.context.annotation.Bean}注解的属性
     *     <ol>
     *         <li>{@link Bean#value()}：bean别名和name是相互依赖关联的，value,name如果都使用的话值必须要一致；</li>
     *         <li>{@link Bean#name()}：bean名称，如果不写会默认为注解的方法名称；</li>
     *         <li>{@link Bean#autowire()}：自定装配默认是不开启的，建议尽量不要开启，因为自动装配不能装配基本数据类型、字符串、数组等，
     *         这是自动装配设计的局限性，并且自动装配不如依赖注入精确；</li>
     *         <li>{@link Bean#initMethod()}：bean的初始化之前的执行方法，该参数一般不怎么用，因为完全可以在代码中实现；</li>
     *         <li>{@link Bean#destroyMethod()}：默认使用javaConfig配置的bean，如果存在close或者shutdown方法，
     *         则在bean销毁时会自动执行该方法，如果你不想执行该方法，则添加@Bean(destroyMethod="")来防止出发销毁方法；</li>
     *     </ol>
     * </p>
     */
    public static void main(String[] args) {
    }
}
