package com.dongfeng.study.config.aop.flowlimit;

import com.dongfeng.study.bean.base.Response;
import com.dongfeng.study.util.MethodUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * redis分布式限流处理器（切面）
 *
 * <p> 作用于注解（声明）了 {@link RedisLimiter} 注解的方法的切面。
 *
 * @author eastFeng
 * @date 2020/8/15 - 13:05
 */
@Slf4j
@Aspect
//@Component
public class RedisLimiterAspect{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> getRedisScript;

    /**
     * 初始化getRedisScript
     * 整个Lua脚本一起执行，保证了原子性
     */
    @PostConstruct
    public void init(){
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redisLimiter.lua")));
        log.info("【RedisLimiterAspect】分布式限流处理lua脚本加载完成");
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.dongfeng.study.config.aop.flowlimit.RedisLimiter)")
    public void redisLimiter(){}

    /**
     * 环绕通知
     */
    @Around("redisLimiter()")
    public Object interceptor(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        try {
            // 获取RedisLimiter注解  【Spring反射工具AnnotationUtils比较好用, 可以躲过代码检查】
            RedisLimiter limiter = AnnotationUtils.findAnnotation(method, RedisLimiter.class);
            if (limiter!=null && StringUtils.isNotBlank(limiter.value())){
                // 限流key （定义了key就用定义的可以，否则就根据类路径和方法生成一个唯一key）
                String limitKey = StringUtils.defaultIfBlank(limiter.value(), MethodUtil.resolveMethodName(method));
                // 限流阈值
                long limitTimes = limiter.times();
                // 限流超时时间
                long timeout = limiter.timeout();
                // 限流key放入list
                List<String> keyList = new ArrayList<>(1);
                keyList.add(limitKey);

                // 调用lua脚本并执行
                Long result = stringRedisTemplate.execute(getRedisScript, keyList, String.valueOf(timeout),
                        String.valueOf(limitTimes));

                if (result==null || result==0){
                    // result为0就是被限流了(lua脚本中指定的)
                    log.info("【{}】被限流----系统繁忙，请稍后再试", limitKey);
                    return Response.errorInstance(2020, "系统繁忙，请稍后再试");
                }
            }
        }catch (Exception e){
            log.error("error:{}", e.getMessage(), e);
        }

        // 执行方法
        return point.proceed();
    }
}
