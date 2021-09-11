package com.dongfeng.study.sourcecode.java8.util;

import com.amazonaws.transform.MapEntry;

import java.util.*;
import java.util.SortedMap;

/**
 *
 * <p> {@link java.util.TreeMap}实现了{@link SortedMap}接口，也就是说会按照键key的大小顺序对Map中的元素进行排序，
 * 键key大小的评判可以通过其本身的自然顺序（natural ordering），这时键需要实现{@link Comparable}接口，
 * 也可以通过构造时传入的比较器（{@link Comparator}接口实例）根据自定义的排序方式对键排序。
 *
 * @author eastFeng
 * @date 2021-04-26 2:05
 */
public class TreeMap<K,V> extends AbstractMap<K,V>{

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    // 静态内部类Entry
    /**
     * 红黑树节点，表示一个键值对。
     *
     * <p>二叉查找树（也叫二叉排序树、二叉搜索树）的性质：
     * <ol>
     *     <li> 若左子树不为空，则左子树上所有结点的值均小于它的根结点的值；
     *     <li> 若右子树不为空，则右子树上所有结点的值均大于它的根结点的值；
     *     <li> 左、右子树也分别为二叉查找树；
     * </ol>
     *
     * <p> 红黑树的另一种定义是含有红黑链接并满足下列条件的二叉查找树：
     * <ol>
     *     <li> 每个节点（链接）要么是红色，要么是黑色。
     *     <li> 【<b>根节点必须是黑色</b>】。
     *     <li> 【<b>红链接（节点）均为左链接（节点）</b>】。但是左右节点都可以是黑色。
     *     <li> 【<b>红节点（链接）不能连续</b>】（没有任何一个结点同时和两条红链接相连）：红节点的子节点和父节点都不能是红色。
     *     <li> 该树是完美黑色平衡的，即任意空链接到根结点的路径上的黑链接（黑节点）数量相同。
     *     <li> 每个叶节点（null）都是黑色的。
     * </ol>
     *
     * <p> 每当根结点由红变黑时树的黑节点高度就会加1。
     *
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    static final class Entry<K,V> implements Map.Entry<K,V>{
        // 键
        K key;
        // 值
        V value;
        // 左子节点（左子树）
        Entry<K,V> left;
        // 右子节点（右子树）
        Entry<K,V> right;
        // 父节点
        Entry<K,V> parent;
        // 链接颜色
        boolean color = BLACK;

        Entry(K key, V value, Entry<K,V> parent){
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o){
                return true;
            }
            if (o == null){
                return false;
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            return Objects.equals(key,e.getKey()) && Objects.equals(value,e.getValue());
        }

        @Override
        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    // 实例变量

    /**
     * 用于维护此TreeMap中顺序的键的比较器，如果此TreeMap使用键的自然顺序，则为null。
     */
    private final Comparator<? super K> comparator;
    /**
     * 红黑树的根节点
     */
    private transient TreeMap.Entry<K,V> root;
    /**
     * 此Map中实际键值对的数量
     */
    private transient int size = 0;
    /**
     * 内部修改次数，方便迭代期间检查是否发生结构性修改。
     */
    private transient int modCount = 0;

    // 构造方法

    public TreeMap() {
        comparator = null;
    }

    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public TreeMap(Map<? extends K, ? extends V> m) {
        comparator = null;
        putAll(m);
    }

    public TreeMap(SortedMap<K, ? extends V> m) {
        comparator = m.comparator();
//        try {
//            buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
//        } catch (java.io.IOException cannotHappen) {
//        } catch (ClassNotFoundException cannotHappen) {
//        }
    }

    // 查询操作

