package com.dongfeng.study.basicstudy.sourcecode.java8.lang;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * local : 局部的，本地的。
 *
 * <p>该类提供线程局部/本地变量。这些变量与普通的对应变量不同，ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
 * ThreadLocal中填充的变量属于当前线程，该变量对其他线程而言是隔离的。
 *
 * <p>ThreadLocal实例通常是类中的私有静态字段，它们希望将状态与线程相关联
 *
 * @author eastFeng
 * @date 2020-11-21 23:35
 * @param <T> 局部变量类型
 */
public class ThreadLocal<T> {
    /**
     *
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * 下一个要给出的哈希值，原子更新，从0开始。
     */
    private static final AtomicInteger nextHashCode = new AtomicInteger();

    /**
     * 0x61c88647转为十进制就是：1640531527
     * 连续生成的哈希值的差值
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * 返回下一个哈希值
     */
    private static int nextHashCode(){
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * 返回此线程局部变量的当前线程“初始值”。
     */
    protected T initialValue(){
        return null;
    }

    /**
     * 泛型方法
     * <p>创建线程的局部变量，变量的初始值是通过Supplier的get方法确定的。
     *
     * @param supplier 用于确定线程局部变量初始值的Supplier
     * @param <S> 线程局部变量的类型
     * @return 一个新的ThreadLocalJava8对象
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier){
        return new SuppliedThreadLocal<>(supplier);
    }

    /**构造方法**/
    public ThreadLocal(){
    }

    /**
     * <b>重要方法</b>
     * <p>返回当前线程的线程局部变量的值</p>
     */
    public T get(){
        Thread thread = Thread.currentThread();
        ThreadLocalMap threadLocalMap = getMap(thread);
        if (threadLocalMap!=null){
            ThreadLocalMap.Entry entry = threadLocalMap.getEntry(this);
            if (entry!=null){
                @SuppressWarnings("unchecked")
                T value = (T) entry.value;
                return value;
            }
        }

        return setInitialValue();
    }

    /**
     * set()方法的变量来设置初始值
     * @return 初始值
     */
    private T setInitialValue(){
        //
        T value = initialValue();
        Thread thread = Thread.currentThread();
        ThreadLocalMap map = getMap(thread);
        if (map!=null){
            map.set(this, value);
        }else {
            createMap(thread, value);
        }
        return value;
    }


    /**
     * <b>重要方法</b>
     *
     * <p>设置当前线程的局部变量设置为指定值。</p>
     * <p>大部分子类都不需要重写这个方法。</p>
     * @param value 要存储在当前线程的 thread-local的副本的值。
     */
    public void set(T value){
        Thread thread = Thread.currentThread();
        ThreadLocalMap map = getMap(thread);
        if (map!=null){
            map.set(this, value);
        }else {
            createMap(thread, value);
        }
    }


    /**
     * 创建与ThreadLocal相关联的映射。
     *
     * @param t 当前线程
     * @param firstValue ThreadLocalMap初始Entry元素的value值
     */
    void createMap(Thread t, T firstValue) {
        //t.threadLocals = new ThreadLocalMap(this, firstValue);
        new ThreadLocalMap(this, firstValue);
    }

    /**
     * 获取与当前线程关联的ThreadLocalMap
     * @param t 当前线程
     * @return ThreadLocalMap
     */
    ThreadLocalMap getMap(Thread t){
        //return t.threadLocals;
        return null;
    }

    /**
     *
     * @param parentMap
     * @return
     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }

    /**
     * 在子类InheritableThreadLocal中定义
     */
    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }


