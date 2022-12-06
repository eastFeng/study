package com.dongfeng.study.basicstudy.sourcecode.java8.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * @author eastFeng
 * @date 2021-04-26 1:52
 */
public interface SortedMap<K,V> extends Map<K,V>{

    Comparator<? super K> comparator();

    SortedMap<K,V> subMap(K fromKeyInclude, K toKeyExclude);

    SortedMap<K,V> headMap(K toKey);

    SortedMap<K,V> tailMap(K fromKey);

    K firstKey();

    K lastKey();

    @Override
    Set<K> keySet();

    @Override
    Collection<V> values();

    @Override
    Set<Map.Entry<K, V>> entrySet();
}
