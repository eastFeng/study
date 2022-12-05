package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import cn.hutool.core.util.ArrayUtil;
import com.dongfeng.study.util.AnnotationUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.Map;
import java.util.Set;

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
public class ImportSelector_Test_2 implements ImportSelector {

    // EnableTest_2注解的名称
    private static final String ANNOTATION_CLASS_NAME = EnableTest_2.class.getName();

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
     * <p> 3. {@link AnnotationMetadata}：元注解信息
     *
     * @param importingClassMetadata 能够获取到当前标注@Import注解的类的所有注解信息。
     *                               <p> 因为AnnotationMetadata是Import注解所在的类属性，
     *                               如果Import注解标注在注解上，则延伸至应用这个注解的类为止。
     *                               <p> 比如类A标注了其他注解X，注解X标注了@Import注解，
     *                               A也是标注了@Import注解。
     * @return 类的全限定名的String数组
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // AnnotationMetadata : 够获取到当前标注@Import注解的类的所有注解信息。
        // 因为AnnotationMetadata是Import注解所在的类属性，
        // 如果Import注解标注在注解上，则延伸至应用这个注解的类为止。


        System.out.println("ImportSelector_Test_2 ANNOTATION_CLASS_NAME="+ANNOTATION_CLASS_NAME);
        //
        Set<MethodMetadata> annotatedMethods =
                importingClassMetadata.getAnnotatedMethods(ANNOTATION_CLASS_NAME);
        annotatedMethods.forEach(System.out::println);

        // 获取（标注@Import注解的）类的某个指定注解的所有属性
        // 获取EnableTest_2注解的所有属性
        Map<String, Object> map =
                importingClassMetadata.getAnnotationAttributes(ANNOTATION_CLASS_NAME, true);
        assert map != null;

        // 获取EnableTest_2注解中beanClass属性的值 并指定为String数组类型
        String[] beanClasses =
                AnnotationUtil.getSpecifyTypeAttribute(map,
                "beanClass",
                String[].class);
        // 获取EnableTest_2注解中beanClassName属性的值
        String[] beanClassName = (String[])map.get("beanClassName");

        if (beanClasses==null && beanClassName==null){
            // 两个都为null
            return new String[0];
        }else if (beanClasses == null){
            // beanClasses为null，beanClassName不为null
            return beanClassName;
        }else if (beanClassName == null){
            // beanClassName为null，beanClasses不为null
            return beanClasses;
        }else {
            // 将两个数组组合在一起
            return ArrayUtil.addAll(beanClassName, beanClasses);
        }
    }


}
