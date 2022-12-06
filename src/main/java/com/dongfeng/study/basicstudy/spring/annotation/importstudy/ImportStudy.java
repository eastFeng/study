package com.dongfeng.study.basicstudy.spring.annotation.importstudy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.ElementType;

/**
 * {@link org.springframework.context.annotation.Import}注解学习
 *
 * @author eastFeng
 * @date 2022-12-02 11:40
 */
public class ImportStudy {

    /**
     * {@link Import}注解只有一个属性：{@link Import#value()}
     * <p> Class<?>[] value() : Class类数组
     * <p>1. Import注解能添加（标注）到类上，接口上，注解上，枚举上。({@link Import}注解Target类型是{@link ElementType#TYPE})
     *       不能放在方法上。
     *
     * <p> 2. Import注解作用：通过快速导入的方式把实例对象加入到Spring IoC容器当中，对于第三方的class来说尤其方便。
     * <ul>
     *     <li>导入要注入的bean对应的字节码。</li>
     *     <li>被导入的bean无需使用注解声明为bean。</li>
     *     <li>
     *         使用{@link Import}注解加载bean可以有效的降低源代码与Spring技术的耦合度，
     *         在Spring技术底层及诸多框架的整合中大量使用。
     *     </li>
     *     <li>用{@link Import}注解的方式比使用{@link Configuration}注解+{@link Bean}注解更简单快捷。</li>
     * </ul>
     *
     * <p>3. Import注解用法、可以导入三种类
     * <p> 1) 直接导入普通类（包括Configuration注解标注的配置类）。
     *
     * <p> 2) 导入{@link ImportSelector}接口的实现类，用于（个性化）动态批量加载Bean。
     * <p> 步骤：实现{@link ImportSelector}接口，在{@link ImportSelector#selectImports(AnnotationMetadata)}方法中
     *          返回要创建bean的类的全类名。然后使用{@link Import}注解指定{@link ImportSelector}接口的实现类。
     *
     * <p> 3) 导入{@link ImportBeanDefinitionRegistrar}接口的实现类，用于个性化动态加载。
     *
     *
     *
     */
    public static void main(String[] args) {
        // 初始化容器
        ApplicationContext context =
                new AnnotationConfigApplicationContext(ImportStudyConfiguration.class);

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println("============================ Start ==============================");
        for (String name : beanDefinitionNames){
            System.out.println(name);
        }
        System.out.println("============================ End ==============================");

        // 根据类型获取bean
        Person_01 person_01 = context.getBean(Person_01.class);
        person_01.say();


    }
}
