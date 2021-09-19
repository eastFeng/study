package com.dongfeng.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * <b> {@link SpringBootApplication}： 自动配置的实现
 * <p> 配置：指的是javaConfig配置 而不是xml配置
 * <p> 自动配置：Spring Boot帮我们把那些配置类提前写好了
 *
 * <p> {@link org.springframework.boot.SpringBootConfiguration}：声明(标注)当前类是根配置类
 *
 * <p> {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration}：开启自动配置的核心
 *
 * <p> {@link org.springframework.boot.autoconfigure.AutoConfigurationPackage}：
 * 添加该注解的类所在的package作为自动配置package进行管理
 *
 * <p> {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector}：是自动配置的核心类
 *
 * <p> {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#selectImports(AnnotationMetadata)}：
 * 是自动配置的核心方法。
 * 该方法的返回值的String必须是java类路径，返回的类都会被加载到Spring容器中。
 * <p> selectImports方法实现步骤：
 * <p> 1. {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getAutoConfigurationEntry(AnnotationMetadata)}：
 * 该方法是selectImports方法的和核心方法，会返回所有自动配置类
 * <p> 2. getAutoConfigurationEntry中会调用
 * {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getCandidateConfigurations(AnnotationMetadata, AnnotationAttributes)}方法：
 * 该方法会调用 {@link org.springframework.core.io.support.SpringFactoriesLoader#loadFactoryNames(Class, ClassLoader)}方法，
 * 该方法会扫描org.springframework.boot:spring-boot-autoconfigure:2.3.3.RELEASE下的spring.factories配置文件并传入key。
 *
 * <p>loadSpringFactories {@link }: 加载整个配置文件并缓存
 *
 * <p>selectImports会过滤自动配置类:
 *    1.会移除exclude里面的配置类
 *    2.filter：根据自动配置类的条件---自动配置类上标注的一系列@ConditionalOnXXX注解
 *
 * @author eastFeng
 * @date 2020/8/15 - 12:49
 */
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@MapperScan(basePackages = "com.dongfeng.study.bean.mapper") // 添加扫描mapper接口的注解
public class StudyApplication {

    /**
     * {@link SpringApplication} :
     * <p> 该类用于从main方法引导(bootstrap)和启动(launch)Spring应用程序。
     * <p> 默认情况下，该类将执行以下步骤来引导应用程序:
     * <ol>
     * <li> 创建适当的ApplicationContext实例（取决于类路径）
     * <li> 注册一个CommandLinePropertySource，将命令行参数作为Spring属性公开
     * <li> 刷新应用程序上下文，加载所有单例bean
     * <li> 触发任何CommandLineRunner bean
     * </ol>
     * @param args 启动参数
     */
    public static void main(String[] args) {
        try {
            // 启动Tomcat  生命周期
            SpringApplication.run(StudyApplication.class, args);
        } catch (Exception e) {
            // 查看项目启动报错信息
            e.printStackTrace();
        }
    }

}
