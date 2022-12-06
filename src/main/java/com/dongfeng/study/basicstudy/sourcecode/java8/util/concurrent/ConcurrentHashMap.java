package com.dongfeng.study.basicstudy.sourcecode.java8.util.concurrent;

import com.dongfeng.study.basicstudy.sourcecode.java8.util.AbstractMap;
import com.dongfeng.study.basicstudy.sourcecode.java8.util.Map;

import java.io.Serializable;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author eastFeng
 * @date 2021-04-29 0:14
 */
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
        implements ConcurrentMap<K,V>, Serializable {


    /* ---------------- 静态常量 -------------- */

    /**
     * 数组最大容量
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * 数组默认容量
     */
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    /**
     * 默认负载因子 -- 在构造函数中未指定是用到的负载因子
     */
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    private static int RESIZE_STAMP_BITS = 16;
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;

    static final int MOVED     = -1;
    static final int TREEBIN   = -2;
    static final int RESERVED  = -3;
    static final int HASH_BITS = 0x7fffffff;


    /** Number of CPUS, to place bounds on some sizings */
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public V replace(K key, V value){
        return null;
    }
}
