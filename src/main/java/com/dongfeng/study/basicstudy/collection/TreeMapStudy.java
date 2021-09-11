package com.dongfeng.study.basicstudy.collection;

import java.util.TreeMap;

/**
 * <b> {@link java.util.TreeMap}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-26 1:18
 */
public class TreeMapStudy<K,V> {
    public static void main(String[] args) {
        /*
         * HashMap有一个重要局限，键值对之间没有特定的顺序，在TreeMap中，键值对之间按键有序，TreeMap的实现基础是排序二叉树。
         */
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "a");
        boolean a = treeMap.containsValue("a");

        /*
         * 与HashMap相比，TreeMap同样实现了Map接口，但内部使用红黑树实现。
         * 红黑树是统计效率比较高的大致平衡的排序二叉树，这决定了它有如下特点：
         *
         * 1. 按键有序，TreeMap同样实现了SortedMap和NavigableMap接口，Map中的键值对是按照键进行排序的，
         *   可以方便地根据键的顺序进行查找，如第一个、最后一个、某一范围的键、邻近键等。
         * 2. 为了按键有序，TreeMap要求键实现Comparable接口或通过构造方法提供一个键的Comparator对象（键的比较器对象）。
         * 3. 根据键保存、查找、删除的效率比较高，为O(h), h为树的高度，在树平衡的情况下，h为log2(N), N为节点数。
         *
         * 应该用HashMap还是TreeMap呢？不要求排序，优先考虑HashMap，要求排序，考虑TreeMap。
         */
    }

    public static void basePrinciple(){
        /*
         *
         * // 用于维护此TreeMap中顺序的键的比较器，如果此TreeMap使用键的自然顺序，则为null
         * private final Comparator<? super K> comparator;
         *
         * // 红黑树的根节点
         * private transient TreeMap.Entry<K,V> root;
         *
         * // 此Map中实际键值对的数量
         * private transient int size = 0;
         *
         * // 内部修改次数，方便迭代期间检查是否发生结构性修改。
         * private transient int modCount = 0;
         *
         *
         * comparator就是比较器，在构造方法中传递，如果没传，就是null。
         * size为当前键值对个数。
         * root指向树的根节点，从根节点可以访问到每个节点，节点的类型为Entry。
         * Entry是TreeMap的一个内部类
         */
        // 源码：
        com.dongfeng.study.sourcecode.java8.util.TreeMap<Integer, String> integerStringTreeMap
                = new com.dongfeng.study.sourcecode.java8.util.TreeMap<>();
    }
}
