package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author eastFeng
 * @date 2022-12-02 17:58
 */
public class MyImportSelector implements ImportSelector {
    /**
     * 根据导入@Configuration类的AnnotationMetadata（注解元数据）
     * 选择并返回 要导入的（1个或者多个）类的名称。
     *
     * @param importingClassMetadata 注解元数据
     * @return 全类名的字符串数组
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String s1 = "com.dongfeng.study.basicstudy.spring.annotation.importstudy.Person_02";
        return new String[]{s1};
    }
}