    public Comparator<? super K> comparator() {
        return comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // TreeMap可以高效地按键进行查找，但如果要根据值进行查找，则需要遍历
        // 从第一个节点开始，逐个进行比较
        for (Entry<K,V> e = getFirstEntry(); e != null; e = successor(e)) {
            if (valEquals(value, e.value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        TreeMap.Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
    }

    /**
     * 根据key找到对应的节点
     *
     * @param key 键
     * @return 红黑树的节点（键值对）
     */
    final Entry<K,V> getEntry(Object key){
        if (root == null){
            return null;
        }

        // 判断是否设置了comparator
        if (comparator != null){
            return getEntryUsingComparator(key);
        }
        // Comparator的compare的两个参数都没要求不可以为null。 --- 实例变量comparator不为空，则TreeMap中可以包含null键。

        // Comparable的compareTo方法的调用方和参数都不允许为null。--- 实例变量comparator为null，则TreeMap中不可以包含null键。
        if (key == null){
            throw new NullPointerException();
        }

        Entry<K, V> t = this.root;
        Comparable<? super K> k = (Comparable<? super K>) key;
        while (t != null){
            int cmp = k.compareTo(t.key);
            if (cmp < 0){
                // 左子树
                t = t.left;
            }else if (cmp > 0){
                // 右子树
                t = t.right;
            }else {
                return t;
            }
        }

        return null;
    }

    final Entry<K,V> getEntryUsingComparator(Object key){
        K k = (K) key;
        Comparator<? super K> cpr = comparator;

        if (cpr != null){
            Entry<K, V> t = this.root;
            while (t != null){
                int cmp = cpr.compare((K) key, t.key);
                if (cmp < 0){
                    // 左子树
                    t = t.left;
                }else if (cmp > 0){
                    // 右子树
                    t = t.right;
                }else {
                    return t;
                }
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value){
        Entry<K, V> t = this.root;

        // 判断根节点是否为null
        if (t == null){
            // 类型检查：这里的目的不是为了比较，而是为了检查key的类型和是否为null，如果类型不匹配或为null，会抛出异常
            compare(key, key);

            root = new Entry<>(key, value, null);
            size++;
            modCount++;
            return null;
        }

        // 不是第一次添加，添加的关键步骤是寻找父节点
        int cmp;
        Entry<K, V> parent;
        Comparator<? super K> cpr = this.comparator;
        // 判断是否设置了comparator
        if (cpr != null){
            do {
                // 下一个t节点的父节点
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0){
                    t = t.left;
                }else if (cmp > 0){
                    t = t.right;
                }else{
                    // 命中，更新值并返回旧值
                    return t.setValue(value);
                }
            } while (t != null);
        }else {
            // 类型转换放在循环外面，不用每次比较都进行类型转换
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                // 下一个t节点的父节点
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0){
                    t = t.left;
                }else if(cmp > 0){
                    t = t.right;
                }else {
                    // 命中，更新值并返回旧值
                    return t.setValue(value);
                }
            } while (t != null);
        }

        // 走到这里说明没找到，需要parent节点下面插入新节点。parent有个子节点是为空的。
        Entry<K, V> newNode = new Entry<>(key, value, parent);
        // 根据新的键与父节点的键比较的结果，作为左节点或右节点
        if (cmp < 0){
            parent.left = newNode;
        }else {
            parent.right = newNode;
        }
        // 插入新节点之后红黑树的结构发生变化，需要调整，保证红黑树的特点。
        fixAfterInsertion(newNode);
        size++;
        modCount++;
        return null;
    }

    /**
     * 根据键删除键值对
     * @param key 键
     * @return 键对应的旧值
     */
    @Override
    public V remove(Object key) {
        // 根据key找到节点
        Entry<K,V> p = getEntry(key);
        if (p == null) {
            return null;
        }

        V oldValue = p.value;
        // 删除节点
        deleteEntry(p);
        return oldValue;
    }

    final int compare(Object k1, Object k2){
        if (comparator == null){
            return ((Comparable<? super K>)k1).compareTo((K)k2);
        }else {
            return comparator.compare((K)k1, (K)k2);
        }
    }

    static final boolean valEquals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    static <K,V> Map.Entry<K,V> exportEntry(TreeMap.Entry<K,V> e) {
        return (e == null) ? null :
                new AbstractMap.SimpleImmutableEntry<>(e);
    }

    static <K,V> K keyOrNull(TreeMap.Entry<K,V> e) {
        return (e == null) ? null : e.key;
    }

    static <K> K key(Entry<K,?> e) {
        if (e==null) {
            throw new NoSuchElementException();
        }
        return e.key;
    }


    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }


    //  -----------------红黑树的一些操作-------------

    /**
     * 返回第一个节点
     */
    final Entry<K,V> getFirstEntry() {
        // 第一个节点就是最左边的节点。

        Entry<K,V> p = root;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
        }
        return p;
    }

