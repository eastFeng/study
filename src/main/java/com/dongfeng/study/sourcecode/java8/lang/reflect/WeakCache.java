package com.dongfeng.study.sourcecode.java8.lang.reflect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * <b> JDK提供的弱缓存，是一个二级缓存。 </b>
 *
 * <p><b> JDK动态代理用了这个缓存来存储动态代理类 </b></p>
 *
 * <p> 一级Key是类加载器，二级Key是KeyN </p>
 * <p> 一级缓存的 key 和 value 都是弱引用，二级缓存都是强引用 </p>
 *
 * <p> 一级Key封装位内部类CacheKey，继承了弱引用，如果key被回收，会在get/size等方法中清楚失效的数据 </p>
 * <p> 构造方法传入SubKeyFactory和valueFactory，SubKeyFactory是产生二级Key的接口，map是二级ConcurrentMap</p>
 *
 * <p>
 *     缓存（Cache）键值对(key, sub-key)->value。key（键）和value（值）是弱引用的，但是sub-key（子键、二级键）是强引用的。
 *     key直接传递给 {@link #get(Object, Object)} 方法，该方法也接受一个参数。
 *     用传递给构造函数的 {@link #subKeyFactory} 函数从key和参数中计算出sub-key。
 *     使用传递给构造函数的 {@link #valueFactory} 函数从key和参数中计算出value。
 *     key可以为null并按标识（identity）进行比较，而 {@link #subKeyFactory} 返回的sub-key和 {@link #valueFactory} 返回的value不能为null。
 *     sub-key使用equals方法进行比较。
 *     每次调用{@link #get(Object, Object)} 、{@link #containsValue(Object)} 或 {@link #size()} 方法，
 *     清除对key的WeakReferences（弱引用）时，都会从缓存中延迟删除Entries（条目）。
 *     清楚对单个value的WeakReferences（弱引用）不会导致删除，但这些条目在逻辑上被视为不存在，
 *     并在请求它们的key/subKey时触发对{@link #valueFactory}的重新评估。
 * </p>
 *
 *
 *
 * @author eastFeng
 * @date 2021-02-03 15:08
 * @param <K> key的类型
 * @param <P> 参数的类型
 * @param <V> value的类型
 */
public final class WeakCache<K, P, V> {

    /**
     * 引用队列，存放被回收的 WeakReference。
     */
    private final ReferenceQueue<K> refQueue = new ReferenceQueue<>();

    /**
     * 一、二级缓存容器，一级缓存的key，value是弱引用类型对象，二级缓存的key，value是强引用对象。
     * <p> key的类型是Object，并且支持null key。（为了支持null, map的key类型设置为Object）
     * <p> ConcurrentMap : 线程安全的Map
     */
    private final ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> map = new ConcurrentHashMap<>();

    /**
     * 记录已注册的 Supplier，  为了实现缓存的过期机制
     */
    private final ConcurrentMap<Supplier<V>, Boolean> reverseMap = new ConcurrentHashMap<>();

    /**
     * sub-key的生成器
     */
    private final BiFunction<K, P, ?> subKeyFactory;

    /**
     * value的生成器
     */
    private final BiFunction<K, P, V> valueFactory;

    /**
     * 构造 {@link WeakCache} 实例。
     *
     * @param subKeyFactory 生成sub-key的函数： {@code (key, parameter) -> sub-key}
     * @param valueFactory  生成value的函数： {@code (key, parameter) -> value}
     * @throws NullPointerException 如果参数subKeyFactory或者参数valueFactory为null。
     */
    public WeakCache(BiFunction<K, P, ?> subKeyFactory,
                     BiFunction<K, P, V> valueFactory) {
        this.subKeyFactory = Objects.requireNonNull(subKeyFactory);
        this.valueFactory = Objects.requireNonNull(valueFactory);
    }

    /**
     * 通过缓存查找值（value）。如果在缓存中没有给定(key, subKey)的条目或条目已被清除，则会计算{@link #subKeyFactory}函数，
     * 并可选的计算{@link #valueFactory}函数。
     *
     * <p> WeakCache的get方法并没有用锁进行同步，那它是怎样实现线程安全的呢？ 因为它的所有会进行修改的成员变量都使用了ConcurrentMap，这个类是线程安全的
     *
     * @param key       键（可能为null）
     * @param parameter 参数（不能为null）和key一起用于创建 sub-key和value，
     * @return          缓存的值（从不为null）
     * @throws NullPointerException 如果传入的parameter参数或{@link #subKeyFactory}计算出的sub-key
     *                              或@link #valueFactory}计算出的value为null
     */
    public V get(K key, P parameter) {
        // 校验parameter是否为null
        Objects.requireNonNull(parameter);

        // 删除过时的条目
        expungeStaleEntries();

        // 将key包装成CacheKey, 作为一级缓存的key
        Object cacheKey = CacheKey.valueOf(key, refQueue);

        // 获取二级缓存
        // 为特定的 cacheKey 延迟初始化二级valuesMap
        ConcurrentMap<Object, Supplier<V>> valuesMap = map.get(cacheKey);
        // 如果二级缓存为null
        if (valuesMap == null) {
            // 以CAS方式放入, 如果不存在则放入，否则返回原先的值
            ConcurrentMap<Object, Supplier<V>> oldValuesMap
                    = map.putIfAbsent(cacheKey, valuesMap = new ConcurrentHashMap<>(16));
            if (oldValuesMap != null) {
                // 如果oldValuesMap有值, 说明放入失败
                valuesMap = oldValuesMap;
            }
        }


        // 创建subKey
        Object subKey = Objects.requireNonNull(subKeyFactory.apply(key, parameter));
        // 通过subKey从valuesMap中获取到二级缓存的值
        Supplier<V> supplier = valuesMap.get(subKey);
        Factory factory = null;

        // 循环直到return
        while (true) {
            if (supplier != null) {
                // 根据subKey取出来的supplier不为null
                // supplier可能是Factory或者CacheValue<V>的实例
                V value = supplier.get();
                if (value != null) {
                    return value;
                }
            }

            // 缓存中没有supplier或者返回的supplier为null（可能是清除的CacheValue或者Factory没有成功安装CacheValue）

            // lazily construct a Factory
            // 延时构建一个Factory
            if (factory == null) {
                // 创建一个Factory实例作为subKey的对应值
                factory = new Factory(key, parameter, subKey, valuesMap);
            }

            if (supplier == null) {
                // 到这里表明subKey没有对应的值, 就将factory作为subKey的值放入
                supplier = valuesMap.putIfAbsent(subKey, factory);
                if (supplier == null) {
                    // 到这里表明成功将factory放入缓存
                    supplier = factory;
                }
                // else retry with winning supplier
                // 否则, 可能期间有其他线程修改了值, 那么就不再继续给subKey赋值, 而是取出来直接用
            } else {
                if (valuesMap.replace(subKey, supplier, factory)) {
                    // successfully replaced
                    // cleared CacheEntry / unsuccessful Factory
                    // with our Factory
                    supplier = factory;
                } else {
                    // retry with current supplier
                    supplier = valuesMap.get(subKey);
                }
            }
        }
    }

    /**
     * Checks whether the specified non-null value is already present in this
     * {@code WeakCache}. The check is made using identity comparison regardless
     * of whether value's class overrides {@link Object#equals} or not.
     *
     * @param value the non-null value to check
     * @return true if given {@code value} is already cached
     * @throws NullPointerException if value is null
     */
    public boolean containsValue(V value) {
        Objects.requireNonNull(value);

        expungeStaleEntries();
        return reverseMap.containsKey(new WeakCache.LookupValue<>(value));
    }

    /**
     * Returns the current number of cached entries that
     * can decrease over time when keys/values are GC-ed.
     */
    public int size() {
        expungeStaleEntries();
        return reverseMap.size();
    }

    /**
     * 删除过时的条目
     */
    private void expungeStaleEntries() {
        WeakCache.CacheKey<K> cacheKey;
        while ((cacheKey = (WeakCache.CacheKey<K>)refQueue.poll()) != null) {
            cacheKey.expungeFrom(map, reverseMap);
        }
    }

    /**
     * 工厂类，通过一级缓存的 key 和 parameter 生成最终结果值，同时会把值赋值给二级缓存 WeakReference 弱引用
     */
    private final class Factory implements Supplier<V> {

        private final K key;
        private final P parameter;
        private final Object subKey;
        private final ConcurrentMap<Object, Supplier<V>> valuesMap;

        Factory(K key, P parameter, Object subKey,
                ConcurrentMap<Object, Supplier<V>> valuesMap) {
            this.key = key;
            this.parameter = parameter;
            this.subKey = subKey;
            this.valuesMap = valuesMap;
        }

        @Override
        public synchronized V get() { // serialize access
            // re-check
            Supplier<V> supplier = valuesMap.get(subKey);
            if (supplier != this) {
                // something changed while we were waiting:
                // might be that we were replaced by a CacheValue
                // or were removed because of failure ->
                // return null to signal WeakCache.get() to retry
                // the loop
                return null;
            }
            // else still us (supplier == this)

            // create new value
            V value = null;
            try {
                value = Objects.requireNonNull(valueFactory.apply(key, parameter));
            } finally {
                if (value == null) { // remove us on failure
                    valuesMap.remove(subKey, this);
                }
            }
            // the only path to reach here is with non-null value
            assert value != null;

            // wrap value with CacheValue (WeakReference)
           WeakCache.CacheValue<V> cacheValue = new WeakCache.CacheValue<>(value);

            // put into reverseMap
            reverseMap.put(cacheValue, Boolean.TRUE);

            // try replacing us with CacheValue (this should always succeed)
            if (!valuesMap.replace(subKey, this, cacheValue)) {
                throw new AssertionError("Should not reach here");
            }

            // successfully replaced us with new CacheValue -> return the value
            // wrapped by it
            return value;
        }
    }

    /**
     * 该接口继承自Supplier接口
     *
     * @param <V> value的类型
     */
    private interface Value<V> extends Supplier<V> {}

    /**
     * 实现了{@link WeakCache.Value}接口，并重写了{@link #hashCode()} 和 {@link #equals(Object)}方法。
     */
    private static final class LookupValue<V> implements WeakCache.Value<V> {
        private final V value;

        LookupValue(V value) {
            this.value = value;
        }

        @Override
        public V get() {
            return value;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(value); // compare by identity
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this ||
                    obj instanceof WeakCache.Value &&
                            this.value == ((WeakCache.Value<?>) obj).get();  // compare by identity
        }
    }

    /**
     * 弱引用所指
     * A {@link WeakCache.Value} that weakly references the referent.
     */
    private static final class CacheValue<V>
            extends WeakReference<V> implements WeakCache.Value<V>
    {
        private final int hash;

        CacheValue(V value) {
            super(value);
            this.hash = System.identityHashCode(value); // compare by identity
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            V value;
            return obj == this ||
                    obj instanceof WeakCache.Value &&
                            // cleared CacheValue is only equal to itself
                            (value = get()) != null &&
                            value == ((WeakCache.Value<?>) obj).get(); // compare by identity
        }
    }

    /**
     * 包含弱引用key的CacheKey。它向{@link #refQueue}注册自己，以便在清除 WeakReference 时可以使用它来删除条目。
     */
    private static final class CacheKey<K> extends WeakReference<K> {

        // null键的替换
        private static final Object NULL_KEY = new Object();

        static <K> Object valueOf(K key, ReferenceQueue<K> refQueue) {
            return key == null // null键意味着我们无法弱引用它，所以用NULL_KEY替换
                    ? NULL_KEY
                    //非null键需要使用 WeakReference 进行包装
                    : new CacheKey<>(key, refQueue);
        }

        private final int hash;

        private CacheKey(K key, ReferenceQueue<K> refQueue) {
            super(key, refQueue);
            this.hash = System.identityHashCode(key);  // compare by identity
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            K key;
            return obj == this ||
                    obj != null &&
                            obj.getClass() == this.getClass() &&
                            // 清除的CacheKey只等于它自己
                            (key = this.get()) != null &&
                            // 比较key
                            key == ((CacheKey<K>) obj).get();
        }

        void expungeFrom(ConcurrentMap<?, ? extends ConcurrentMap<?, ?>> map,
                         ConcurrentMap<?, Boolean> reverseMap) {
            // 在这里，只按键删除总是安全的，因为在清除CacheKey并将其排队候，它只等于自身
            ConcurrentMap<?, ?> valuesMap = map.remove(this);
            // 如果需要，也从reverseMap中删除
            if (valuesMap != null) {
                for (Object cacheValue : valuesMap.values()) {
                    reverseMap.remove(cacheValue);
                }
            }
        }
    }
}
