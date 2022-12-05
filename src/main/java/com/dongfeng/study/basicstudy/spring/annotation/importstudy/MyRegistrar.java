package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author eastFeng
 * @date 2022-12-02 18:54
 */
public class MyRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * @param importingClassMetadata  annotation metadata of the importing class
     *                                导入类的注解元数据
     * @param registry                current bean definition registry
     *                                当前bean定义注册表
     * @param importBeanNameGenerator 不知道
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        // 把Person_03交给容器管理

        // Person_03的BeanDefinition（Bean元数据）
        RootBeanDefinition definition = new RootBeanDefinition(Person_03.class);
        // 注册Person_03的BeanDefinition（Bean元数据）
        registry.registerBeanDefinition("person03", definition);
    }
}
