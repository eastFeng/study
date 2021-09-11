package com.dongfeng.study.basicstudy.collection;

import java.util.HashSet;

/**
 * <b> {@link java.util.HashSet}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-25 17:50
 */
public class HashSetStudy {
    public static void main(String[] args) {
        /*
         * HashSet有很多应用场景，比如：
         * 1）排重，如果对排重后的元素没有顺序要求，则HashSet可以方便地用于排重；
         * 2）保存特殊值，Set可以用于保存各种特殊值，程序处理用户请求或数据记录时，根据是否为特殊值判断是否进行特殊处理，比如保存IP地址的黑名单或白名单；
         * 3）集合运算，使用Set可以方便地进行数学集合中的运算，如交集、并集等运算，这些运算有一些很现实的意义。
         * 比如，用户标签计算，每个用户都有一些标签，两个用户的标签交集就表示他们的共同特征，交集大小除以并集大小可以表示他们的相似程度。
         */


        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(1);
        boolean remove = hashSet.remove(1);

        // Set接口学习
        setStudy();
    }

    /**
     * {@link java.util.Set}接口学习
     */
    public static void setStudy(){
        /*
         * Set表示的是没有重复元素、且不保证顺序的容器接口，它扩展了Collection，
         * 但没有定义任何新的方法，不过，对于其中的一些方法，它有自己的规范。
         */
    }

    /**
     * HashSet基本原理
     */
    public static void basePrinciple(){
        /*
         * // HashSet内部是用HashMap实现的，它内部有一个HashMap实例变量
         * private transient HashMap<E,Object> map;
         *
         * // Map有键和值，HashSet相当于只有键，值都是相同的固定值，这个值的定义为
         * private static final Object PRESENT = new Object();
         *
         * 它实现了Set接口，内部实现利用了HashMap，有如下特点：
         * 1）没有重复元素；
         * 2）可以高效地添加、删除元素、判断元素是否存在，效率都为O(1)；
         * 3）没有顺序。
         * HashSet可以方便高效地实现去重、集合运算等功能。
         * 如果要保持添加的顺序，可以使用HashSet的一个子类LinkedHashSet。
         * Set还有一个重要的实现类TreeSet，它可以排序。
         */
    }
}
