package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link ImportSelector}接口：可以实现动态批量加载bean。
 * <p> ImportSelector接口是Spring中导入外部配置的核心接口，
 * 在SpringBoot的自动化配置和@EnableXXX(功能性注解)都有它的存在。
 *
 * <p> 用法一：在selectImports方法中返回固定（写死的）类的全限定名的String数组。
 * （返回固定的要加载的bean）
 *
 * @author eastFeng
 * @date 2022-12-02 17:58
 */
public class ImportSelector_Test_1 implements ImportSelector {
    /**
     * 该返回要加载（到SpringIoC容器）的 配置类（{@link Configuration}注解标注的类）或者具体Bean 类的全限定名的String数组。
     *
     * <p> 可以是配置类（{@link Configuration}注解标注的类），也可以是具体的Bean类的全限定名称。
     *
     * @param importingClassMetadata 注解元数据
     * @return 类的全限定名的String数组
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String s1 = "com.dongfeng.study.basicstudy.spring.annotation.importstudy.Person_02";
        return new String[]{s1};
    }
}
