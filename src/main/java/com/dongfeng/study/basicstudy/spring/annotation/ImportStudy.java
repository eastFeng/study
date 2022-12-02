package com.dongfeng.study.basicstudy.spring.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

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
     * <p> Class<?>[] value();
     * <p>1. Import注解能添加（标注）到类上，接口上，注解上，枚举上。({@link Import}注解Target类型是{@link ElementType#TYPE})
     *
     * <p>2. Import注解作用：通过快速导入的方式把实例对象加入到Spring容器当中，对于第三方的class来说尤其方便。
     *
     * <p>3. Import注解用法
     * <ul>
     *     <li>直接导入类（普通类）。</li>
     *     <li>导入{@link ImportSelector}接口的实现类</li>
     *     <li>导入{@link ImportBeanDefinitionRegistrar}接口的实现类。</li>
     * </ul>
     *
     *
     *
     */
    public static void main(String[] args) {
    }
}
