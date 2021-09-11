package com.dongfeng.study.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eastFeng
 * @date 2020-10-22 10:15
 */
@Slf4j(topic = "----")
public class StringUtil {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(10);
        list.add("hh");
        list.add("LL");
        list.add("aa");
        list.add("11");

        System.out.println(listToString(list, ","));
        System.out.println(listToString(new ArrayList<>(), ","));
        System.out.println(listToString(null, ","));

        System.out.println(listToStringByStream(list, "."));
    }


    /**
     * String 转换为 List<String>,,用逗号分隔符
     */
    public static List<String> stringToList(String value){
        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(value)){
            return list;
        }
        String[] split = value.split(",|，");
        list = Arrays.asList(split);
        return list;
    }

    /**
     * List<String>转String
     *
     * @param list List<String>
     * @param connectStr list转String的时候list中每个元素之间的连接字符串
     * @return 转换后的字符串
     */
    public static String listToString(List<String> list, String connectStr){
        if (CollectionUtil.isEmpty(list)){
            return "";
        }
        int size = list.size();
        if (size==1){
            return list.get(0);
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<size; i++){
            if (i!=0){
                sb.append(connectStr);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * List<String>转String
     * stream 简洁写法
     *
     * @param list List<String>
     * @param connectStr list转String的时候list中每个元素之间的连接字符串
     * @return 转换后的字符串
     */
    public static String listToStringByStream(List<String> list, String connectStr){
        if (CollectionUtil.isEmpty(list)){
            return "";
        }

        return list.stream().collect(Collectors.joining(connectStr));
    }

    public static String listToStringByJoin(List<String> list, String connectStr){
        if (CollectionUtil.isEmpty(list)){
            return "";
        }
        return String.join(connectStr, list);
    }


}
