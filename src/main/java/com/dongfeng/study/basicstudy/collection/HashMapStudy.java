package com.dongfeng.study.basicstudy.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * <b> {@link java.util.HashMap}学习 </b>
 * @author eastFeng
 * @date 2021-04-24 23:27
 */
public class HashMapStudy {
    public static void main(String[] args) {
        /*
         * HashMap由Hash和Map两个单词组成，这里Map不是地图的意思，而是表示映射关系，是一个接口，
         * 实现Map接口有多种方式，HashMap实现的方式利用了哈希（Hash）。
         *
         * HashMap中的键值对没有顺序，因为hash值是随机的。
         */

        HashMap<Integer, String> map = new HashMap<>(16);
        map.put(1, "a");

        // Map接口介绍
        mapStudy();

        // HashMap基本原理
        basePrinciple();
    }

    /**
     * Map接口
     */
    public static void mapStudy(){
        /*
         * Map有键和值的概念。
         * 一个键映射到一个值，Map按照键存储和访问值，键不能重复，即一个键只会存储一份，给同一个键重复设值会覆盖原来的值。
         * 使用Map可以方便地处理需要根据键访问对象的场景，比如：
         * 1) 一个词典应用，键可以为单词，值可以为单词信息类，包括含义、发音、例句等；
         * 2) 统计和记录一本书中所有单词出现的次数，可以以单词为键，以出现次数为值；
         * 3) 管理配置文件中的配置项，配置项是典型的键值对；
         * 4) 根据身份证号查询人员信息，身份证号为键，人员信息为值。
         *
         * Set是一个接口，表示的是数学中的集合概念，即没有重复的元素集合。
         * 它扩展了Collection，但没有定义任何新的方法，不过，它要求所有实现者都必须确保Set的语义约束，即不能有重复元素。
         * Map中的键是没有重复的，所以ketSet()返回了一个Set。
         *
         * keySet()、values()、entrySet()有一个共同的特点，它们返回的都是视图，不是复制的值，基于返回值的修改会直接修改Map自身，
         * 比如：map.keySet().clear() 会删除所有键值对。
         */
        Map<String, Integer> map = new HashMap<>(16);
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+ "=" + entry.getValue());
        }

        map.keySet().clear();
        System.out.println(map.size());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+ "=" + entry.getValue());
        }
    }

    /**
     * HashMap基本原理
     */
    public static void basePrinciple(){

        /*
         * HashMap内部有如下几个实例变量：
         *
         * transient Node<K,V>[] table;   // table数组的每个元素指向一个单向链表或者红黑树，链表和红黑树中的每个节点代表一个键值对。
         * transient Set<Map.Entry<K,V>> entrySet;  // 此Map中所有键值对的集合
         * transient int size;  // 此Map中实际键值对的数量
         * transient int modCount;  // 内部修改次数
         * int threshold;  // 阈值
         * final float loadFactor;  // 负载因子：表示整体上table数组被占用的程度
         *
         *
         * 当添加键值对进去之后，table就不是空数组了，随着键值对的添加，table会进行扩容，扩展的粗略类似于ArrayList。
         * 当添加第一个元素时，默认分配数组大小为16，不过并不是size大于16的时候，table再进行扩展，而是size大于threshold的时候进行扩容。
         * threshold = table.length*loadFactory。
         * 比如，如果table. length为16, loadFactor为0.75，则threshold为12。
         *
         * HashMap中，数组的长度length为2的幂次方，h&(length-1)等同于求模运算h%length。
         *
         * HashMap中的数组扩容，扩容为原来的2倍。
         */

        // HashMap源码学习
        com.dongfeng.study.sourcecode.java8.util.HashMap<String, Integer> map
                = new com.dongfeng.study.sourcecode.java8.util.HashMap<>(16);
    }
}
