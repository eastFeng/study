package com.dongfeng.study.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eastFeng
 * @date 2020/8/15 - 14:40
 */
public class CollectionUtil {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello","hello","你好","你好","hi","hi","hi");
        List<String> list1 = duplicateRemove(list);
        System.out.println(list1);

        Long aa = null;
        System.out.println(aa);
    }

    public static <E> List<E> emptyList(){
        return new ArrayList<E>();
    }

    public static <E> Set<E> emptySet(){
        return new HashSet<E>();
    }

    public static <K,V> Map<K,V> emptyMap(){
        return new HashMap<K,V>(16);
    }

    public static <E> Queue<E> emptyQueue(){
        return new ArrayDeque<E>();
    }

    public static <E> Deque<E> emptyDeque(){
        return new ArrayDeque<E>();
    }


    /**
     * 按照指定的顺序排序
     */
    public static List<String> customer(){
        // 指定的顺序
        List<String> strings = new ArrayList<String>(){{
            add("景点");
            add("酒店");
            add("美食");
            add("购物");
            add("地铁/公交");
            add("机场/车站");
            add("停车场");
            add("游客服务中心");
            add("公厕");
        }};
        // 自定义比较器
        Comparator comparator = Comparator.comparing(String::toString, (x, y) -> {
            int i = strings.indexOf(x);
            int j = strings.indexOf(y);
            return i - j;
        });
        // 要排序的list集合
        List<String> stringList = new ArrayList<String>(){{
            add("公厕");
            add("酒店");
            add("美食");
            add("地铁/公交");
            add("游客服务中心");
            add("景点");
        }};
        return (List<String>) stringList.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * list去重原理:
     * HashSet实现了Set接口，不允许出现重复元素
     * LinkedHashSet是HashSet的子类
     * @param list  需要去重的list
     * @param <T>  List中数据类型
     * @return  去重后的list
     */
    private static <T> List<T> duplicateRemove(List<T> list){
        // 初始化HashSet对象，并把list对象元素赋值给HashSet对象
        LinkedHashSet<T> set = new LinkedHashSet<>(list);
        // HashSet对象添加至新的List集合，并返回
        return new ArrayList<>(set);
    }
}
