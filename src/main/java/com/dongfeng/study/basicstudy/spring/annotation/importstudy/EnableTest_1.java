package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author eastFeng
 * @date 2022-12-05 1:48
 */
@Import(Person_05.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableTest_1 {
}
