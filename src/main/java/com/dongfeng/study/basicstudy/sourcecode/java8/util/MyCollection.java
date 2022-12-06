package com.dongfeng.study.basicstudy.sourcecode.java8.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author eastFeng
 * @date 2021-04-29 1:21
 */
public interface MyCollection<E> extends Iterable<E> {

    /* ---------------- 查询操作 -------------- */

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    @Override
    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    /* ---------------- 修改操作 -------------- */

    boolean add(E e);
    boolean remove(Object o);
    boolean containsAll(MyCollection<?> c);
    boolean addAll(MyCollection<? extends E> c);
    boolean removeAll(MyCollection<?> c);

    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        // 接口里面可以调用抽象方法
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    boolean retainAll(MyCollection<?> c);
    void clear();

    /* ---------------- Comparison and hashing -------------- */

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    /* ---------------- 默认方法 -------------- */

    @Override
    default Spliterator<E> spliterator() {
        return null;
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

}
