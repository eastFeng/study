package com.dongfeng.study.sourcecode.java8.util;


import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 将键（Key）映射到值（Value）的对象。Map中不能包含重复的键；每个键最多只能映射到一个值。
 *
 * <p> 注：Map有键和值的概念。
 * 一个键映射到一个值，Map按照键存储和访问值，键不能重复，即一个键只会存储一份，给同一个键重复设值会覆盖原来的值。
 * 使用Map可以方便地处理需要根据键访问对象的场景。
 *
 * <p> 这个接口取代了{@link java.util.Dictionary}类，Dictionary类是一个抽象类，而不是一个接口。
 *
 * <p> Map接口提供了三种集合视图。
 * 允许将Map的内容视为：一组键（Set<K>）、一组值（Collection<V>）或者一组键值映射（Set<Map.Entry<K, V>>）。
 * 映射的顺序定义为映射集合视图上的迭代器返回其元素的顺序。一些map实现，比如TreeMap类，对它们的顺序做了特定的保证；其他的类，比如HashMap类，则不然。
 *
 * <p> 注意：如果使用可变对象作为键（Key），则必须非常小心。
 *
 * <p> 所有通用map实现类都应提供两个“标准”构造函数：
 * 一个无参的构造函数，用于创建空Map；
 * 另一个构造函数具有Map类型的单个参数，用于创建与其参数具有相同键值映射的新Map。
 *
 * <p> 一些Map实现对它们可能包含的键和值有限制。例如，有些实现禁止空（null）键和值，有些实现对其键的类型有限制。
 *
 * @author eastFeng
 * @date 2021-01-18 17:50
 * @param <K> 键（Key）的类型
 * @param <V> 值（Value）的类型
 */
public interface Map<K,V> {
    // K和V为类型变量（类型参数），分别表示键（Key）和值（Value）的类型。

    // Query Operations（查询操作）

    /**
     * <b> 查看Map中键值对的个数 </b>
     * <p> 如果Map中键值对个数超过Integer.MAX_VALUE, 则返回Integer.MAX_VALUE
     *
     * @return 此Map中键值对的个数
     */
    int size();

    /**
     * <b> 是否为空：此Map中键值对的数量是否为0 </b>
     *
     * @return 为空，返回true，否则返回false
     */
    boolean isEmpty();

    /**
     * <b> 查看是否包含指定的键 </b>
     *
     * @param key 键
     * @return 如果当前Map包含指定键key，返回true，否则返回false
     */
    boolean containsKey(Object key);

    /**
     * <b> 查看是否包含某个值 </b>
     *
     * @param value 值
     * @return 如果此Map将一个或多个键映射到指定值，则返回true。否则返回false。
     */
    boolean containsValue(Object value);

    /**
     *
     * <b> 根据键获取值，没找到返回null。
     * <p> 返回指定键映射的值，如果当前Map不包含该键，则返回null。
     *
     * @param key 键
     * @return 根据键获取值，没找到返回null
     */
    V get(Object key);

    // Modification Operations（修改操作）

    /**
     * <b> 保存键值对，如果原来有key，覆盖原来的值，返回原来的值。</b>
     *
     * @param key 键
     * @param value 值
     * @return 如果原来有键key，返回key对应的旧值；如果原来没有key，返回null。
     */
    V put(K key, V value);

    /**
     * <b> 根据键删除键值对，返回key原来的值，如果不存在，返回null </b>
     *
     * @param key 键
     * @return 返回key原来的值，如果当前Map原来不存在key，返回null
     */
    V remove(Object key);

    // Bulk Operations（批量操作）

    /**
     * <b> 保存m中的所有键值对到当前Map </b>
     *
     * @param m Map
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * <b> 清空Map中所有键值对 </b>
     */
    void clear();

    // Views（视图）

    /**
     * <b> 获取Map中键的集合 </b>
     *
     * @return 键的集合
     */
    Set<K> keySet();

    /**
     * <b> 获取Map中所有值的集合 </b>
     *
     * @return 所有值的集合
     */
    Collection<V> values();

    /**
     * <b> 获取Map中所有键值对 </b>
     *
     * @return 所有键值对
     */
    Set<Entry<K, V>> entrySet();

