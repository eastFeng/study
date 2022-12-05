package com.dongfeng.study.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2022-12-05 14:31
 */
@Slf4j
public class AnnotationUtil {

    /**
     * 获取注解某属性的值，并转换为指定/目标类型。
     * <p> 如果参数异常或者强转不了，返回null </p>
     *
     * @param map 注解的所有属性（key是注解的属性名称，value是对应的属性值）
     * @param attributeName 属性名称
     * @param targetType 指定属性值的目标类型
     * @param <T> 目标类型
     * @return （目标类型的）属性值
     */
    public static <T> T getSpecifyTypeAttribute(Map<String, Object> map,
                                                String attributeName,
                                                Class<T> targetType){
        log.info("getSpecifyTypeAttribute map:{},attributeName:{},targetType:{}",
                map, attributeName, targetType);
        if (map==null || map.isEmpty()){
            log.info("getSpecifyTypeAttribute map is Null or Empty, map:{}", map);
            return null;
        }
        if (StrUtil.isBlank(attributeName)){
            log.info("getSpecifyTypeAttribute attributeName is Blank, attributeName:{}", attributeName);
            return null;
        }
        if (targetType == null){
            log.info("getSpecifyTypeAttribute targetType is NULL, targetType:{}", targetType);
            return null;
        }

        // 获取属性的值
        Object value = map.get(attributeName);
        log.info("getSpecifyTypeAttribute value:{}", value);
        if (value == null){
            return null;
        }
        log.info("getSpecifyTypeAttribute value.getClass:{}", value.getClass());
        // 如果值是异常类
        if (value instanceof Throwable){
            return null;
        }

        // 对属性的值value进行类型转换
        return ObjectUtil.castType(value, targetType);
    }
}
