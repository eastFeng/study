package com.dongfeng.study.config.aop.flowlimit;

import com.dongfeng.study.bean.base.BaseResponse;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * guava RateLimiter限流处理器（切面）
 *
 * @author eastFeng
 * @date 2020/8/15 - 13:39
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {
    private ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    /**
     * 指定注解作为切点
     */
    @Pointcut("@annotation(com.dongfeng.study.config.aop.flowlimit.ARateLimiter)")
    public void rateLimit(){}

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        final Method method = signature.getMethod();

        // 获取ARateLimiter注解
        final ARateLimiter limiter = AnnotationUtils.findAnnotation(method, ARateLimiter.class);
        final String rateKey = limiter.value();
        if (limiter!=null && limiter.qps()>ARateLimiter.NOT_RATE && StringUtils.isNotBlank(rateKey)){
            final double qps = limiter.qps();
            if (RATE_LIMITER_MAP.get(rateKey)==null){
                // 使用qps初始化RateLimiter
                RATE_LIMITER_MAP.put(rateKey, RateLimiter.create(qps));
                log.info("【{}】的QPS设置为：{}", limiter.value(),qps);
            }

            // 尝试获取许可证
            if (RATE_LIMITER_MAP.get(rateKey)!=null && !RATE_LIMITER_MAP.get(rateKey).tryAcquire(limiter.timeout(),
                    limiter.timeUnit())){
                // 没有获取到令牌
                log.info("【{}没有获取到令牌】请求频繁，请稍后再试~", rateKey);
                return BaseResponse.errorInstance(2020, "系统繁忙，请稍后再试");
            }
        }

        // 执行方法
        return point.proceed();
    }
}
