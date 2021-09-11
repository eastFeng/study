package com.dongfeng.study.allconf.aop.flowlimit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * guava RateLimiter限流注解
 *
 * @author eastFeng
 * @date 2020/8/15 - 13:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ARateLimiter {
    /**
     * 默认每秒0个许可证
     */
    int NOT_RATE = 10;

//    //@AliasFor表示别名，它可以注解到自定义注解的两个属性上，表示这两个互为别名，也就是说这两个属性其实同一个含义。
//    /**
//     * @return qps 每秒许可证的个数
//     */
//    @AliasFor("qps") double value() default NOT_RATE;
//
//    /**
//     * @return qps 每秒许可证的个数
//     */
//    @AliasFor("value") double qps() default NOT_RATE;

    /**
     * @return 资源名称，标识唯一的RateLimiter
     */
    String value() default "";

    /**
     * @return qps 每秒许可证的个数
     */
    double qps() default NOT_RATE;

    /**
     * @return 获取许可证超时时长 默认0
     */
    int timeout() default 0;

    /**
     * @return 超时时间单位  默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
