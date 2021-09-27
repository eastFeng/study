package com.dongfeng.study.config.properties;

import com.dongfeng.study.config.configuration.DemoConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * <p> {@link ConfigurationProperties}注解 : 外部化配置注解。
 *     是Springboot的注解，不能读取其他配置文件，只能读取Springboot的application配置文件里面的配置。
 *
 * <p> ConfigurationProperties : 配置属性
 *
 * <p> 如果要绑定和验证一些外部属性（从.properties文件），可以将该注解添加到类上或者@Configuration类中的@Bean方法上。
 *
 * <p> 当该注解作用于类上时，如果想要有效的绑定配置，那么该类需要有@Component注解。并且类的实例字段要有对应的set方法。
 *     例如：{@link org.springframework.boot.autoconfigure.amqp.RabbitProperties}
 *
 * <p> 当将该注解作用于方法上时，如果想要有效的绑定配置，那么该方法需要有@Bean注解 且 所属Class需要有@Configuration注解。
 *     例如：{@link DemoConfiguration#getTestVo()}
 * <p> 该注解有一个prefix属性，通过指定的前缀，绑定配置文件中的配置。
 *
 * <p> prefix: 可绑定到此对象的有效属性的前缀。一个有效的前缀是由一个或多个用点隔开的词来定义的（例如：acme.system.feature)。
 *
 * <p> 请注意，与@Value相反，由于属性值是外部化的，因此不计算SpEL表达式。
 *
 * @author eastFeng
 * @date 2020-11-20 14:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "my.test.cpd")
public class ConfigurationPropertiesDemo {
    /**
     * 统计线程大小
     */
    private Integer statisticsTaskPoolSize;
    /**
     * 上传文件线程数大小
     */
    private Integer uploadTaskPoolSize;
    /**
     * 实名认证URL
     */
    private String identityCheckUrl;
    /**
     * 邀请码
     */
    private Map<String, String> invitationCodeMap;
    /**
     * 姓名列表
     */
    private List<String> nameList;
}
