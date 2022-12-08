package com.dongfeng.study.basicstudy.spring.springboot;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.EnvironmentAware;

import java.lang.annotation.Inherited;

/**
 * SpringBoot自动配置学习
 *
 * @author eastFeng
 * @date 2022-12-07 13:18
 */
public class AutoConfiguration_01 {
    /**
     * <b> 一. 自动配置原理</b>
     * <p> 1. 收集Spring开发者的编程习惯，整理开发过程使用的【常用技术列表。——>（技术集A）】
     * <p> 2. 收集常用技术（技术集A）的使用参数，整理开发过程中【每个技术的常用设置列表。——>（设置集B）】
     * <p> 3. 初始化SpringBoot基础环境，加载用户自定义的bean和导入的其他坐标（pom中添加的依赖），形成初始化环境。
     * <p> 4. 将技术集A包含的所有技术都定义出来，在Spring/SpringBoot启动时默认全部加载。
     * <p> 5. 将技术集A中具有使用条件的技术约定出来，设置成按条件加载，
     *        由开发者决定是否使用该技术（与初始化环境比对）。
     * <p> 6. 将设置集B作为默认配置加载（约定大于配置），减少开发者配置工作量。
     * <p> 7. 开放设置集B的配置覆盖接口，由开发者根据自身需要决定是否覆盖默认配置。
     */
    public static void study_1(){}

    /**
     * <p><b> 二. SpringBoot自动配置的开始：{@link SpringBootApplication}注解 </b>
     * <p> {@link SpringBootApplication}注解是若干个注解的组合注解。
     * <p></p>
     *
     * <p><b> 三. {@link SpringBootApplication}注解的层级和每层包含的主要注解 </b></p>
     * <p> {@link SpringBootApplication}注解：自动配置的实现，表示当前类是SpringBoot的启动类。
     *     <ol>
     *         <li>{@link SpringBootConfiguration}注解：声明（标注）当前类是根配置类。
     *             <ul>
     *                 <li>{@link Configuration}注解：声明当前类是配置类.
     *                     <ul>
     *                         <li>{@link Component}注解：表示将当前类标记为Spring容器中的一个Bean。
     *                             <ul><li>{@link Indexed}注解：为Spring的模式注解1添加索引，以提升应用启动性能。
     *                                     在应用中有大量使用@ComponentScan扫描的package包含的类越多的时候，
     *                                     Spring模式注解解析耗时就越长。
     *                             </li></ul>
     *                         </li>
     *                     </ul>
     *                 </li>
     *             </ul>
     *         </li>
     *         <li>{@link EnableAutoConfiguration}注解：开启自动配置的核心。
     *             <ul>
     *                 <li>{@link Inherited}注解</li>
     *                 <li>{@link AutoConfigurationPackage}注解：添加该注解标注的类所在的package作为自动配置package进行管理。
     *                     设置SpringBoot启动类所在的包作为扫描包，后续要针对这个包进行扫描。
     *                     <ul>
     *                         <li>@Import(AutoConfigurationPackages.Registrar.class)：【重点】</li>
     *                     </ul>
     *                 </li>
     *                 <li>@Import(AutoConfigurationImportSelector.class)：【重点】
     *                 将类{@link AutoConfigurationImportSelector}加入Spring容器。
     *                 {@link Import}注解：导入类到Spring容器中。
     *                 </li>
     *             </ul>
     *         </li>
     *         <li>@ComponentScan(excludeFilters=
     *             { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class)
     *             , @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })：
     *             {@link ComponentScan} ：将路径下合适的类加载到容器中。
     *             并且定义了两个过滤规则：{@link TypeExcludeFilter}和{@link AutoConfigurationExcludeFilter}。
     *             <ul>
     *                 <li>{@link TypeExcludeFilter}：加载spring bean容器中所有针对TypeExcludeFilter的扩展，并循环遍历这些扩展类调用其match方法。</li>
     *                 <li>{@link AutoConfigurationExcludeFilter}：过滤掉会自动配置的配置类，避免重复。</li>
     *             </ul>
     *         </li>
     *     </ol>
     * </p>
     */
     public static void study_2(){}

    /**
     * <p> 四. @Import(AutoConfigurationPackages.Registrar.class)【重点】</p>
     * <p> Registrar类中的
     * {@link AutoConfigurationPackages.Registrar#registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry)}
     * 方法中设置SpringBoot启动类所在的包作为扫描包，后续要针对这个包进行扫描。
     * 扫描包意思就是确定要扫描哪个包中的所有bean交由Spring IoC容器创建并管理。
     */
    public static void study_3(){}

    /**
     * <p> 五. @Import(AutoConfigurationImportSelector.class)【重点】
     * <p> {@link AutoConfigurationImportSelector}类实现了很多接口，这些接口可以分为三类：
     * <ul>
     *     <li>以Aware结尾的接口：{@link BeanClassLoaderAware},{@link ResourceLoaderAware},
     *     {@link BeanFactoryAware}, {@link EnvironmentAware}。</li>
     *     <li>{@link Ordered}接口：Spring IoC容器加载创建bean的顺序。</li>
     *     <li>{@link DeferredImportSelector}接口：
     *     （1）该接口继承了{@link ImportSelector}接口，
     *         {@link ImportSelector#selectImports(AnnotationMetadata)}方法
     *         返回要加载（到SpringIoC容器）的 配置类（Configuration注解标注的类）或者具体Bean
     *         类的全限定名的String数组。
     *      （2）该接口中定义了一个接口{@link DeferredImportSelector.Group}，该接口的
     *          {@link DeferredImportSelector.Group#process(AnnotationMetadata, DeferredImportSelector)}
     *          方法很重要。
     *     </li>
     * </ul>
     * <p> 1. 从{@link AutoConfigurationImportSelector.AutoConfigurationGroup#process(AnnotationMetadata, DeferredImportSelector)}
     * 方法开始看起。
     */
    public static void study_4(){
        // 从AutoConfigurationImportSelector中的process方法开始看起

        // public void process(AnnotationMetadata annotationMetadata,
        //                     DeferredImportSelector deferredImportSelector) {
        //
        //     Assert.state(deferredImportSelector instanceof AutoConfigurationImportSelector,
        //					() -> String.format("Only %s implementations are supported, got %s",
        //					AutoConfigurationImportSelector.class.getSimpleName(),
        //					deferredImportSelector.getClass().getName()));
        //
        //     AutoConfigurationEntry autoConfigurationEntry =
        //                  ((AutoConfigurationImportSelector) deferredImportSelector)
        //					.getAutoConfigurationEntry(annotationMetadata);
        //
        //     this.autoConfigurationEntries.add(autoConfigurationEntry);
        //
        //     for (String importClassName : autoConfigurationEntry.getConfigurations()) {
        //		   this.entries.putIfAbsent(importClassName, annotationMetadata);
        //     }
        // }
    }

    public static void main(String[] args) {
    }
}
