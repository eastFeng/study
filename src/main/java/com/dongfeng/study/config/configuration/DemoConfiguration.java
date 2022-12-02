package com.dongfeng.study.config.configuration;

import com.dongfeng.study.bean.vo.TestVo;
import com.dongfeng.study.bean.vo.UserPunch;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <p> {@link Configuration}: 指示一个类声明 一个或者多个{@link Bean}方法。
 * <p> {@link Configuration}用于定义配置类，定义的配置类可以替换xml文件，一般和{@link Bean}注解联合使用。
 *
 * @author eastFeng
 * @date 2020-11-20 16:53
 */
@Configuration
public class DemoConfiguration {

    /**
     * <p> {@link Bean}是方法级别的注解
     * <p> {@link Bean}注解指示方法生成一个由Spring容器管理的bean
     * <p> 方法返回的结果bean会由Spring容器管理
     * <p> 通常{@link Bean}注解的方法在@Configuration注解的类中声明
     * <p> 方法名可以任意的
     *
     * @return {@link TestVo}实例
     */
    @Bean
    @ConfigurationProperties(prefix = "my.test.testvo")
    public TestVo getTestVo(){
        return new TestVo();
    }

    /**
     * <p> {@link Component}是类级别的注释，{@link Bean}是方法级别的，
     * 所以{@link Component}只是类的源代码可编辑时的一个选项。
     * {@link Bean}总是可以使用的，但是它更冗长。
     *
     * <p> {@link Component}与Spring的自动检测兼容，但{@link Bean}需要手动实例化类。
     *
     * <p> 使用{@link Bean}将Bean的实例化与其类定义解耦。这就是为什么我们可以使用它将第三方类变成spring bean的原因。
     * 这也意味着我们可以引入逻辑来决定bean使用几个可能的实例选项中的哪一个。
     *
     * @return {@link UserPunch}对象
     */
    @Bean
    public UserPunch getUserPunch(){
        // 需要手动实例化类
        UserPunch userPunch = new UserPunch();
        userPunch.setProvince("河南");
        return userPunch;
    }

}
