package com.dongfeng.study.config.aop.flowlimit;

import java.lang.annotation.*;

/**
 * redis分布式限流注解
 *
 * @author eastFeng
 * @date 2020/8/15 - 13:02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLimiter {

    /**
     * @return 资源的名称 (限流key)。建议必须自定义一个。
     */
    String value() default "";

    /**
     * @return 超时时间(限流时间), 单位秒
     */
    long timeout() default 1;

    /**
     * @return 超时时间内最多的访问次数 (限流阈值)
     */
    long times() default 10;
}