    /**
     * ThreadLocal的扩展，从指定的Supplier获取初始值
     * @param <T> 局部变量类型
     */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier){
            this.supplier = Objects.requireNonNull(supplier, "参数supplier不能为null");
        }

        @Override
        protected T initialValue(){
            return supplier.get();
        }
    }

    //-------------------------------------------- ThreadLocalMap ------------------------------------------------------
    /**
     * ThreadLocalMap是定制的哈希映射，仅适用于维护线程本地值。
     * <p> 该类是包私有的，以允许在类Thread中声明。
     * <p> 每个线程都有个ThreadLocal.ThreadLocalMap对象
     *
     * <p> Entry用来存放键值对（键是ThreadLocal对象，值是线程局部变量的值）。
     * <p> ThreadLocalMap中有个Entry数组，用来存放多个键值对。
     */
    static class ThreadLocalMap{
        /**
         * Entry用来保存数据，继承了弱引用。
         * 在Entry内部使用ThreadLocal作为key，使用我们设置的线程局部变量作为value。
         */
        static class Entry extends WeakReference<ThreadLocal<?>>{
            // 与ThreadLocal关联的值
            Object value;

            public Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        // 初始容量，必须是2的倍数
        private static final int INITIAL_CAPACITY = 16;

        // 过期元素: 元素Entry!=null , 但是 Entry.get()==null (也就是Entry中的键ThreadLocal为null)。
        // 该数组的长度必须是2的倍数
        private Entry[] table;

        // 数组table的元素的个数
        private int size;

        // 下一个可以调整大小的值 默认是0
        private int threshold;

        private void setThreshold(int len){
            threshold = len * 2 / 3;
        }

        /**
         * 索引i的后一个索引
         */
        private static int nextIndex(int i, int len){
            return ((i + 1 < len) ? i + 1 : 0);
        }

        /**
         * 索引i的前一个索引
         */
        private static int prevIndex(int i, int len){
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        /**
         * 构造一个最初包含(firstKey, firstValue)的ThreadLocalMap对象，
         * ThreadLocalMap是延迟初始化的，因为只有在至少有一个Entry元素要放入其中时才创建。
         */
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            // INITIAL_CAPACITY 数组初始大小 : 16
            table = new Entry[INITIAL_CAPACITY];
            // 计算数组下标: 和HashMap中计算数组下标一样
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            // 放入数组table中
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }

        private ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (Entry e : parentTable) {
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null) {
                            h = nextIndex(h, len);
                        }
                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        /**
         * <b>获取和key相关联的Entry对象</b>
         * <p>此方法本身只处理快速路径：直接命中现有键。否则它会传递给getEntryAfterMiss。这是为了最大限度地提高直接命中的性能而设计的</p>
         *
         * @param key ThreadLocal
         * @return 与key相关联的Entry, 如果没有则为null
         */
        private Entry getEntry(ThreadLocal<?> key) {
            // 计算数组下标
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key) {
                // 该索引处元素不为null 并且key相同 , 命中
                return e;
            } else {
                return getEntryAfterMiss(key, i, e);
            }
        }

        /**
         * 在getEntry没有直接命中，调用此方法
         *
         * @param key ThreadLocal
         * @param i 数组table的索引
         * @param e table[i]处的Entry
         * @return 与key相关联的Entry, 如果没有则为null
         */
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            // 如果e为null ,直接返回null
            while (e != null) { // 循环遍历查找
                ThreadLocal<?> k = e.get();
                if (k == key) {
                    // 命中
                    return e;
                }
                if (k == null) {
                    // 删除数组中Entry的key为null的元素
                    expungeStaleEntry(i);
                } else {
                    // 下一个索引
                    i = nextIndex(i, len);
                }
                e = tab[i];
            }
            return null;
        }

        /**
         * 设置键值对
         *
         * @param key ThreadLocal
         * @param value 要设置的值
         */
        private void set(ThreadLocal<?> key, Object value){

            // 不像get方法那样使用快速路径，因为使用set方法创建新的Entry对象和替换现有Entry对象一样常见。
            // 在这种情况下，快速路径失败的频率更高。

            Entry[] tab = table;
            int len = tab.length;
            // 计算数组索引
            int i = key.threadLocalHashCode & (len - 1);

            // 找到就更新value
            for (Entry e = tab[i]; e!=null ; e=tab[i=nextIndex(i, len)]){
                // e!=null
                ThreadLocal<?> k = e.get();

                if (k == key){
                    // 找到,更新value
                    e.value = value;
                    return;
                }

                if (k == null){
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            table[i] = new Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold) {
                rehash();
            }
        }

        /**
         * 删除table数组中指定key的Entry对象
         */
        private void remove(ThreadLocal<?> key){
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);

            for (Entry e = tab[i]; e!=null; e=tab[i=nextIndex(i, len)]){
                if (e.get() == key){
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        /**
         *
         * @param key 键
         * @param value 值
         * @param staleSlot table数组的索引，该索引处Entry元素的key为null
         */
        private void replaceStaleEntry(ThreadLocal<?> key, Object value, int staleSlot){
            Entry[] tab = table;
            int len = table.length;
            Entry e;

            // 该索引处的元素需要删除
            int slotToExpunge = staleSlot;
            // 往前找
            for (int i=prevIndex(staleSlot, len); (e=tab[i]) != null; i=prevIndex(i, len)) {
                // e!=null
                if (e.get() == null) {
                    // key为null
                    slotToExpunge = i;
                }
            }

            for (int i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
                // e!=null
                ThreadLocal<?> k = e.get();

                // 如果找到了key，需要将它与过时的Entry交换，以保证哈希表的顺序。
                // 然后可以将新过时的Entry元素或者上面遇到的任何其他过时的Entry元素索引传递给expungeStaleEntry方法，
                // 以删除或者rehash所有运行中的其他Entry元素。
                if (k == key) {
                    // 找到key命中，更新value
                    e.value = value;

                    // 互换位置
                    tab[i] = tab[staleSlot];
                    tab[staleSlot] = e;

                    // 在前一个过时条目（如果存在）处开始删除
                    if (slotToExpunge == staleSlot) {
                        slotToExpunge = i;
                    }
                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    return;
                }

                if (k == null && slotToExpunge == staleSlot) {
                    slotToExpunge = i;
                }
            }

        }

        /**
         * 删除table数组中索引为staleSlot处的元素
         * 删除table数组中过时的Entry元素
         *
         * @param staleSlot table数组的索引，该索引处Entry元素的key为null
         * @return staleSlot之后下一个元素为null的索引
         */
        private int expungeStaleEntry(int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;

            // staleSlot索引处的元素设为null （删除）
            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            // 数组table的元素的个数减一
            size--;

            // 遍历直到遇到null
            Entry e;
            int i;
            // 从索引staleSlot开始往后遍历直到遇到下一个元素为null的索引。并且这俩索引之间所有的元素都会被检查
            for (i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
                // e != null
                ThreadLocal<?> k = e.get();
                if (k == null) {
                    // Entry的key为null就把该索引处Entry设为null （删除）
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) { // 计算的索引不相等, 把元素e放在数组正确的位置
                        // 索引i出置为null
                        tab[i] = null;

                        // 必须扫描到null, 然后把元素e放在数组元素为null的位置，，有可能还会放在原来位置吧，，反正不会删除
                        while (tab[h] != null) {
                            h = nextIndex(h, len);
                        }
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        /**
         * 试探性地扫描一些元素，寻找过时的元素。
         * 当添加新元素或者删除另外一个过时元素时，将调用此方法。
         * @param i 已知不是过期元素的索引，扫描从i之后的元素开始。
         * @param n 扫描控制: 扫描log2(n)个元素，除非找到过时条目，这种情况下log2(table.length)-1 个其他元素会被扫描。
         *          从插入调用时，此参数是元素数，而从replaceStaleEntry方法调用时，此参数是表长度。
         * @return 如果删除了任何过时的元素，则为true
         */
        private boolean cleanSomeSlots(int i, int n){
            boolean remove = false;
            Entry[] tab = table;
            int len = tab.length;

            do {
                i = nextIndex(i, len);
                Entry e = tab[i];
                if (e!=null && e.get()==null){
                    n = len;
                    remove = true;
                    i = expungeStaleEntry(i);
                }
            }while ((n >>>= 1) != 0);

            return remove;
        }

        /**
         * 重新包装 和/或 调整table数组的大小。
         * <p>首先扫描整个数组，删除过时的元素。如果这不能充分缩小表的大小，将表大小增加一倍。</p>
         */
        private void rehash(){
            expungeStaleEntries();

            // 用较低的双倍阈值以避免迟滞。
            if (size >= threshold-threshold/4){
                resize();
            }
        }

        /**
         * 数组容量增加一倍
         */
        private void resize(){
            Entry[] oldTab = table;
            int oldLen = oldTab.length;
            // 扩容后新数组的长度
            int newLen = 2 * oldLen;
            Entry[] newTab = new Entry[newLen];
            int count = 0;

            // 旧数组中的元素复制到新数组中
            for (Entry e : oldTab) {
                if (e != null) {
                    ThreadLocal<?> k = e.get();
                    if (k == null) {
                        // 帮助GC (帮助垃圾回收)
                        e.value = null;
                    } else {
                        // 元素在新数组中的索引 (重新计算索引)
                        int h = k.threadLocalHashCode & (newLen - 1);
                        while (newTab[h] != null) {
                            h = nextIndex(h, newLen);
                        }
                        newTab[h] = e;
                        count++;
                    }
                }
            }

            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        /**
         * 删除table数组中所有过时的元素
         */
        private void expungeStaleEntries(){
            Entry[] tab = table;
            int length = tab.length;
            for (int i=0; i<length; i++){
                Entry entry = tab[i];
                if (entry!=null && entry.get()==null){
                    expungeStaleEntry(i);
                }
            }
        }
    }


}
