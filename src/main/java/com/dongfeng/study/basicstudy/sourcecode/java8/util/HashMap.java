package com.dongfeng.study.basicstudy.sourcecode.java8.util;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * <b> java8 hashMap数据结构 : 数组+单向链表+红黑树。
 * 数组是 HashMap 的主体，链表和数组则是主要为了解决哈希冲突而存在的。 </b>
 *
 * <p> HashMap 是Map接口的一个实现，基于哈希表将键映射到值。
 *
 * <p> HashMap 根据键的HashCode值存储数据，具有很快的访问速度，最多允许一条记录的键为null，不支持线程同步。
 *
 * <p> HashMap 是无序的，即不会记录插入的顺序，键值对也不会根据键的大小进行排序。
 *
 * <p> HashMap 继承了{@link AbstractMap}, 实现了{@link java.util.Map}{@link Cloneable}, {@link Serializable}接口。
 *
 * <p> 面试常见问题：底层的数据结构，红黑树的进化条件与链表的退化条件，以及最后会问下为什么线程不安全，会造成什么问题，原因是什么？
 * <p> 红黑树进化条件：链表长度大于等于8并且数组长度大于等于64。 链表退化条件：红黑树节点个数小于等于6。 </p>
 * <p> HashMap的线程不安全体现在会造成死循环、数据丢失、数据覆盖这些问题。其中死循环和数据丢失是在JDK1.7中出现的问题，在JDK1.8中已经解决。
 * 然而在JDK1.8中仍然有数据覆盖这样的问题。数据覆盖问题发生在{@link #putVal(int, Object, Object, boolean, boolean)}方法中。
 *
 *
 * @author eastFeng
 * @date 2020-10-26 17:01
 * @param <K> 键（Key）的类型
 * @param <V> 值（Value）的类型
 */
public class HashMap<K, V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {

    /* ---------------- 静态常量 -------------- */

    private static final long serialVersionUID = -1478322580246286757L;

    /**
     * table数组默认的初始化容量
     *
     * <p> 数组容量（数组长度）必须是2的次幂：2^n，并且数组长度大于等于16。
     * <p> <<(左移运算符): 左移一位相当于该数乘以2
     * <p> 1<<4: 16
     * <p> 幂(power)是指乘方运算的结果。n^m指该式意义为m个n相乘。把n^m看作乘方的结果，叫做n的m次幂，也叫n的m次方。
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * table数组最大容量
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认负载因子 -- 在构造函数中未指定是用到的负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 链表转换红黑树时节点数量阈值
     *
     * <p> 链表节点数量大于等于8时会触发链表转红黑树
     * <p> threshold: 阈值
     */
    static final int TREEITY_THRESHOLD = 8;

    /**
     * 红黑树转换为链表时节点数量阈值
     * <p> 红黑树结构退化为链表结构时节点数量阈值：红黑树节点数量小于等于6时退化为链表
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 链表转换成红黑树时第二个条件 table数组的最小长度：数组长度大于等于64时才能转为红黑树。
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /* ---------------- 静态内部类Node -------------- */
    /**
     * 基本的hash节点（链表节点）
     * <p> Map.Entry：表示一个键值对   ----> Node表示一个键值对
     * <p> 如果类,方法,变量没有使用任何访问修饰符 对应的访问修饰符就是default,只有包内的任何类可以访问(包内可见)
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    static class Node<K,V> implements Entry<K,V> {
        // 键的哈希值，直接存储哈希值是为了在比较的时候加快计算。
        final int hash;
        // 键
        final K key;
        // 值
        V value;
        // 下一个节点
        Node<K,V> next;

        // 如果类,方法,变量没有使用任何访问修饰符 对应的访问修饰符就是default,只有包内的任何类可以访问(包内可见)
        Node(int hash, K key, V value, Node<K,V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * final方法是不允许被重写的
         */
        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final String toString(){
            return key + "=" + value;
        }

        @Override
        public final int hashCode(){
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object o){
            if (o == this){
                return true;
            }
            if (o == null){
                return false;
            }

            if (o instanceof Map.Entry){
                Entry<?,?> e = (Entry<?, ?>) o;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

    // 如果类,方法,变量没有使用任何访问修饰符 对应的访问修饰符就是default,只有包内的任何类可以访问(包内可见)
    /**
     * 【重要】
     * <p><b>计算键的hash值: 不直接只用键的hashCode方法产生的hash值</b>
     *
     * <p><b>计算索引会用到键的hash值</b>: (n-1)&hash , n是table数组的长度，大部分n都是小于65535（2的16次方）的，也就是n的高16位全是0。
     * 与0进行按位与计算必为0，因此(n-1)&hash的运算结果只取决于hash的低16位，在这种情况下，hash的高16位就没有任何作用，
     * 并且由于计算结果只取决于hash的低16位，hash冲突的概率也会增加。因此，该方法中将高位也参与计算，目的是为了降低hash冲突的概率。
     *
     * <p> hashCode方法产生的hash值是int类型的, 是32位, 前16位是高位, 后16位是低位
     * 让高16位数据和低16位数据进行异或运算, 即hashCode^(hashCode>>>16),
     * 让高位数据与低位数据进行异或, 以此加大低位信息的随机性 变相的让高位数据参与其中
     *
     * <p> 异或(exclusive OR, 缩写xor): 逻辑与算符, 两个值不相同,则异或结果为1。如果两个值相同,异或结果为0【同为0 异为1】
     * <p> ^ : 异或运算符
     * <p> >>> : 无符号右移运算符
     * <p> & : 按位与运算符
     *
     * @param key 键
     * @return 哈希值
     */
    static int hash(Object key){
        int h;
        // hashMap允许null键和null值 null键的hash值为0
        // 1. 先拿到key的hashCode值  2. 将hashCode的高16位参与运算
        return (key==null) ? 0 : (h=key.hashCode())^(h>>>16);
    }

    /**
     * <b> 查找对象x实现的Comparable接口 </b>
     *
     * <p> 如果直接或者间接实现了Comparable接口，返回x的类对象，否则返回null
     */
    static Class<?> comparableClassFor(Object x){
        // 判断是否实现了Comparable接口
        if (x instanceof Comparable){
            // Class对象
            Class<?> c = x.getClass();
            Type[] ts, as;
            Type t;
            ParameterizedType p;
            // 首先判断是不是String类，String也实现了Comparable接口
            if (c == String.class){
                // 是String类，直接返回
                return c;
            }

            // 获取对象x表示的类或接口直接实现的接口的Type。
            ts = c.getGenericInterfaces();
            if (ts!=null){
                for (int i=0; i<ts.length; ++i){
                    t=ts[i];
                    if ((t instanceof ParameterizedType)
                            && ((p=(ParameterizedType)t).getRawType() == Comparable.class)
                            && (as=p.getActualTypeArguments())!=null
                            && as.length==1
                            && as[0]==c) {
                        return c;
                    }
                }
            }
        }
        // 没有实现Comparable接口
        return null;
    }

    /**
     * kc有可能为null，返回比较值
     */
    static int compareComparables(Class<?> kc, Object k, Object x){
        return x==null || x.getClass()!=kc ? 0 : ((Comparable)k).compareTo(x);
    }

    /**
     * 返回给定目标容量的 2的幂
     *
     * 或运算符(|): 位运算符, 两个只要有一个是1 结果都是1. 都是0结果才是0
     * >>>: 无符号右移运算符
     */
    static final int tableSizeFor(int cap){
        int n = cap - 1;
        n |= n>>>1;
        n |= n>>>2;
        n |= n>>>4;
        n |= n>>>8;
        n |= n>>>16;
        return n<0 ? 1 : (n>=MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : n+1);
    }

    /* ---------------- 实例变量 -------------- */
    // 将不需要序列化的属性前添加关键字transient,序列化对象的时候,这个属性就不会被序列化。
    /**
     * table是一个Node类型的数组：其中数组中的每一个元素指向一个单向链表或者一个红黑树。
     * <p> 链表或红黑树中的每一个节点表示一个键值对。
     */
    transient Node<K,V>[] table;

    /**
     * 存放所有键值对的集合
     */
    transient Set<Entry<K,V>> entrySet;

    /**
     * 此Map中实际键值对的数量
     */
    transient int size;

    /**
     * 内部的修改次数，方便在迭代中检测结构性修改变化
     */
    transient int modCount;

    /**
     * threshold表示阈值：capacity*loadFactory
     *
     * <p> 当添加键值对后，table就不是空数组了，它会随着键值对的添加进行扩展，扩展的策略类似于ArrayList。
     * 添加第一个元素时，默认分配数组的大小为16，不过，并不是size大于16时再进行扩展，下次什么时候扩展与threshold有关。
     * 当键值对个数size大于threshold时进行扩展。<b>threshold等于table.length乘以loadFactor。</b>
     * 比如，如果table. length为16, loadFactor为0.75，则threshold为12。
     */
    int threshold;

    /**
     * 负载因子：表示整体上table数组被占用的程度，是一个浮点数，默认为0.75，可以通过构造方法进行修改。
     *
     * <p> loadFactor 是控制数组存放数据的疏密程度，
     * loadFactor 越趋近于1（threshold就越趋近于数组长度），那么 数组中存放的数据(entry)也就越多，也就越密，也就是会让链表的长度增加，
     * loadFactor 越小，也就是趋近于 0，数组中存放的数据(entry)也就越少，也就越稀疏。</p>
     */
    final float loadFactor;


    /* ---------------- 构造方法 -------------- */

    // 所有的构造方法都会初始化负载因子loadFactory，只有空参的构造方法不会初始化阈值threshold（Java默认赋初始值为0）。
    // 但是在第一次调用put方法的时候也会重新给阈值赋值，并且会初始化table数组
    /**
     * 构造一个具有指定初始容量和负载因子的空的HashMap
     *
     * @param initialCapacity 初始容量
     * @param loadFactor 负载因子
     * @throws IllegalArgumentException 如果initialCapacity为负数或者loadFactory是负的
     */
    public HashMap(int initialCapacity, float loadFactor){
        if (initialCapacity<0){
            throw new IllegalArgumentException("Illegal initial capacity: "+initialCapacity);
        }
        if (initialCapacity>MAXIMUM_CAPACITY){
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor<=0 || Float.isNaN(loadFactor)){
            throw new IllegalArgumentException("Illegal load factor: "+loadFactor);
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * 使用指定的初始容量和默认的负载因子（0.75）构造一个空的HashMap
     *
     * @param initialCapacity 初始容量
     */
    public HashMap(int initialCapacity){
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 使用默认初始容量（16）和默认负载因子（0.75）构造一个空的HashMap
     */
    public HashMap(){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public HashMap(Map<? extends K, ? extends V> map){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(map, false);
    }


    /**
     * 实现了 Map.putAll 和 Map构造器
     *
     * @param m the map
     * @param evict
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict){
        if (m == null){
            throw new NullPointerException("putMapEntries m is null");
        }

        int size = m.size();
        if (size > 0){
            // 判断table是否已经初始化
            if (table == null){
                // table未初始化，size为m中实际的元素个数
                float ft = ((float)size / loadFactor) + 0.1F;
                int t = (ft<(float)MAXIMUM_CAPACITY) ? (int)ft : MAXIMUM_CAPACITY;
                if (t > threshold){
                    // 计算得到的t大于阈值，则初始化阈值
                    threshold = tableSizeFor(t);
                }
            }else if (size > threshold){
                // table数组已经初始化，并且键值对数量大于阈值，要扩容
                resize();
            }
            // m中的所有元素复制一份到此HashMap中
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                // 复制一份 put进去
                K key = entry.getKey();
                V value = entry.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    /* ---------------- 公共方法 -------------- */

    /**
     * 键值对数量
     */
    @Override
    public int size(){
        return size;
    }

    /**
     * 是否有键值对
     */
    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * <b> 根据键获取值，没找到返回null。
     * <p> 返回指定键映射的值，如果当前Map不包含该键，则返回null。
     *
     * @param key 键
     * @return 存在key, 返回value ,否则返回null
     */
    @Override
    public V get(Object key){
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * 实现了 Map.get
     *
     * <p> 两个key相同满足的两个条件: 1. hash值一样  2. key1==key2或者key1.equals(key2)
     * <p> 比较的时候，先比较hash值，hash相同的时候，再使用equals方法进行比较。
     * 为什么要先比较hash呢？
     * 因为hash是整数，比较的性能一般要比equals高很多，hash不同，就没有必要调用equals方法了，这样整体上可以提高比较性能。
     *
     * <p> 基本步骤：
     * <ol>
     *     <li> 计算key的hash值，根据hash值找到对应数组下标：hash&(length-1) </li>
     *     <li> 判断数组在该位置处的元素是否刚好是我们要找到，如果不是，走第三步 </li>
     *     <li> 判断该元素类型是否是红黑树节点类型TreeNode，如果是用红黑树的方法取数组，如果不是，走第四步</li>
     *     <li> 遍历链表，知道找到相等（==或equals）的key </li>
     * </ol>
     *
     * <p> 时间复杂度：
     * <ol>
     *     <li> 在理想状态下，及未发生任何hash碰撞，数组中的每一个链表都只有一个节点，
     *     那么get方法可以通过hash直接定位到目标元素在数组中的位置，时间复杂度为O(1)。
     *     <li> 若发生hash碰撞，则可能需要进行遍历寻找，N个元素的情况下，链表时间复杂度为O(N)、红黑树为O(logN)。
     * </ol>
     *
     * @param hash 键的hash值
     * @param key 键
     * @return node 或者 null
     */
    final Node<K,V> getNode(int hash, Object key){
        // table数组
        Node<K,V>[] tab = this.table;
        Node<K,V> first, e;
        // 数组长度
        int n;
        K k;

        // 【重要】(n-1)&hash : 计算索引
        // (n-1)&hash等价于hash对n取余,也就是hash对数组长度取余,由于取余的计算效率没有位运算高,所以这也是一个小优化
        // 原理: hash%2ⁿ = hash&(2ⁿ -1),, 所以数组长度length需要是2的n次方

        // 对table数组进行校验：数组table不为null，数组长度大于0，数组在根据hash计算的索引处的元素不为null
        if (tab!=null && (n=tab.length)>0 && (first= tab[(n-1)&hash])!=null){
            // 永远检查第一个节点
            if (first.hash == hash
                    && ((k=first.key)==key || (key!=null&&key.equals(k)))){
                // 是第一个。 这种情况下时间复杂度是O(1)
                return first;
            }

            // 第一个节点后面是否还有节点
            if ((e=first.next)!=null){
                if (first instanceof TreeNode){
                    // 数组该位置红黑树结构，调用红黑树查找目标节点方法getTreeNode
                    // 红黑树时间复杂度为O(logN)。
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                }else {
                    // 数组该位置是链表结构, 则向下循环链表, 进行查找
                    // 链表时间复杂度为O(N)。
                    do {
                        if (e.hash == hash
                                && ((k=e.key)==key || (key!=null && key.equals(k)))){
                            //找到
                            return e;
                        }
                    }while ((e=e.next) != null);
                }
            }
        }

        // 找不到符合的返回null
        return null;
    }


    /**
     * 是否存在key
     */
    @Override
    public boolean containsKey(Object key){
        return getNode(hash(key), key) != null;
    }

    /**
     * <b> 保存键值对，如果原来有key，覆盖原来的值，返回原来的值。</b>
     *
     * <p> 将指定的键值对进行关联, 如果已经包含键的映射 则替换旧值
     *
     * @param key 键
     * @param value 值
     * @return 该键的旧值, 或者null
     */
    @Override
    public V put(K key, V value){
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * <b> 保存键值对，如果原来有key，覆盖并返回旧值。 </b>
     *
     * <p> 没有这个key 新增, 已经有这个key 更新value
     *
     * <p> 两个key相同满足的两个条件: 1. hash值一样  2. key1==key2或者key1.equals(key2)
     * <p> 比较的时候，先比较hash值，hash相同的时候，再使用equals方法进行比较。
     * 为什么要先比较hash呢？
     * 因为hash是整数，比较的性能一般要比equals高很多，hash不同，就没有必要调用equals方法了，这样整体上可以提高比较性能。
     *
     * <p> put方法基本上有以下几个步骤：
     * <ol>
     *     <li> 计算键的哈希值 </li>
     *     <li> 根据哈希值对数组取余计算索引（位置） </li>
     *     <li> 如果定位到的数组位置没有元素，则直接插入</li>
     *     <li> 定位到的数组位置有元素要和插入的key比较，如果相同就直接覆盖旧值。否则，单向链表或红黑树中插入新节点或者更新已有节点的值 </li>
     *     <li> 如果size>threshold（阈值），对table数组进行扩容 </li>
     * </ol>
     *
     * <p> table数组中某一位置的链表转换为红黑树的两个条件：
     * <ol>
     *     <li> 链表长度大于等于8 </li>
     *     <li> table数组长度大于等于64 </li>
     * </ol>
     *
     * @param hash 键的hash值
     * @param key 键
     * @param value 值
     * @param onlyIfAbsent 如果是true, 不改变原有值
     * @param evict 如果false, 则表处于创建模式
     * @return 键对应的旧值, 如果此Map中原来没有键key则返回null
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict){
        Node<K,V>[] tab;
        // 索引处的第一个节点
        Node<K,V> p;
        int n, i; // n:数组长度  i:数组索引
        if ((tab=table)==null || (n=tab.length)==0){
            // 数组为空，创建数组
            n = (tab = resize()).length;
        }

        // 【重要】(n-1)&hash : 计算索引
        // (n-1)&hash等价于hash对n取余,也就是hash对数组长度取余,由于取余的计算效率没有位运算高,所以这也是一个小优化
        // 原理: hash%2ⁿ = hash&(2ⁿ -1),, 由数组长度length需要是2的n次方

        // 判断是否有hash碰撞
        if ((p = tab[i=(n-1)&hash]) == null){  // 没有hash碰撞直接插入元素
            // 数组该索引（位置）处没有元素, 则put进来的键值对组装成Node放入该索引处, key对应的旧值就是null
            tab[i] = newNode(hash, key, value, null);
            /*
             * 判断是否有hash碰撞，这一步骤会出现数据覆盖的情况。（线程不安全）
             * 假设两个线程A、B都在进行put操作，并且通过hash计算出来的索引是相同的，当线程A执行完判断后由于时间片耗尽被挂起，而线程B得到时间片后
             * 在该索引处插入了元素，完成了正常的put操作，然后线程A获得时间片，由于之前进行了判断，所以不再重新进行判断，
             * 而是直接插入，这导致了线程B插入的数据被线程A覆盖，从而线程不安全。
             */
        }else {
            // 数组在该索引处已经有元素（链表或者红黑树）发生了hash碰撞，两种情况：
            // 1.如果链表(或者红黑树)中已经有key键, 找出这个节点(Node)。
            // 2.如果没有这个key键, 将put进来的(key-value)组装成节点并链接在链表(或者红黑树)后面。

            // e: key命中的Node或者put进来的键值对组成的Node
            Node<K,V> e;
            K k;

            // 首先判断该位置的第一个节点和我们要插入的数据，key是不是“相等”
            if (p.hash==hash
                    && ((k=p.key)==key || (key!=null&&key.equals(k)))){
                // map中已经有键key,并且节点p的键就是put进来的key ----> p命中key
                e = p;
            }else if (p instanceof TreeNode){
                // p没有命中key(p的键不是要put进来的key),并且p是红黑树的节点(已由链表结构转换为红黑树结构)
                // 则put进来的键值对放入该树, 返回的Node赋值给e
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            }else {
                // 到这里，说明数组该位置上是一个链表
                // 要么这个链表上命中key, 要么put进来的键值对组装成节点连接在链表的最后一个节点后面
                for (int binCount=0; ; ++binCount){
                    // 判断是否到达链表尾部（判断p是否是链表的尾节点）
                    if ((e=p.next) == null){
                        // 在链表尾部插入新节点，e==null
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEITY_THRESHOLD-1){
                            // 该链表长度过长(大于等于8): 数组扩容或者结构由链表转换为红黑树
                            // 数组长度大于等于64才会转换为红黑树
                            treeifyBin(tab, hash);
                        }
                        break;
                    }

                    if (e.hash==hash
                            &&((k=e.key)==key || (key!=null&&key.equals(k)))){
                        // 节点e=p.next 命中key，e!=null
                        break;
                    }

                    // p=p.next
                    p = e;
                }
            }

            // 变量e引用的就是put进来的key的Node
            // 在上面的代码中，如果是插入新节点：e==null，如果是更新旧值e!=null
            if (e != null){
                // 更新并返回旧值
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue==null){
                    e.value = value;
                }
                afterNodeAccess(e);
                return oldValue;
            }
        }
        // 是插入新节点
        // modCount和size加1，这个地方也不是线程安全的
        ++modCount;
        if (++size > threshold){
            // size大于阈值，数组扩容
            resize();
        }
        afterNodeInsertion(evict);
        return null;
    }

    /**
     * <b> 初始化table数组或者对table数组进行2倍扩容 </b>
     *
     * <p> 初始化数组或数组扩容，每次扩容后，容量为原来的2倍，并进行数据迁移。
     *
     * @return 扩容后的新数组
     */
    final Node<K,V>[] resize(){
        Node<K,V>[] oldTable = table;
        // 旧容量: 数组大小
        int oldCapacity = oldTable==null ? 0 :oldTable.length;
        // 旧阈值
        int oldThreshold = threshold;
        // 新的容量, 新的阈值
        int newCapacity, newThreshold = 0;

        // 判断老的数组是否为空
        if (oldCapacity > 0){
            // 判断老的容量是否超过最大值
            if (oldCapacity >= MAXIMUM_CAPACITY){
                // 【超过最大值就不在扩容了】，老的数组长度不能在扩容了: 阈值设为最大值 返回老的数组
                threshold = Integer.MAX_VALUE;
                return oldTable;
            }else if((newCapacity = oldCapacity<<1)<MAXIMUM_CAPACITY
                    && oldCapacity>=DEFAULT_INITIAL_CAPACITY){
                // 【没有超过最大值，就扩容为原来的两倍】：newCapacity = oldCapacity<<1
                // 如果老的阈值大于等于默认阈值并且新的容量小于最大容量: 新的阈值变为原来阈值的两倍
                newThreshold = oldThreshold << 1;
            }
        }else if (oldThreshold > 0){
            // 老的数组为空，但是老的阈值大于0: 初始容量设为阈值
            newCapacity = oldThreshold;
        }else {
            // 老的容量和老的阈值都为0
            // 新的容量和阈值设为默认值
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int )(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        // 计算新的阈值
        if (newThreshold == 0){
            float ft = (float) (newCapacity * loadFactor);
            newThreshold = (newCapacity<MAXIMUM_CAPACITY && ft<(float)MAXIMUM_CAPACITY) ? (int)ft : Integer.MAX_VALUE;
        }

        // 阈值
        threshold = newThreshold;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K, V>[] newTable = (Node<K,V>[])new Node[newCapacity];
        // table指向新数组
        table = newTable;

        // 扩容之后，将原来数组中的元素迁移到新的更大的数组中，重新计算索引
        if (oldTable != null){
            // 遍历原数组，进行数据迁移
            for (int j=0; j<oldCapacity; ++j){
                Node<K,V> e = oldTable[j];
                if (e != null){
                    oldTable[j] = null;
                    // 如果数组该位置上只有单个元素，那就简单了，简单迁移这个元素就可以了
                    if (e.next == null){
                        // 只有单个元素，只用迁移这个元素就行，重新计算这个元素在新数组的索引并迁移过去
                        newTable[e.hash & (newCapacity-1)] = e;
                    }else if (e instanceof TreeNode){
                        // 不只有单个元素，而且是红黑树结构
                        ((TreeNode<K,V>)e).split(this, newTable, j, oldCapacity);
                    }else {
                        // 不只有单个元素，并且是链表结构
                        /*
                         * 正常情况下，计算节点在table数组中下标的方法是：hash&(oldTable.length-1)。
                         * 数组扩容之后，计算新下标的就是：hash&(newTable.length-1)，也就是hash&(oldTable.length*2-1)。
                         * 于是我们有了这样的结论：【这新旧两次计算下标的结果，要不然相同，要不然新下标=旧下标+旧数组长度】。
                         *
                         * 例如：假设table原长度是16，扩容后长度32，那么一个hash值在扩容前后的table下标是这么计算的：
                         * hash&15 和 hash&31，15=1111（二进制），31=11111（二进制）。
                         * 最后4位显然是相同的，【唯一可能出现的区别就在第5位，如果hash的第5位是0，那么新旧结果就相同，
                         * 反之如果第5位是1，则新结果就比旧table的结果多了10000（二进制），而这个二进制10000就是旧table的长度16。
                         * 所以，新下标是等于原下标还是等于原小标加上原数组长度，就看hash中影响结果中的那一位是0还是1】
                         *
                         * 所以，下面do-while循环中外层if中的
                         * e.hash&oldCapacity就是计算每一个节点中键hash影响新下标结果的那一位到底是0还是1。
                         * （为啥一条链上的每一个节点的键的hash都要计算，一条链上所有的hash不是相等的吗？
                         * 答：一条链表上的hash值不一定是相等的，而是hash&(tableLength-1)（取余）之后相等。）
                         * 因为oldCapacity是大于等于16的偶数，所以其二进制形式为：10000...，hash和它相与，结果为0，
                         * 那响新下标结果的那一位肯定是0，否则是1。
                         * 所以，如果e.hash&oldCapacity结果是0，则链表e新散列下标就等于原散列下标，
                         * 否则链表e新散列坐标要在原散列坐标加上原table数组长度。
                         *
                         * 所以，原数组中同一个链表上的节点，在新数组中的下标分为两种情况，一种是等于原数组下标，一种是等于原数组下标+原素组长度。
                         * 因此，原数组中同一个链表迁移到新数组中有两个链表。
                         */
                        // 将此链表拆成连个链表，放到新数组中，并且保留原来的先后顺序
                        // loHead和loTail对应一条链表：loHead是下标不变情况下的表头，loTail是表尾
                        Node<K,V> loHead = null, loTail = null;
                        // hiHead和hiTail对应另一条链表：hiHead是下标改变情况下的表头，hiTail是表位
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash&oldCapacity) == 0){
                                // 该节点e在新数组中下标不变（等于原数组中的下标）
                                if (loTail == null){
                                    // 表尾如果为null，则说明表头也为null
                                    loHead = e;
                                }else {
                                    // 放在表尾后面
                                    loTail.next = e;
                                }
                                // 更新表尾
                                loTail = e;
                            }else {
                                // 该节点e在新数组中下标 = 原数组下标+原数组长度
                                if (hiTail == null){
                                    hiHead = e;
                                }else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        }while ((e=next) != null);

                        if (loTail != null){
                            loTail.next = null;
                            // 第一条链表：下标等于原数组下标
                            newTable[j] = loHead;
                        }
                        if (hiTail != null){
                            hiTail.next = null;
                            // 第二条链表：下标等于原数组下标加上原数组长度
                            newTable[j + oldCapacity] = hiHead;
                        }
                    }
                }
            }
        }
        return oldTable;
    }

    /**
     * <b> table数组中 该hash值确定的索引 的链表 转为红黑树结构 </b>
     * <p> 数组长度
     *
     * @param tab 链表(红黑树)节点数组
     * @param hash 键的哈希值
     */
    final void treeifyBin(Node<K,V>[] tab, int hash){
        // n: 数组大小, index: 数组索引
        int n, index;
        // 索引处链表的表头(索引处节点)
        Node<K,V> e;
        if (tab==null || (n=tab.length)<MIN_TREEIFY_CAPACITY){
            // 数组为null或者数组长度小于64, 数组扩容
            resize();
        }else if ((e = tab[index=(n-1)&hash]) != null){
            // 数组长度大于等于64，链表转换为红黑树
            TreeNode<K,V> hd = null, tl = null;
            do {
                // e是链表表头
                // 将节点e所在的链表转换为红黑树节点
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null){
                    hd = p;
                }else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            }while ((e=e.next)!=null);

            if ((tab[index] = hd) != null){
                hd.treeify(tab);
            }
        }
    }

    /**
     * 将指定map中的所有键值对复制到此map
     * 这些键值对将替换此map 已有键的值
     *
     * @param m 要将键值对存储到此map的map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m){
        putMapEntries(m, true);
    }

    /**
     * 移除键值对
     *
     * @param key 键key
     * @return key对应的旧值
     */
    @Override
    public V remove(Object key){
        Node<K,V> e;
        return (e=removeNode(hash(key), key, null, false, true))==null ? null : e.value;
    }

    /**
     * 实现 Map.remove
     *
     * @param hash key的hash值
     * @param key key
     * @param value matchValue为true时, 要匹配的值. 否则忽略
     * @param matchValue 如果为true, 只有值相等时才删除
     * @param movable 如果为false, 删除时不要移动其他节点
     * @return 节点或者null
     */
    final Node<K,V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable){
        Node<K,V>[] tab;
        // hash计算的索引处第一个节点
        Node<K,V> p;
        int n, index;

        if ((tab=table) != null
                && (n=tab.length)>0
                && (p=tab[index=(n-1)&hash])!=null){
            // node：要删除的节点
            Node<K,V> node = null, e;
            K k;
            V v;
            // 先看第一个节点是不是要删除的节点
            if (p.hash==hash && Objects.equals(key, p.key)){
                node = p;
            }else if ((e=p.next)!=null){  // 第二个节点不为null
                if (p instanceof TreeNode){
                    // 红黑树
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                }else {
                    // 链表
                    do {
                        if (e.hash==hash && Objects.equals(e.key, key)){
                            //找到
                            node = e;
                            break;
                        }
                        //p就是node的上一个节点(p.next=node)
                        p = e;
                    }while ((e=e.next) != null);
                }
            }

//            if (node!=null
//                    && ((!matchValue || (v=node.value)==value) || (value!=null && value.equals(v)))){
//            }
            if (node!=null
                    && (!matchValue || Objects.equals(value, node.value))){
                // node就是要移除的节点
                if (node instanceof TreeNode){
                    // 红黑树
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                }else if (node == p){
                    // 链表，并且要删除的是第一个节点 : 第一个节点变为第一个节点的后续节点
                    // 链表删除: 指向后续一个节点
                    tab[index] = node.next;
                }else {
                    // 链表，并且要删除的不是第一个节点
                    // 链表删除: 指向后续一个节点
                    p.next = node.next;
                }
                ++modCount;
                --size;
                afterNodeRemove(node);
                return node;
            }
        }
        return null;
    }

    /**
     * 移除所有节点  清空数组
     */
    @Override
    public void clear(){
        Node<K,V>[] tab;
        modCount++;
        if ((tab=table)!=null && size>0){
            size = 0;
            // 清空数组中的每个元素：每个元素设为null
            for (int i=0; i<tab.length; ++i){
                tab[i] = null;
            }
        }
    }

    /**
     * 是否有key的值是value  双循环
     *
     * @param value value
     * @return true:有 false: 没有
     */
    @Override
    public boolean containsValue(Object value){
        Node<K,V>[] tab;
        V v;
        if ((tab=table)!=null && size>0){
            for (int i=0; i<tab.length; ++i){
                for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                    if (Objects.equals(value, e.value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return 此Map中所有键（key）的集合
     */
    @Override
    public Set<K> keySet(){
        Set<K> ks = new KeySet();
        return ks;
    }

    final class KeySet extends AbstractSet<K>{
        @Override
        public final int size(){
            return size;
        }

        @Override
        public final void clear(){
            HashMap.this.clear();
        }

        @Override
        public final Iterator<K> iterator(){
            return new KeyIterator();
        }

        @Override
        public final boolean contains(Object key){
            return containsKey(key);
        }

        @Override
        public final boolean remove(Object key){
            Node<K, V> node = removeNode(hash(key), key, null, false, true);
            return node!=null;
        }

        @Override
        public final Spliterator<K> spliterator(){
            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        @Override
        public final void forEach(Consumer<? super K> action){
            Node<K,V>[] tab;
            if (action == null){
                throw new NullPointerException();
            }
            if (size>0 && (tab=table)!=null){
                int mc = modCount;
                for (int i=0; i<tab.length; ++i){
                    for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                        action.accept(e.key);
                    }
                }
                if (modCount != mc){
                    // 遍历过程中不允许修改
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    /**
     * @return 此Map中所有值（value）的集合
     */
    @Override
    public Collection<V> values(){
        Collection<V> vs = new Values();
        return vs;
    }

    final class Values extends AbstractCollection<V>{
        @Override
        public final int size(){
            return size;
        }

        @Override
        public final void clear(){
            HashMap.this.clear();
        }

        @Override
        public final Iterator<V> iterator(){
            return new ValueIterator();
        }

        @Override
        public final boolean contains(Object value){
            return containsValue(value);
        }

        @Override
        public final Spliterator<V> spliterator(){
            return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        @Override
        public final void forEach(Consumer<? super V> action){
            Node<K,V>[] tab;
            if (action == null){
                throw new NullPointerException();
            }
            if (size>0 && (tab=table)!=null){
                int mc = modCount;
                for (int i=0; i<tab.length; ++i){
                    for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                        action.accept(e.value);
                    }
                }
                if (modCount != mc){
                    //遍历过程中不允许修改
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    @Override
    public Set<Entry<K,V>> entrySet(){
        Set<Entry<K,V>> entrySet = new EntrySet();
        return entrySet;
    }


    final class EntrySet extends AbstractSet<Entry<K,V>>{
        @Override
        public final int size(){
            return size;
        }

        @Override
        public final void clear(){
            HashMap.this.clear();
        }

        @Override
        public final Iterator<Entry<K,V>> iterator(){
            return new EntryIterator();
        }

        @Override
        public final boolean contains(Object o){
            if (!(o instanceof Map.Entry)){
                return false;
            }

            Entry<?,?> e = (Entry<?,?>)o;
            Object key = e.getKey();
            Node<K, V> candidate = getNode(hash(key), key);
            return candidate!=null && candidate.equals(e);
        }

        @Override
        public final boolean remove(Object o){
            if (o instanceof Map.Entry){
                Entry<?,?> e = (Entry<?,?>)o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        @Override
        public final Spliterator<Entry<K,V>> spliterator(){
            return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }


        @Override
        public final void forEach(Consumer<? super Entry<K,V>> action){
            Node<K,V>[] tab;
            if (action == null){
                throw new NullPointerException();
            }

            if (size>0 && (tab=table)!=null){
                int mc = modCount;
                for(int i=0; i<tab.length; ++i){
                    for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                        action.accept(e);
                    }
                }
            }
        }
    }

    // 公共方法：java8(JDK8)中的新方法 接口Map中的default方法

    /**
     *存在此key, 返回key对应的value、否则返回defaultValue
     *
     * @param key 键
     * @param defaultValue 如果键不存在 默认值
     * @return 存在此key, 返回key对应的value、否则返回defaultValue
     */
    @Override
    public V getOrDefault(Object key, V defaultValue){
        Node<K,V> e;
        return (e=getNode(hash(key), key))==null ? defaultValue : e.value;
    }

    /**
     * <b>只有在此Map中给定的键不存在或者键对应的值为null时，才保存给定的键值对。</b>
     *
     * @param key 键
     * @param value 值
     * @return value
     */
    @Override
    public V putIfAbsent(K key, V value){
        return putVal(hash(key), key, value, true, true);
    }

    /**
     * 仅当键key对应的值是value时, 才删除该键值对
     *
     * @param key 键
     * @param value 值
     * @return 是否删除了该键值对 true: 删除了  false: 未删除
     */
    @Override
    public boolean remove(Object key, Object value){
        return removeNode(hash(key), key, value, true, true)!=null;
    }

    /**
     * 仅当当前键key映射的值是oldValue时, 才用newValue替换oldValue
     *
     * @param key 键
     * @param oldValue 旧值
     * @param newValue 新值
     * @return true:该值被替换  false:没哟
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue){
        Node<K,V> e; V v;
        if ((e=getNode(hash(key), key))!=null && Objects.equals(e.value, oldValue)){
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    /**
     * key存在时, 用value替换key映射的值
     *
     * @param key 键
     * @param value 值
     * @return key存在并替换成功:返回key对应的旧值  否则返回null
     */
    @Override
    public V replace(K key, V value){
        Node<K,V> e;
        if ((e=getNode(hash(key), key)) != null){
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    /**
     * 如果指定的键key还没有与某个值关联(或映射为null)-->不存在key,或者存在此key但是对应的value是null
     * 则尝试使用给定的Function函数把key当做输入参数计算出value,,
     * 如果计算出来的value不为null,,更新key的值(key在map中已存在)或者put进去key-value键值对(map中不存在此key)。
     *
     * @param key 键
     * @param mappingFunction Function
     * @return 计算之后的值
     */
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction){
        if (mappingFunction == null){
            throw new NullPointerException();
        }
        int hash = hash(key);
        Node<K,V>[] tab;
        Node<K,V> first, old=null;
        int n, i, binCount=0;
        TreeNode<K,V> t = null;

        if (size>threshold || (tab=table)==null || (n=tab.length)==0){
            n = (tab=resize()).length;
        }
        if ((first=tab[i=(n-1)&hash]) != null){
            if (first instanceof TreeNode){
                //红黑树
                old = (t=(TreeNode<K,V>)first).getTreeNode(hash, key);
            }else {
                //链表
                Node<K,V> e = first; K k;
                do {
                    if (e.hash==hash && Objects.equals((k=e.key), key)){
                        old = e;
                        break;
                    }
                    ++binCount;
                }while ((e=e.next) != null);
            }
            V oldValue;
            if (old!=null && (oldValue=old.value)!=null){
                afterNodeAccess(old);
                return oldValue;
            }
        }
        //计算
        V v = mappingFunction.apply(key);
        if (v == null){
            return null;
        }else if (old != null){
            //有键key, 更新值
            old.value = v;
            afterNodeAccess(old);
            return v;
        }else if (t != null){
            //没有键key, 红黑树树结构
            t.putTreeVal(this, tab, hash, key, v);
        }else {
            //没有键key, 链表结构
            tab[i] = newNode(hash, key, v, first);
            if (binCount >= TREEITY_THRESHOLD-1){
                treeifyBin(tab, hash);
            }
        }
        ++modCount;
        ++size;
        afterNodeInsertion(true);
        return v;
    }

    /**
     * 如果给定键key的值存在且不是null, 则尝试使用给定的BiFunction函数重新计算key的值(key-value作为函数的输入)并更新。
     *
     * @param key 键
     * @param remappingFunction BiFunction
     * @return 计算后的新值
     */
    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction){
        if (remappingFunction == null){
            throw new NullPointerException();
        }
        Node<K,V> e; V oldValue;
        int hash = hash(key);
        if ((e=getNode(hash, key))!=null && (oldValue=e.value)!=null){
            //计算
            V v = remappingFunction.apply(key, oldValue);
            if (v != null){
                e.value = v;
                afterNodeAccess(e);
                return v;
            }else {
                 removeNode(hash, key, null, false, true);
            }
        }
        return null;
    }


    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction){
        if (remappingFunction == null){
            throw new NullPointerException();
        }
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n,i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;

        if (size>threshold || (tab=table)==null || (n=tab.length)==0){
            n = (tab=resize()).length;
        }
        if ((first=tab[i=(n-1)&hash]) != null){
            if (first instanceof TreeNode){
                //红黑树
                old = (t=(TreeNode<K,V>)first).getTreeNode(hash, key);
            }else {
                //链表
                Node<K,V> e = first;
                do {
                    if (e.hash==hash && Objects.equals(key, e.key)){
                        //找到
                        old = e;
                        break;
                    }
                    ++binCount;
                }while ((e=e.next) != null);
            }
        }
        //
        V oldValue = (old==null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        if (old != null){
            if (v != null){
                old.value = v;
                afterNodeAccess(old);
            }else {
                removeNode(hash, key, null, false, true);
            }
        }else if (v != null){
            if (t != null){
                //红黑树
                t.putTreeVal(this, tab, hash, key, v);
            }else {
                //链表
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEITY_THRESHOLD-1){
                    treeifyBin(tab, hash);
                }
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return v;
    }


    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction){
        if (value==null || remappingFunction==null){
            throw new NullPointerException();
        }
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n,i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if (size>threshold || (tab=table)==null || (n=tab.length)==0){
            n = (tab=resize()).length;
        }
        if ((first=tab[i=(n-1)&hash]) != null){
            if (first instanceof TreeNode){
                //红黑树结构
                old = (t=(TreeNode<K,V>)first).getTreeNode(hash, key);
            }else {
                //链表结构
                Node<K,V> e = first;
                do {
                    if (e.hash==hash && Objects.equals(key, e.key)){
                        old = e;
                        break;
                    }
                    ++binCount;
                }while ((e=e.next) != null);
            }
        }
        //map存在key
        if (old != null){
            V v;
            if (old.value != null){
                v = remappingFunction.apply(old.value, value);
            }else {
                v = value;
            }
            if (v != null){
                old.value = v;
                afterNodeAccess(old);
            }else {
                removeNode(hash, key, null, false, true);
            }
            return v;
        }
        //不存在key 且value不为null,新增键值对
        if (value != null){
            if (t != null){
                //红黑树结构
                t.putTreeVal(this, tab, hash, key, value);
            }else {
                //链表结构
                tab[i] = newNode(hash, key, value, first);
                if (binCount >= TREEITY_THRESHOLD-1){
                    treeifyBin(tab, hash);
                }
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return value;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action){
        Node<K,V>[] tab;
        if (action == null){
            throw new NullPointerException();
        }
        if (size>0 && (tab=table)!=null){
            int mc = modCount;
            for (int i=0; i<tab.length; ++i){
                for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                    action.accept(e.key, e.value);
                }
            }
            if (modCount != mc){
                //遍历过程中被修改
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function){
        Node<K,V>[] tab;
        if (function == null){
            throw new NullPointerException();
        }
        if (size>0 && (tab=table)!=null){
            int mc = modCount;
            for (int i=0; i<tab.length; ++i){
                for (Node<K,V> e=tab[i]; e!=null; e=e.next){
                    e.value = function.apply(e.key, e.value);
                }
            }
        }
    }

    /*-------------------------------------------------------------------------------------*/
    // cloning and serialization

    /**
     * clone  深拷贝
     */
    @Override
    public Object clone(){
        HashMap<K,V> result;

        try {
            result = (HashMap<K,V>)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    final float loadFactor(){
        return loadFactor;
    }

    final int capacity(){
        return (table!=null) ? table.length : (threshold>0)?threshold:DEFAULT_INITIAL_CAPACITY;
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        int buckets = capacity();
        stream.defaultWriteObject();
        stream.writeInt(buckets);
        stream.writeInt(size);
        internalWriteEntries(stream);
    }

    // iterators

    abstract class HashIterator{
        //下一个要返回的entry
        Node<K,V> next;
        //当前entry
        Node<K,V> current;
        int expectedModCount;
        int index;

        HashIterator(){
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t!=null && size>0){
                do {}while (index<t.length && (next=t[index++])==null);
            }
        }

        public final boolean hasNext(){
            return next!=null;
        }

        final Node<K,V> nextNode(){
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            if (e == null){
                throw new NoSuchElementException();
            }
            if ((next=(current=e).next)==null && (t=table)!=null){
                do {
                }while (index<t.length && (next=t[index++])==null);
            }
            return e;
        }

        public final void remove(){
            Node<K,V> p = current;
            if (p == null){
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }

    final class KeyIterator extends HashIterator implements Iterator<K>{
        @Override
        public final K next(){
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements Iterator<V>{
        @Override
        public V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator implements Iterator<Entry<K,V>>{
        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }



    // spliterators
    static class Hmj8Spliterator<K,V>{
        final HashMap<K,V> map;
        //当前Node
        Node<K,V> current;
        //当前索引, advance/split后修改
        int index;
        //最后一个索引
        int fence;
        //size估计
        int est;
        //comodification check
        int expectedModCount;

        Hmj8Spliterator(HashMap<K,V> map, int origin, int fence, int est, int expectedModCount){
            this.map = map;
            this.index = origin;
            this.fence = fence;
            this.est = est;
            this.expectedModCount  = expectedModCount;
        }

        //第一次使用的时候初始化fence和size
        final int getFence(){
            int hi;
            if ((hi=fence) < 0){
                HashMap<K,V> m = map;
                est = m.size;
                expectedModCount = m.modCount;
                Node<K,V>[] tab = m.table;
                hi = fence = (tab==null) ? 0 : tab.length;
            }
            return hi;
        }

        public final long estimateSize(){
            getFence(); //强制初始化
            return est;
        }

    }

    /**
     * 接口Spliterator: 用于遍历和划分源元素对象。源元素可以是数组,集合,IO通道(IO channel)或者生成器函数。
     *
     * Spliterator中的方法:
     *                   1.tryAdvance: 逐个遍历元素
     *                   2.forEachRemaining: 批量顺序遍历元素
     *                   3.trySplit: 分割元素
     *                   4.characteristics: 结构,源,元素的特征
     *                   5.estimateSize: 剩余元素数量的估计值
     *                   6.
     *
     */
    static final class KeySpliterator<K,V>
            extends Hmj8Spliterator<K,V>
            implements Spliterator<K>{

        KeySpliterator(HashMap<K,V> m, int origin, int fence, int est, int expectedMoCount){
            super(m, origin, fence, est, expectedMoCount);
        }

        /**
         * 分割元素
         */
        @Override
        public KeySpliterator<K,V> trySplit(){
            //mid等于lo与hi相加除以2  >>>:右移一位除以2
            int hi = getFence(), lo = index, mid = (lo+hi)>>>1;
            return (lo>=mid || current!=null) ? null : new KeySpliterator<>(map, lo, index=mid, est>>>=1, expectedModCount);
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action){
            int i, hi, mc;
            if (action == null){
                throw new NullPointerException();
            }

            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi=fence) < 0){
                mc = expectedModCount = m.modCount;
                hi = fence = (tab==null) ? 0 : tab.length;
            }else {
                mc = expectedModCount;
            }

            if (tab!=null
                    && tab.length>=hi
                    && (i=index)>=0
                    && (i<(index=hi) || current!=null)){
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null){
                        p = tab[i++];
                    }else {
                        action.accept(p.key);
                        p = p.next;
                    }
                }while (p!=null || i<hi);
            }

            if (m.modCount != mc){
                throw new ConcurrentModificationException();
            }
        }


        /**
         * 还有剩余元素, 则返回true
         */
        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            int hi;
            if (action == null){
                throw new NullPointerException();
            }

            Node<K,V>[] tab = map.table;
            if (tab!=null
                    && tab.length>=(hi=getFence())
                    && index>=0){
                while (current!=null || index<hi){
                    if (current == null){
                        current = tab[index++];
                    }else {
                        K k = current.key;
                        current = current.next;
                        action.accept(k);
                        if (map.modCount != expectedModCount){
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * 特征
         */
        @Override
        public int characteristics() {
            return (fence<0 || est==map.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }

        /**
         * 如果这个Spliterator的源是通过比较器(Comparator)排序的, 则返回比较器
         * 如果源是通过Comparable自然排序, 则返回null
         * 如果源没有排序, 则抛出IllegalStateException异常
         *
         * @return Comparator或者null
         */
        @Override
        public Comparator<? super K> getComparator() {
            return null;
        }
    }


    static final class ValueSpliterator<K,V>
            extends Hmj8Spliterator<K,V>
            implements Spliterator<V>{

        ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est, int expectedModCount){
            super(m, origin, fence, est, expectedModCount);
        }

        /**
         * 分割元素
         */
        @Override
        public ValueSpliterator<K,V> trySplit(){
            //mid等于lo与hi相加除以2
            int hi = getFence(), lo = index, mid = (lo+hi)>>>1;
            return (lo>=mid || current!=null) ? null : new ValueSpliterator<>(map, lo, index=mid, est>>>=1, expectedModCount);
        }

        @Override
        public void forEachRemaining(Consumer<? super V> action){
            int i, hi, mc;
            if (action == null){
                throw new NullPointerException();
            }

            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi=fence) < 0){
                mc = expectedModCount = m.modCount;
                hi = fence = (tab==null) ? 0 : tab.length;
            }else {
                mc = expectedModCount;
            }

            if (tab!=null
                    && tab.length>=hi
                    && (i=index)>=0
                    && (i<(index=hi) || current!=null)){
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null){
                        p = tab[i++];
                    }else {
                        action.accept(p.value);
                        p = p.next;
                    }
                }while (p!=null || i<hi);
            }

            if (m.modCount != mc){
                throw new ConcurrentModificationException();
            }
        }


        /**
         * 还有剩余元素, 则返回true
         */
        @Override
        public boolean tryAdvance(Consumer<? super V> action) {
            int hi;
            if (action == null){
                throw new NullPointerException();
            }

            Node<K,V>[] tab = map.table;
            if (tab!=null
                    && tab.length>=(hi=getFence())
                    && index>=0){
                while (current!=null || index<hi){
                    if (current == null){
                        current = tab[index++];
                    }else {
                        V v = current.value;
                        current = current.next;
                        action.accept(v);
                        if (map.modCount != expectedModCount){
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * 特征
         */
        @Override
        public int characteristics() {
            return (fence<0 || est==map.size ? Spliterator.SIZED : 0);
        }
    }


    static final class EntrySpliterator<K,V>
            extends Hmj8Spliterator<K,V>
            implements Spliterator<Entry<K,V>>{
        EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est, int expectedModCount){
            super(m, origin, fence, est, expectedModCount);
        }

        /**
         * 分割元素
         */
        @Override
        public EntrySpliterator<K,V> trySplit(){
            //mid等于lo与hi相加除以2  >>>:右移一位除以2
            int hi = getFence(), lo = index, mid = (lo+hi)>>>1;
            return (lo>=mid || current!=null) ? null : new EntrySpliterator<>(map, lo, index=mid, est>>>=1, expectedModCount);
        }

        @Override
        public void forEachRemaining(Consumer<? super Entry<K,V>> action){
            int i, hi, mc;
            if (action == null){
                throw new NullPointerException();
            }

            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi=fence) < 0){
                mc = expectedModCount = m.modCount;
                hi = fence = (tab==null) ? 0 : tab.length;
            }else {
                mc = expectedModCount;
            }

            if (tab!=null
                    && tab.length>=hi
                    && (i=index)>=0
                    && (i<(index=hi) || current!=null)){
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null){
                        p = tab[i++];
                    }else {
                        action.accept(p);
                        p = p.next;
                    }
                }while (p!=null || i<hi);
            }

            if (m.modCount != mc){
                throw new ConcurrentModificationException();
            }
        }


        /**
         * 还有剩余元素, 则返回true
         */
        @Override
        public boolean tryAdvance(Consumer<? super Entry<K,V>> action) {
            int hi;
            if (action == null){
                throw new NullPointerException();
            }

            Node<K,V>[] tab = map.table;
            if (tab!=null
                    && tab.length>=(hi=getFence())
                    && index>=0){
                while (current!=null || index<hi){
                    if (current == null){
                        current = tab[index++];
                    }else {
                        Node<K,V> e = current;
                        current = current.next;
                        action.accept(e);
                        if (map.modCount != expectedModCount){
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * 特征
         */
        @Override
        public int characteristics() {
            return (fence<0 || est==map.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }


    //----------------------------------------------------
    /**
     * 创建一个常规的（非树）节点
     */
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next){
        return new Node<>(hash, key, value, next);
    }

    /**
     * 用于从树节点到普通节点的转换
     */
    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next){
        return new Node<>(p.hash, p.key, p.value, next);
    }

    /**
     * 创建一个树节点
     */
    TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next){
        return new TreeNode<>(hash, key, value, next);
    }

    TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next){
        return new TreeNode<>(p.hash, p.key, p.value, next);
    }

    /**
     * 重置到初始默认状态, 由clone和readObject方法调用
     */
    void reinitialize(){
        table = null;
        entrySet = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    /**
     * 回调
     */
    void afterNodeAccess(Node<K,V> p){}
    void afterNodeInsertion(boolean evict){}
    void afterNodeRemove(Node<K,V> p){}

    /**
     * 仅由writeObject调用
     */
    void internalWriteEntries(java.io.ObjectOutputStream stream) throws IOException{
        Node<K,V>[] tab;
        if (size>0 && (tab=table)!=null){
            for (int i=0; i<tab.length; ++i){
                for (Node<K,V> e= tab[i]; e!=null; e=e.next){
                    stream.writeObject(e.key);
                    stream.writeObject(e.value);
                }
            }
        }
    }

    /**
     * <b> 红黑树节点 </b>
     *
     * <p> 如果类、方法、变量没有使用任何访问修饰符 对应的访问修饰符就是default，只有包内的任何类可以访问(包内可见)
     */
    static final class TreeNode<K,V> extends Node<K,V>{
        /**
         * 父节点
         */
        TreeNode<K,V> parent;
        /**
         * 左子节点
         */
        TreeNode<K,V> left;
        /**
         * 右子节点
         */
        TreeNode<K,V> right;
        /**
         * needed to unlink next upon deletion（删除时需要取消下一个链接）
         */
        TreeNode<K,V> prev;
        /**
         * 是否是红节点
         */
        boolean red;

        /**
         * 构造方法
         */
        TreeNode(int hash, K key, V val, Node<K,V> next){
            super(hash, key, val, next);
        }

        /**
         * 从（调用该方法的）当前节点一直往上找，找到当前节点所在红黑树的根节点。
         *
         * <p>this：表示当前实例 </p>
         */
        final TreeNode<K,V> root(){
            // this：当前对象（实例）。调用该方法的当前节点。
            for (TreeNode<K,V> r=this, p; ;){
                if ((p=r.parent) == null){
                    // 节点r的父节点为null，则为根节点。
                    return r;
                }
                r = p;
            }
        }

        /**
         * 确保给定的根节点是树的第一个节点
         */
        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root){
        }

        /**
         * 查找
         */
        final TreeNode<K,V> find(int hash, Object key, Class<?> kc){
            return null;
        }

        /**
         * 红黑树的查找节点方法
         */
        final TreeNode<K,V> getTreeNode(int hash, Object key){
            // 1. 找到红黑树的根节点   2. 使用根节点调用find方法
            return (parent!=null ? root() : this).find(hash, key, null);
        }

        /**
         * 对a和b进行排序
         * 在没有实现Comparable接口的情况下的终极解决方案
         */
        static int tieBreakOrder(Object a, Object b){
            int d;
            if (a==null || b==null ||
                    // 通过类名进行比较
                    (d=a.getClass().getName().compareTo(b.getClass().getCanonicalName()))==0){
                // 对象a和b同属一个类，则通过对象的哈希值进行比较
                d = System.identityHashCode(a)<=System.identityHashCode(b) ? -1 : 1;
            }
            return d;
        }

        /**
         *
         */
        final void treeify(Node<K,V>[] tab){
            TreeNode<K,V> root = null;
            for (TreeNode<K,V> x = this, next; x != null; x = next) {
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                }
                else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K,V> p = root;;) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h) {
                            dir = -1;
                        } else if (ph < h) {
                            dir = 1;
                        } else if ((kc == null &&
                                // 获取对象k的实现了Comparable接口的Class对象
                                (kc = comparableClassFor(k)) == null) ||
                                // compareComparables方法是在使用过comparableClassFor后才使用的，
                                // 因为第一步已经获取到了比较器
                                (dir = compareComparables(kc, k, pk)) == 0) {
                            // 没有实现Comparable接口，调用tieBreakOrder方法进行排序
                            dir = tieBreakOrder(k, pk);
                        }

                        TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0) {
                                xp.left = x;
                            } else {
                                xp.right = x;
                            }
                            root = balanceInsertion(root, x);
                            break;
                        }
                    }
                }
            }
            moveRootToFront(tab, root);
        }

        final Node<K,V> untreeify(HashMap<K,V> map){
            return null;
        }

        /**
         * 红黑树的put操作
         *
         * @param map HashMap
         * @param tab table数组
         * @param h hash值
         * @param k 键
         * @param v 值
         * @return 红黑树节点
         */
        final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab, int h, K k, V v){
            // 首先要找到红黑树的根节点

            Class<?> kc = null;
            boolean searched = false;

            // this：表示当前实例。（当前节点）
            // 第一步找到根节点：如果调用该方法的当前节点的父节点为null，则当前节点为父节点。否则调用root方法
            TreeNode<K,V> root = (parent != null) ? root() : this;

            for (TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                if ((ph = p.hash) > h) {
                    dir = -1;
                } else if (ph < h) {
                    dir = 1;
                } else if ((pk = p.key) == k || (k != null && k.equals(pk))) {
                    return p;
                } else if ((kc == null &&
                        (kc = comparableClassFor(k)) == null) ||
                        (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null &&
                                (q = ch.find(h, k, kc)) != null) ||
                                ((ch = p.right) != null &&
                                        (q = ch.find(h, k, kc)) != null)) {
                            return q;
                        }
                    }
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K,V> xpn = xp.next;
                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    xp.next = x;
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K,V>)xpn).prev = x;
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }
            }
        }

        final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab, boolean movable){
        }

        /**
         * 分割
         * @param map the map
         * @param table 记录bin头部数据的数据
         * @param index 被分割的表的索引
         * @param bit
         */
        final void split(HashMap<K,V> map, Node<K,V>[] table, int index, int bit){
        }

        //---------------------------------------红黑树的方法, 均改编自CLR------------------------------------------------/
        //左旋
        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root, TreeNode<K,V> p){
            return null;
        }

        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root, TreeNode<K,V> p){
            return null;
        }

        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root, TreeNode<K,V> x){
            return null;
        }

        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root, TreeNode<K,V> x){
            return null;
        }

        static <K,V> boolean checkInvariants(TreeNode<K,V> t){
            return true;
        }
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return true:数组为空  false:不为空
     */
    private <T> boolean arrayIsEmpty(T[] array){
        return array==null || array.length==0;
    }

    private <T> boolean arrayIsNotEmpty(T[] array){
        return (array!=null && array.length>0);
    }
}
