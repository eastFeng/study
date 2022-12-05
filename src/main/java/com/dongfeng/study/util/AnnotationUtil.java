package com.dongfeng.study.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2022-12-05 14:31
 */
@Slf4j
public class AnnotationUtil {

    /**
     * 获取注解某属性的值，并指定该值的类型。
     *
     * @param map 注解的所有属性（key是注解的属性名称，value是对应的属性值）
     * @param attributeName 属性名称
     * @param specifyType 指定属性值的类型
     * @param <T> 指定的类型
     * @return （指定了类型的）属性的值
     */
    public static <T> T getSpecifyTypeAttribute(Map<String, Object> map,
                                                String attributeName,
                                                Class<T> specifyType){
        log.info("getSpecifyTypeAttribute attributeName:{},specifyType:{}",
                attributeName,specifyType);

        // 获取属性的值
        Object value = map.get(attributeName);
        log.info("getSpecifyTypeAttribute value:{}", value);
        if (value == null){
            return null;
        }
        log.info("getSpecifyTypeAttribute value.getClass:{}",
                value.getClass());
        // 如果值是异常类
        if (value instanceof Throwable){
            return null;
        }

        // value与该specifyType表示的对象不兼容 并且
        // specifyType是数组类型 并且
        // value与数组的组件类型表示的对象兼容。
        // 意思就是：specifyType是数组，value不是数组，但是value的类型和数组的组件类型匹配，
        // value就是数组的第一个值即可。
        if (!specifyType.isInstance(value)
                && specifyType.isArray()
                && specifyType.getComponentType().isInstance(value)){
            Object array = Array.newInstance(specifyType.getComponentType(), 1);
            Array.set(array, 0, value);
            value = array;
        }

        // 其他情况，直接判断value与该specifyType表示的对象是否兼容
        if (!specifyType.isInstance(value)){
            return null;
        }

        // 最后类型强转
        return (T)value;
    }
}
