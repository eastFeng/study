package com.dongfeng.study.basicstudy.collection;

import java.util.*;

/**
 *
 * @author eastFeng
 * @date 2021-04-24 1:56
 */
public class CollectionStudy {
    public static void main(String[] args) {

    }

    public static void test(){
        /**
         *
         * Java 集合框架主要包括两种类型的容器，一种是集合（Collection），存储一个元素集合，另一种是图（Map），存储键/值对映射。
         * Collection 接口又有 3 种子类型，List、Set 和 Queue，再下面是一些抽象类，最后是具体实现类，
         * 常用的有 ArrayList、LinkedList、HashSet、LinkedHashSet、HashMap、LinkedHashMap 等等。
         *
         *
         *
         * Java集合类库也将接口（interface）与实现（implementation）分离。
         *
         * 在Java类库中，集合类的基本接口是Collection接口。
         *
         * 迭代器：Iterator
         * Collection接口的iterator方法用于返回一个实现了Iterator接口的对象。可以使用这个迭代器对象依次访问集合中的元素。
         * iterator方法：返回一个用于访问集合中每个元素的迭代器。
         *
         * Iterator接口包含4个方法：
         * 1. boolean hasNext();
         * 2. E next();
         * 3. default void remove()
         * 4. default void forEachRemaining(Consumer<? super E> action)
         *
         * 通过反复调用next方法，可以逐个访问集合中的每个元素。
         * 但是，如果到达了集合的末尾，next方法将抛出一个NoSuchElementException。
         * 因此，需要在调用next之前调用hasNext方法。如果迭代器对象还有多个供访问的元素，这个方法就返回true。
         *
         * Iterable接口：Iterable接口是Java集合框架的顶级接口，实现此接口使集合对象可以通过迭代器遍历自身元素。
         * Collection接口扩展了Iterable接口。
         *
         * Java迭代器（Iterator）查找操作与位置变更是紧密相连的。
         * 查找一个元素的唯一方法是调用next，而在执行查找操作的同时，迭代器的位置随之向前移动。
         *
         * 应该将Java迭代器认为是位于两个元素之间。当调用next时，迭代器就越过下一个元素，并返回刚刚越过的那个元素的引用。
         * （看ArrayList中Iterator的实现就明白了。）
         *
         *
         *
         */
        ArrayList<String> list =new ArrayList<>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        ListIterator<String> stringListIterator = list.listIterator();

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.get(1);

        Set<String> hashSet = new HashSet<>();
        hashSet.add("hhh");
    }
}