    /**
     * <b> 嵌套接口，表示一个键值对 </b>
     *
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    interface Entry<K,V> {
        /**
         * <b> 返回此键值对的键 </b>
         * @return 键
         */
        K getKey();

        /**
         * <b> 返回此键值对的值 </b>
         * @return 值
         */
        V getValue();

        /**
         * <b> 替换此键值对的值 </b>
         * @param value 新值
         * @return 此键值对的旧值
         */
        V setValue(V value);

        @Override
        boolean equals(Object o);

        @Override
        int hashCode();

        /**
         * 静态方法comparingByKey
         *
         * @param <K> 键的类型，实现了{@link Comparable}接口
         * @param <V> 值的类型
         * @return 键值对依据键自然排序的比较器（{@link Comparator}）
         */
        public static <K extends Comparable<? super K>, V> Comparator<java.util.Map.Entry<K,V>> comparingByKey() {
            return (Comparator<java.util.Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        /**
         * 静态方法comparingByValue
         *
         * @param <K> 键的类型
         * @param <V> 值的类型，实现了{@link Comparable}接口
         * @return 键值对依据值自然排序的比较器（{@link Comparator}）
         */
        public static <K, V extends Comparable<? super V>> Comparator<java.util.Map.Entry<K,V>> comparingByValue() {
            return (Comparator<java.util.Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        /**
         * 静态方法comparingByKey
         *
         * @param cmp 键的比较器（{@link Comparator}）
         * @param <K> 键
         * @param <V> 值
         * @return 键值对依据键特定排序的比较器（Comparator）
         */
        public static <K, V> Comparator<java.util.Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<java.util.Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        /**
         * 静态方法comparingByValue
         *
         * @param cmp 值的比较器（{@link Comparator}）
         * @param <K> 键
         * @param <V> 值
         * @return 键值对依据值特定排序的比较器（Comparator）
         */
        public static <K, V> Comparator<java.util.Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<java.util.Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }
    }

    // Comparison and hashing

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    // Defaultable methods（默认方法）

    /**
     * <b> 返回指定键映射的值，如果此Map不包含指定键，则返回defaultValue。 </b>
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 键对应的值或者默认值
     */
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }

    /**
     * <b> 对该Map中的每个键值对执行给定的操作，直到处理完所有键值对或发生异常为止。 </b>
     *
     * @param action 为每个键值对执行的操作
     */
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * <b> 将每个键值对的值替换为对该键值对调用给定{@link BiFunction}函数的结果，直到处理完所有键值对发生异常为止。 </b>
     *
     * @param function 为每个键值对执行的函数
     */
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        for (Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }

            // ise thrown from function is not a cme.
            v = function.apply(k, v);

            try {
                entry.setValue(v);
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    /**
     * <b>只有在此Map中给定的键不存在或者键对应的值为null时，才保存给定的键值对。</b>
     *
     * @param key 键
     * @param value 值
     * @return 键key对应的旧值
     */
    default V putIfAbsent(K key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }

    /**
     * <b> 只有当该键对应的是给定的值时，才删除该键（键值对）。 </b>
     *
     * @param key 键
     * @param value 值
     * @return 是否删除，true：删除，false：没有删除
     */
    default boolean remove(Object key, Object value) {
        Object curValue = get(key);
        // 键值对实际的值curValue和给定的值value不相等
        if (!Objects.equals(curValue, value) ||
                // 不存在该键
                (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    default boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    default V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }

    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    default V computeIfPresent(K key,
                               BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                remove(key);
                return null;
            }
        } else {
            return null;
        }
    }

    default V compute(K key,
                      BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);

        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            // delete mapping
            if (oldValue != null || containsKey(key)) {
                // something to remove
                remove(key);
                return null;
            } else {
                // nothing to do. Leave things as they were.
                return null;
            }
        } else {
            // add or replace old mapping
            put(key, newValue);
            return newValue;
        }
    }

    default V merge(K key, V value,
                    BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        V oldValue = get(key);
        V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if(newValue == null) {
            remove(key);
        } else {
            put(key, newValue);
        }
        return newValue;
    }
}
