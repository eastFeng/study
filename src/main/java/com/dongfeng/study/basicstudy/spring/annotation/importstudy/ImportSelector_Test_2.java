package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import cn.hutool.core.util.ArrayUtil;
import com.dongfeng.study.util.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

import org.springframework.context.annotation.Import;

import java.util.Map;

/**
 * {@link ImportSelector}接口：可以实现动态批量加载bean。
 * <p> ImportSelector接口是Spring中导入外部配置的核心接口，
 * 在SpringBoot的自动化配置和@EnableXXX(功能性注解)都有它的存在。
 *
 * <p> 用法二：
 * 实现动态批量加载bean，
 * 在selectImports方法中返回动态的 类的全限定名的String数组。
 *
 * <p> 具体用法：一般的，框架中如果基于AnnotationMetadata的参数实现动态加载类，
 * 一般会写一个额外的EnableXXX注解，配合使用。
 *
 * @author eastFeng
 * @date 2022-12-04 23:45
 */
@Slf4j
public class ImportSelector_Test_2 implements ImportSelector {

    // EnableImportTest_2注解的全限定名称
    private static final String ANNOTATION_CLASS_NAME = EnableImportTest_2.class.getName();
    // 空的String数组
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 该返回要加载（到SpringIoC容器）的 配置类（{@link Configuration}注解标注的类）或者具体Bean 类的全限定名的String数组。
     *
     * <p> 可以是配置类（{@link Configuration}注解标注的类），也可以是具体的Bean类的全限定名称。
     * <p>
     * <p> --------------------------- 分割线 ---------------------------
     * <p> 1. 元数据
     * <p> 数据的数据。比如Class就是一种元数据。
     * <p> {@link ClassMetadata}接口：对Class的抽象和适配。
     *
     * <p> 2. 元注解
     * <p> 注解上的注解 Spring 将其定义为元注解(meta-annotation)，
     *     如 @Component标注在 @Service上，@Component 就被称作为元注解。
     *
     * <p> 3. {@link AnnotationMetadata}：对特定类标注的所有注解（Annotation）的抽象访问。
     *
     * @param importingClassMetadata 能够获取到当前标注{@link Import}注解的类的所有注解信息。
     *                               <p> 因为AnnotationMetadata是Import注解所在的类属性，
     *                               如果Import注解标注在注解上，则延伸至应用这个注解的类为止。
     *                               <p> 比如类A标注了其他注解X，注解X标注了@Import注解，
     *                               A也是标注了@Import注解。
     * @return 类的全限定名的String数组
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        log.info("selectImports importingClassMetadata:{}", importingClassMetadata.getClassName());
        // AnnotationMetadata : 够获取到当前标注@Import注解的类的所有注解信息。
        // 因为AnnotationMetadata是Import注解所在的类属性，
        // 如果Import注解标注在注解上，则延伸至应用这个注解的类为止。

        boolean hasAnnotation = importingClassMetadata.hasAnnotation(ANNOTATION_CLASS_NAME);
        if (!hasAnnotation){
            return EMPTY_STRING_ARRAY;
        }

        // 获取（标注@Import注解的）类的某个指定注解的所有属性
        // 获取EnableImportTest_2注解的所有属性
        Map<String, Object> map =
                importingClassMetadata.getAnnotationAttributes(ANNOTATION_CLASS_NAME, true);
        if (map==null || map.isEmpty()){
            return EMPTY_STRING_ARRAY;
        }

        // 获取EnableImportTest_2注解中beanClass属性的值 并转换为String数组类型
        String[] beanClasses =
                AnnotationUtil.getSpecifyTypeAttribute(map,
                "beanClass",
                String[].class);
        // 获取EnableImportTest_2注解中beanClassName属性的值
        String[] beanClassName = (String[])map.get("beanClassName");

        // 如果给定数组为null或者是个空数据，返回默认数组
        beanClasses = ArrayUtil.defaultIfEmpty(beanClasses, EMPTY_STRING_ARRAY);
        beanClassName = ArrayUtil.defaultIfEmpty(beanClassName, EMPTY_STRING_ARRAY);

        // 这时两个数组都不会为null了，将两个数组组合在一起
        return ArrayUtil.addAll(beanClassName, beanClasses);

//        if (beanClasses==null && beanClassName==null){
//            // 两个都为null
//            return new String[0];
//        }else if (beanClasses == null){
//            // beanClasses为null，beanClassName不为null
//            return beanClassName;
//        }else if (beanClassName == null){
//            // beanClassName为null，beanClasses不为null
//            return beanClasses;
//        }else {
//            // 都不为null，将两个数组 组合在一起
//            return ArrayUtil.addAll(beanClassName, beanClasses);
//        }
    }


}
