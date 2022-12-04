package com.dongfeng.study;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 *
 * <b> SpringBoot的启动类 </b>
 * <p> 1. <b>{@link SpringBootApplication} 注解： 自动配置的实现，表示当前类是SpringBoot的启动类。</b>
 * <p> 此注解等同于：@Configuration + @EnableAutoConfiguration + @ComponentScan的组合。
 * <p> 这里的配置指的是javaConfig配置 而不是xml配置。
 * <p> 自动配置：SpringBoot帮我们把那些配置类提前写好了。
 *
 * <p> 2. <b>{@link SpringBootConfiguration}注解：声明（标注）当前类是根配置类。</b>
 * <p> 此注解是{@link Configuration}注解的派生注解，
 * 跟{@link Configuration}注解的功能一致，标注这个类是一个配置类。
 * 只不过{@link SpringBootConfiguration}是SpringBoot的注解，{@link Configuration}是Spring的注解。
 * <p> {@link Configuration}注解：用于定义配置类。
 *
 * <p> 3. <b>{@link EnableAutoConfiguration}注解：开启自动配置的核心。</b>
 * <p> 是{@link AutoConfigurationPackage}注解和@Import(AutoConfigurationImportSelector.class)
 * 注解的组合。
 * <p> SpringBoot自动配置：尝试根据当前项目依赖的所有jar自动配置Spring应用。
 *
 * <p> 4. <b>{@link AutoConfigurationPackage}注解：
 * 添加该注解的类所在的package作为自动配置package进行管理。</b>
 * <p> 自动注入主类所有包下所有的标注了{@link Controller}、{@link Service}等注解的类，
 *     以及配置类（@Configuration）。
 *
 * <p> 5. {@link Import}注解：导入类。
 *   <ul>
 *       <li>直接导入普通的类。</li>
 *       <li>导入实现了{@link ImportSelector}接口的类。</li>
 *       <li>导入实现了{@link ImportBeanDefinitionRegistrar}接口的类。</li>
 *   </ul>
 * </p>
 *
 * <p> @Import(AutoConfigurationImportSelector.class)
 * <p> 6. <b>{@link AutoConfigurationImportSelector}类：是自动配置的核心类。</b>
 *
 * <p> <b>{@link AutoConfigurationImportSelector#selectImports(AnnotationMetadata)}：
 * 是自动配置的核心方法。</b>
 * 该方法的返回值的String必须是java类路径，返回的类都会被加载到Spring容器中。
 * <p> selectImports方法实现步骤：
 * <p> 6.1. {@link AutoConfigurationImportSelector#getAutoConfigurationEntry(AnnotationMetadata)}：
 * 该方法是selectImports方法的和核心方法，会返回所有自动配置类
 * <p> 6.2. getAutoConfigurationEntry中会调用
 * {@link AutoConfigurationImportSelector#getCandidateConfigurations(AnnotationMetadata, AnnotationAttributes)}方法：
 * 该方法会调用 {@link SpringFactoriesLoader#loadFactoryNames(Class, ClassLoader)}方法，
 * 该方法会扫描org.springframework.boot:spring-boot-autoconfigure:2.3.3.RELEASE下的spring.factories配置文件并传入key。
 *
 * <p>loadSpringFactories {@link }: 加载整个配置文件并缓存
 *
 * <p> selectImports会过滤自动配置类:
 *    1.会移除exclude里面的配置类
 *    2.filter：根据自动配置类的条件---自动配置类上标注的一系列@ConditionalOnXXX注解
 * <p> {@link ComponentScan}注解：组件扫描，可自动发现和装配一些Bean。
 * @author eastFeng
 * @date 2020/8/15 - 12:49
 */
@Slf4j
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan // 在SpringBoot启动时会扫描有@WebServlet、@WebFilter注解的类，并将该类实例化
@MapperScan(basePackages = "com.dongfeng.study.bean.mapper") // Mybatis:扫描mapper接口
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
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

    /**
     * <b> SpringBoot自动配置 </b>
     *
     * <p> 一 Springboot简化了很多dependency（依赖关系）配置，不用手动配置所有的依赖（dependency） </p>
     * <ol>
     *     <li> pom文件中的父依赖spring-boot-starter-parent，指定了starter的版本 </li>
     *     <li> 这些starter依赖会导入很多jar包 </li>
     *     <li> 我们只需要配置starter依赖就行 </li>
     * </ol>
     *
     * <p> 二 自动配置底层源码 </p>
     * <ol>
     *     <li> {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector}是自动配置的核心类 </li>
     *     <li> 关键方法1：
     *     {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#selectImports(AnnotationMetadata)}</li>
     *     <li> 关键方法1中调用的关键方法2：
     *     {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getAutoConfigurationEntry(AnnotationMetadata)} </li>
     *     <li> 关键方法2中调用了自动配置的核心方法：
     *     {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getCandidateConfigurations(AnnotationMetadata, AnnotationAttributes)}
     *     该方法主要是把自动配置列表加载进来
     *     </li>
     *     <li> 关键方法2中会移除exclude里面的配置类 </li>
     *     <li> 关键方法2中会filter（过滤）：根据自动配置类的条件---自动配置类上标注的一系列@ConditionalOnXXX注解 </li>
     * </ol>
     *
     * <p> 自动配置核心方法1：getCandidateConfigurations的源码分析 </p>
     * <pre>{@code
     * protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
     *     List<String> configurations = SpringFactoriesLoader.loadFactoryNames(
     *     getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
     *
     *     // 配置文件在：外部依赖包下的Maven:org.springframework.boot:spring-boot:2.3.3.RELEASE
     *     // 中的META-INF中的spring.factories文件
     *     Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
     * 			   + "are using a custom packaging, make sure that file is correct.");
     * 	   // 断言里message的意思是：在META-INF/spring.factories中找不到自动配置类。如果您使用的是自定义打包，请确保该文件正确无误。
     *     return configurations;
     * }
     * }</pre>
     *
     * 在配置文件spring.factories中，有{@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}
     * 、{@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}等等很多配置类。
     * <p> 以{@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}配置类为例：
     * <lo>
     *     <li>
     *     该配置类的方法{@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration#redisTemplate(RedisConnectionFactory)}
     *     上面的注解：@ConditionalOnMissingBean(name = "redisTemplate")，
     *     就是如果没有自定义{@link org.springframework.data.redis.core.RedisTemplate}类，
     *     该配置类就会创建RedisTemplate对象并注入到Spring的IoC容器中，所以我们就可以自动装配并直接使用RedisTemplate了。
     *     </li>
     *     <li>
     *     改配置类上有一行注解：@EnableConfigurationProperties(RedisProperties.class)。
     *     {@link org.springframework.boot.context.properties.EnableConfigurationProperties}注解的作用是
     *     使使用（被）{@link org.springframework.boot.context.properties.ConfigurationProperties}注解的类生效。
     *     RedisProperties类里有很多关于Redis的配置信息。
     *     关于@ConfigurationProperties注解的作用见{@link com.dongfeng.study.config.properties.ConfigurationPropertiesDemo}
     *     </li>
     * </lo>
     *
     *
     *
     */
    private static void autoConfigurationStudy(){
    }

}
