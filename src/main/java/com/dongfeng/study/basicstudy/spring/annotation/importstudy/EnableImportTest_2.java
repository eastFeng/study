package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * @author eastFeng
 * @date 2022-12-05 0:45
 */

@Import(ImportSelector_Test_2.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableImportTest_2 {

    /**
     * 要加载的 配置类（{@link Configuration}注解标注的类）或者具体Bean 类Class数组
     * @return Class数组
     */
    Class<?>[] beanClass() default {};

    /**
     * 要加载的 配置类（{@link Configuration}注解标注的类）或者具体Bean 类的全限定名 的String数组
     * @return 类的全限定名的String数组
     */
    String[] beanClassName() default {};
}
