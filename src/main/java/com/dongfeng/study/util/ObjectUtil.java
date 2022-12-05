package com.dongfeng.study.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;

/**
 * @author eastFeng
 * @date 2022-12-05 23:27
 */
@Slf4j
public class ObjectUtil {

    /**
     * 将对象转换为目标类型。
     * <p> 如果参数异常或者强转不了，返回null。
     *
     * @param source 要转换类型的对象
     * @param targetType 目标类型
     * @return 强转后的对象
     * @param <T> 目标类型
     */
    public static <T> T castType(Object source, Class<T> targetType){
        log.info("castType source:{},targetType:{}", source, targetType);
        if (source==null || targetType==null){
            log.info("castType source or targetType is NULL");
            return null;
        }

        // source与该targetType表示的对象不兼容 并且
        // targetType是数组类型 并且
        // source与targetType数组的组件类型表示的对象兼容。
        // 意思就是：targetType是数组，source不是数组，但是source的类型和targetType数组的组件类型匹配，
        // value就是数组的第一个值即可。
        if (!targetType.isInstance(source)
                && targetType.isArray()
                && targetType.getComponentType().isInstance(source)){
            Object array = Array.newInstance(targetType.getComponentType(), 1);
            Array.set(array, 0, source);
            source = array;
        }

        // 其他情况，直接判断source与该targetType表示的对象是否兼容
        if (!targetType.isInstance(source)){
            // 转换不了，返回null
            return null;
        }

        // 最后类型强转
        return (T) source;
    }
}
