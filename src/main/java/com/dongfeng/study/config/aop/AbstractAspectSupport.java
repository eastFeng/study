package com.dongfeng.study.config.aop;

import com.dongfeng.study.util.MethodUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 切面的一些公共方法
 *
 * @author eastFeng
 * @date 2021-01-27 15:03
 */
public abstract class AbstractAspectSupport {

    protected String getResourceName(String resourceName, Method method) {
        // 如果resourceName不为空，则返回resourceName
        if (StringUtils.isNotBlank(resourceName)) {
            return resourceName;
        }

        // 分析目标方法的名称。
        return MethodUtil.resolveMethodName(method);
    }

    /**
     *
     * @param joinPoint ProceedingJoinPoint对象实例
     * @return Method
     */
    protected Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = getDeclaredMethodFor(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }


    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}
