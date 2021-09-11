package com.dongfeng.study.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eastFeng
 * @date 2021-01-27 15:55
 */
public class MethodUtil {

    private static final Map<Method, String> METHOD_NAME_MAP = new ConcurrentHashMap<Method, String>();

    private static final Object LOCK = new Object();

    /**
     * <b>获取方法的全路径名称</b>
     * <p>解析方法名称，然后缓存到map中
     *
     * @param method Method对象
     * @return 解析的方法名称
     */
    public static String resolveMethodName(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Null method");
        }
        // 首先从map中获取
        String methodName = METHOD_NAME_MAP.get(method);
        if (StringUtils.isBlank(methodName)) {
            // map中没有
            synchronized (LOCK) {
                methodName = METHOD_NAME_MAP.get(method);
                // 再次从map中获取并判断
                if (StringUtils.isBlank(methodName)) {
                    StringBuilder sb = new StringBuilder();
                    // 方法所在类的全类名
                    String className = method.getDeclaringClass().getCanonicalName();
                    // 方法名称
                    String name = method.getName();
                    // 方法所有参数的类型
                    Class<?>[] params = method.getParameterTypes();

                    sb.append(className).append(":").append(name);
                    sb.append("(");

                    // 拼装参数类型全类名
                    int paramPos = 0;
                    for (Class<?> clazz : params) {
                        sb.append(clazz.getCanonicalName());
                        if (++paramPos < params.length) {
                            sb.append(",");
                        }
                    }
                    sb.append(")");
                    methodName = sb.toString();

                    METHOD_NAME_MAP.put(method, methodName);
                }
            }
        }
        return methodName;
    }

    /**
     * 为了测试使用
     */
    static void clearMethodMap() {
        METHOD_NAME_MAP.clear();
    }
}