    /**
     * 返回最后一个节点
     */
    final Entry<K,V> getLastEntry(){
        Entry<K, V> p = this.root;
        if (p != null){
            while (p.right != null){
                p = p.right;
            }
        }
        return p;
    }

    /**
     * 返回指定节点的后继节点
     */
    static <K,V> Entry<K,V> successor(Entry<K,V> t) {
        if (t == null) {
            return null;
        } else if (t.right != null) {
            // 有右子节点，后继节点就是右子树的最小节点
            // 这种情况，后继节点一定没有左子节点
            Entry<K,V> p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            // 没有右子节点，则第一个非右子节点的父节点就行后继节点
            Entry<K,V> p = t.parent;
            // p是ch的父节点
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) { // ch是右子节点
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * 返回指定节点的前序节点
     */
    static <K,V> Entry<K,V> predecessor(Entry<K,V> t) {
        if (t == null) {
            return null;
        } else if (t.left != null) {
            // 有左子节点，前序节点就是左子树的最大节点
            Entry<K,V> p = t.left;
            while (p.right != null) {
                p = p.right;
            }
            return p;
        } else {
            // 没有左子节点，则第一个非左子节点的父节点就行后继节点
            Entry<K,V> p = t.parent;
            // p是ch的父节点
            Entry<K,V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * <b> 删除指定节点 </b>
     *
     * @param p 要删除的节点
     */
    private void deleteEntry(Entry<K,V> p) {
        modCount++;
        size--;
        /*
         * 要删除的节点有三种情况：
         * 1. 叶子节点（没有子节点）：这个容易处理，直接修改父节点对应引用置null即可。
         * 2. 只有一个子节点：就是在父亲节点和孩子节点直接建立链接。
         * 3. 有两个子节点：先找到后继节点，找到后，替换当前节点的内容为后继节点，然后再删除后继节点，
         *   因为这个后继节点一定没有左子节点（因为要删除的节点p有右子节点），所以就将两个子节点的情况转换为了前面两种情况。
         */
        if (p.left != null && p.right != null) {
            // 第三种情况，节点p有两个子节点
            // 先找到后继节点s，s一定没有左子节点，可能有一个右子节点，也可能没有子节点（叶子节点）
            Entry<K,V> s = successor(p);
            // 当前节点p的key和value设置为了s的key和value
            p.key = s.key;
            p.value = s.value;
            // 将待删节点p指向s，这样就转换成了删除有一个子节点的节点或是删除叶子节点的情况。
            p = s;
        }

        // 到这里待删节点p只有前两种情况了

        /*
         * p为待删节点，replacement为要替换p的子节点（p至多有一个子节点），
         * 主体代码就是在p的父节点p.parent和replacement之间建立链接，以替换p.parent和p原来的链接，
         * 如果p.parent为null，则修改root以指向新的根。fixAfterDeletion方法重新修复红黑树。
         */
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        // 判断p的子节点是否为null
        if (replacement != null) {
            // 有子节点，在子节点和父节点之间建立链接

            // 设置p的子节点的父节点为p的父节点
            replacement.parent = p.parent;
            if (p.parent == null) {
                // 有子节点，没有父节点，则删除的是根节点
                root = replacement;
            } else if (p == p.parent.left) {
                // p是左子节点
                p.parent.left  = replacement;
            } else {
                // p是右子节点
                p.parent.right = replacement;
            }

            // p被删除，不再引用其他节点
            p.left = p.right = p.parent = null;
            // 修复红黑树
            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            // p是叶子节点情况1：没有子节点也没有父节点，则删除的是最后一个节点，修改root为null
            root = null;
        } else {
            // p是叶子节点情况2：没有子节点但是有父节点

            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }

            if (p.parent != null) {
                // 根据待删节点p是左子节点还是右子节点，相应的设置其父节点的子节点为null。
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    /**
     * <b> 删除节点之后修复红黑树 </b>
     * @param x 被删节点或者是被删节点的子节点
     */
    private void fixAfterDeletion(Entry<K,V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry<K,V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                Entry<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

    /**
     * <b> 插入新节点p之后修复红黑树 </b>
     *
     * <p> 对红黑树进行插入和删除等操作时，对树做了修改，那么可能会违背红黑树的性质，这时需要调整，保证红黑树的性质。
     * <p> 调整可以分为两类：一类是颜色调整，即改变某个节点的颜色；另一类是结构调整，即改变红黑树的结构关系。
     * <p> 结构调整包含两个基本操作：
     * <ol>
     *     <li> 左旋（Rotate Left）：{@link #rotateLeft(Entry)} </li>
     *     <li> 右旋（Rotate Right）：{@link #rotateRight(Entry)} </li>
     * </ol>
     * @param x 插入的新节点
     */
    private void fixAfterInsertion(Entry<K,V> x) {
        // 首先将节点x变为红节点
        x.color = RED;

        // 一个节点的左右子节点都是红色，则红色向上传递，该节点的左右子节点变为红色。
        // 注: 为了简化描述，下面注释中，某个节点m是左子节点 意思是 节点m是其（节点m的）父节点的左子节点。

        while (x != null && x != root && x.parent.color == RED) {
            // 节点x的父节点为红节点，这时红节点连续，需要进行修改。

            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                // 节点x的父节点是左子节点
                // y：节点x的父节点的父节点的右子节点
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    // 节点y为红节点，这时节点y的父节点的左右子节点都为红节点
                    // 将子结点的颜色由红变黑之外，同时还要将父结点的颜色由黑变红
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    // 节点y为黑节点，但是红节点也是连续了：节点x和节点x的父节点都是红节点
                    if (x == rightOf(parentOf(x))) {
                        // x是其父节点的右子节点，需要左旋转
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    // x是其父节点的左子节点，需要右旋转
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                // 节点x的父节点是右子节点
                // y：节点x的父节点的父节点的左子节点
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        // x是其父节点的左子节点
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    /**
     * 返回指定节点的颜色
     */
    private static <K,V> boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }
    /**
     * 返回指定节点的父节点
     */
    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }
    /**
     * 设置节点的颜色
     */
    private static <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }
    /**
     * 返回指定节点的左子节点
     */
    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }
    /**
     * 返回指定节点的右子节点
     */
    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }

    /**
     * <b> 左旋转节点p的右子节点 </b>
     *
     * <p> 父节点和右子节点交换
     * <p> 左旋转 ：一条红色的右节点需要被转化为左节点。
     * 在我们实现的某些操作中可能会出现红色右节点或者两个连续的红节点，但在操作完成前这些情况都会被小心地旋转并修复。
     * 旋转操作会改变红链接的指向。首先，假设我们有一条红色的右节点需要被转化为左节点。这个操作叫做左旋转，
     * 它对应的方法接受红黑树中的节点作为参数。假设该结点的右节点是红色的，
     * <b> 只是将两个键中的较小者作为根节点变为将较大者作为根节点。</b>
     *
     * <p> 节点p的右节点为红色，需要把该红节点旋转到左边。
     * <p> 注意：节点p可以是其（p节点的）父节点的左节点或者右节点，节点p的颜色可黑可红。
     *
     * @param p 红黑树的节点，该节点的右节点为红色
     */
    private void rotateLeft(Entry<K,V> p) {
        if (p != null) {
            // 父节点p的右子节点r
            Entry<K,V> r = p.right;

            // 将两个键中的较小者（节点p）作为根节点变为将较大者（节点r）作为根节点

            // 原来的父节点p需要改变的是右节点（右子树）
            // 原来的父节点p的右节点r 需要改变的是左节点（左子树）

            // 调整原来父节点p的右节点
            // 父节点p小于右子树r的所有节点，所以转换之后父节点的右节点变为原来右节点r的左节点
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }

            // 旋转之后新的父节点的父节点是不变的
            r.parent = p.parent;
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                // 节点p是其父节点的左节点
                p.parent.left = r;
            } else {
                // 节点p是其父节点的右节点
                p.parent.right = r;
            }

            // r作为根节点，则r的左节点肯定是r原来的根节点h
            r.left = p;
            p.parent = r;
        }
    }

    /**
     * 右旋转p节点的左子节点
     * <p> 父节点和左子节点交换
     * @param p 节点
     */
    private void rotateRight(Entry<K,V> p) {
        if (p != null) {
            // 父节点p的左子节点
            Entry<K,V> l = p.left;

            // 将两个键中的较大者（节点p）作为根节点变为将较小者（节点l）作为根节点。

            // 改变原来父节点p的左节点
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }

            l.parent = p.parent;
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }

            l.right = p;
            p.parent = l;
        }
    }
}
