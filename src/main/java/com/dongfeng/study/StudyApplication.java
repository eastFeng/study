package com.dongfeng.study;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <b> Spring Boot 的启动类
 * <p> <b> {@link SpringBootApplication} 注解： 自动配置的实现，表示当前类是SpringBoot的启动类。
 * 此注解等同于：@Configuration + @EnableAutoConfiguration + @ComponentScan的组合。
 * <p> 配置：指的是javaConfig配置 而不是xml配置
 * <p> 自动配置：Spring Boot帮我们把那些配置类提前写好了
 *
 * <p> {@link org.springframework.boot.SpringBootConfiguration}注解：声明(标注)当前类是根配置类
 * 此注解是@Configuration注解的派生注解，跟@Configuration注解的功能一致，标注这个类是一个配置类。
 * 只不过@SpringBootConfiguration是SpringBoot的注解，@Configuration是Spring的注解。
 *
 * <p> {@link org.springframework.context.annotation.Configuration}注解：通过对bean对象的操作替代Spring中的xml文件。
 *
 * <p> {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration}注解：开启自动配置的核心。
 * Springboot自动配置：尝试根据添加的jar依赖自动配置Spring应用。是@AutoConfigurationPackage注解和@Import(AutoConfigurationImportSelector.class)
 * 注解的组合。
 *
 * <p> {@link org.springframework.boot.autoconfigure.AutoConfigurationPackage}注解：
 * 添加该注解的类所在的package作为自动配置package进行管理。自动注入主类所有包下所有的加了注解（@Controller，@Services等）的类，以及配置类（@Configuration）。
 *
 * <p> {@link org.springframework.context.annotation.Import}注解：导入类。
 * 1. 直接导入普通的类。 2. 导入实现了{@link org.springframework.context.annotation.ImportSelector}接口的类
 * 3. 导入实现了{@link org.springframework.context.annotation.ImportBeanDefinitionRegistrar}接口的类
 *
 * <p> @Import(AutoConfigurationImportSelector.class)
 * <p> {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector}类：是自动配置的核心类
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
 * <p> selectImports会过滤自动配置类:
 *    1.会移除exclude里面的配置类
 *    2.filter：根据自动配置类的条件---自动配置类上标注的一系列@ConditionalOnXXX注解
 * <p> {@link org.springframework.context.annotation.ComponentScan}注解：组件扫描，可自动发现和装配一些Bean。
 * @author eastFeng
 * @date 2020/8/15 - 12:49
 */
@Slf4j
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan // 在SpringBoot启动时会扫描有@WebServlet、@WebFilter注解的类，并将该类实例化
@MapperScan(basePackages = "com.dongfeng.study.bean.mapper") // Mybatis:扫描mapper接口
@SpringBootApplication
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

        /*
         * Springboot的启动类的作用是启动Springboot项目，是基于main方法来运行的。
         * 注意：启动类在启动时会做注解扫描（@Controller、@Service、@Repository...），
         * 扫描位置为同包或者子包下的注解，所以启动类的位置应该放在包的根下。
         */
        try {
            // 启动Tomcat  生命周期
            SpringApplication.run(StudyApplication.class, args);
        } catch (Exception e) {
            // 查看项目启动报错信息
//            e.printStackTrace();
            log.error("SpringBoot启动报错 error:{}", e.getMessage(), e);
        }
    }


    /*
     * SpringBoot的四个特点：
     * 1. 使开发者快速搭建Spring环境
     * 2. 提供一系列开箱即用的starter
     * 3. 提供许多非业务性的功能
     * 4. 没有XML配置，没有代码生成
     */

}
