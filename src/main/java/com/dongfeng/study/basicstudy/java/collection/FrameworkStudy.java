package com.dongfeng.study.basicstudy.java.collection;


import java.util.*;

/**
 * <b> Java集合框架总结学习 </b>
 * @author eastFeng
 * @date 2021-04-24 3:39
 */
public class FrameworkStudy {
    public static void main(String[] args) {
        // 抽象容器类
        abstractCollects();
        // Collections学习
        collectionsStudy();

        // 集合框架总结：
        // 用法和特点
        usageAndCharacter();
        // 数据结构和算法
        dataStructureAndAlgorithm();
        // 设计思维和模式
        designPattern();
    }

    /**
     * 抽象容器类
     */
    public static void abstractCollects(){
        /*
         * 集合框架有6个基本的接口：Collection、List、Set、Queue、Deque和Map。
         *
         * 有6个抽象容器类：
         * 1. AbstractCollection：实现了Collection接口，
         *   被抽象类AbstractList、AbstractSet、AbstractQueue继承，ArrayDeque也继承自AbstractCollection。
         * 2. AbstractList：父类是AbstractCollection，实现了List接口，被ArrayList、AbstractSequentialList继承。
         * 3. AbstractSequentialList：父类是AbstractList，被LinkedList继承。
         * 4. AbstractMap：实现了Map接口，被TreeMap、HashMap、EnumMap继承。
         * 5. AbstractSet：父类是AbstractCollection，实现了Set接口，被HashSet、TreeSet和EnumSet继承。
         * 6. AbstractQueue：父类是AbstractCollection，实现了Queue接口，被PriorityQueue继承。
         */
    }
    /**
     * {@link java.util.Collections}学习
     */
    public static void collectionsStudy(){
        /*
         * Collections是个工具类。
         * 类Collections以静态方法的方式提供了很多通用算法和功能，
         * 这些功能大概可以分为两类:
         * 1. 对容器接口对象进行操作。
         * 2. 返回一个容器接口对象。
         *
         * 对于第一类，大概可以分为三组：
         * 1). 查找和替换。
         * 2). 排序和调整顺序。
         * 3). 添加和修改。
         *
         * 对于第2类，大概可以分为两组：
         * 1). 适配器：将其他类型的数据转换为容器接口对象。
         * 2). 装饰器：修饰一个给定容器接口对象，增加某种性质。
         */

        /*
         * 所谓适配器，就是将一种类型的接口转换成另一种接口，类似于电子设备中的各种USB转接头，一端连接某种特殊类型的接口，一段连接标准的USB接口。
         * Collections类提供了几组类似于适配器的方法：
         * 1. 空容器方法：类似于将null或“空”转换为一个标准的容器接口对象。
         * 2. 单一对象方法：将一个单独的对象转换为一个标准的容器接口对象。
         * 3. 其他适配方法：将Map转换为Set等。
         */
        // 空容器方法
        List<Object> emptyList = Collections.emptyList();
        Set<Object> emptySet = Collections.emptySet();
        Map<Object, Object> emptyMap = Collections.emptyMap();
        // 一个空容器对象有什么用呢？空容器对象经常用作方法返回值

        /*
         * 装饰器接受一个接口对象，并返回一个同样接口的对象，
         * 不过，新对象可能会扩展一些新的方法或属性，扩展的方法或属性就是所谓的“装饰”，
         * 也可能会对原有的接口方法做一些修改，达到一定的“装饰”目的。
         * Collections有三组装饰器方法，它们的返回对象都没有新的方法或属性，但改变了原有接口方法的性质，
         * 经过“装饰”后，它们更为安全了，具体分别是写安全、类型安全和线程安全
         */

    }

    private static List<Integer> emptyListTest(String a){
        if ("null".equals(a)){
            return Collections.EMPTY_LIST;
        }else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(134);
            return list;
        }
    }

    /**
     * 用法和特点
     */
    public static void usageAndCharacter(){
        /*
         * 容器类（集合框架）有两个根接口，分别是Collection和Map, 【Collection表示单个元素的集合，Map表示键值对的集合】。
         *
         * 【Collection】
         * Collection表示的数据集合有基本的增、删、查、遍历等方法，但没有定义元素间的顺序或位置，也没有规定是否有重复元素。
         * Collection包含的方法：
         * boolean add(E e); boolean addAll(Collection<? extends E> c); // 增
         * boolean remove(Object o); boolean removeAll(Collection<?> c); // 删
         * boolean contains(Object o); boolean containsAll(Collection<?> c); // 查
         * Iterator<E> iterator(); // 遍历
         *
         * 【List】
         * List是Collection的子接口，表示一个有序（元素的进入顺序）集合，允许元素重复，增加了根据元素索引进行操作的方法。
         * 它有两个主要的实现类：ArrayList和LinkedList。
         * ArrayList基于数组实现， LinkedList基于链表实现；
         * ArrayList的随机访问效率很高，但从中间插入和删除元素需要移动元素，效率比较低，
         * LinkedList则正好相反，随机访问效率比较低，但增删元素只需要调整邻近节点的链接。
         * List可以采用两种方式访问元素：使用迭代器访问，或者使用一个整数索引来访问。
         * 后一种方法称为随机访问（random access），因为这样可以按任意顺序访问元素。与之不同，使用迭代器访问时，必须顺序地访问元素。
         *
         * 【Set】
         * Set也是Collection的子接口，表示一个无序集合，不允许重复元素（保证不含重复元素），它没有增加新的方法。
         * 取元素时只能用Iterator接口取得所有元素，再逐一遍历所有元素。
         * 它有两个主要的实现类：HashSet和TreeSet。
         * HashSet基于哈希表实现，要求键重写hashCode方法，效率更高，但元素间没有顺序；
         * TreeSet基于排序二叉树实现，元素按比较有序，元素需要实现Comparable接口，或者创建TreeSet时提供一个Comparator对象。
         * HashSet还有一个子类LinkedHashSet可以按插入有序。
         * Set还有一个针对枚举类型的实现类EnumSet，它基于位向量实现，效率很高。
         *
         * 【Queue】
         * Queue是Collection的子接口，表示先进先出的队列，在尾部添加，从头部查看或删除。
         * Deque是Queue的子接口，表示更为通用的双端队列，有明确的在头或尾进行查看、添加和删除的方法。
         * （注：双端列表Deque也只能在队列两端进行操作，不能像List一样根据索引在列表任意位置操作）
         * 普通队列有两个主要的实现类：LinkedList和ArrayDeque。
         * LinkedList基于链表实现，ArrayDeque基于循环数组实现。
         * 一般而言，如果只需要Deque接口，ArrayDeque的效率更高一些。
         * Queue还有一个特殊的实现类PriorityQueue，表示优先级队列，内部是用堆实现的。
         * 堆除了用于实现优先级队列，还可以高效方便地解决很多其他问题，比如求前K个最大的元素、求中值等。
         *
         * 【Map】
         * Map接口表示键值对集合，经常根据键进行操作。
         * 它有两个主要的实现类：HashMap和TreeMap。
         * HashMap基于哈希表实现，要求键重写hashCode方法，操作效率很高，但元素没有顺序。
         * TreeMap基于排序二叉树实现，要求键实现Comparable接口，或提供一个Comparator对象，操作效率稍低，但可以按键有序。
         *
         * HashMap还有一个子类LinkedHashMap，它可以按插入或访问有序。之所以能有序，是因为每个元素还加入到了一个双向链表中。
         * 如果键本来就是有序的，使用LinkedHashMap而非TreeMap可以提高效率。按访问有序的特点可以方便地用于实现LRU缓存。
         *
         * 如果键为枚举类型，可以使用专门的实现类EnumMap，它使用效率更高的数组实现。
         *
         * 需要说明的是，除了Hashtable、Vector和Stack，我们介绍的各种容器类都不是线程安全的，
         * 也就是说，如果多个线程同时读写同一个容器对象，是不安全的。
         * 如果需要线程安全，可以使用Collections提供的synchronizedⅩⅩⅩ方法对容器对象进行同步，或者使用线程安全的专门容器类。
         *
         * 此外，容器类提供的迭代器都有一个特点，都会在迭代中间进行结构性变化检测，如果容器发生了结构性变化，
         * 就会抛出ConcurrentModificationException，所以不能在迭代中间直接调用容器类提供的add/remove方法，
         * 如需添加和删除，应调用迭代器的相关方法。
         *
         * 在解决一个特定问题时，经常需要综合使用多种容器类。
         * 比如，要统计一本书中出现次数最多的前10个单词，可以先使用HashMap统计每个单词出现的次数，
         * 再使用TopK类用PriorityQueue求前10个单词，或者使用Collections提供的sort方法。
         *
         */

        LinkedList<String> list = new LinkedList<>();
    }

    /**
     * 数据结构和算法
     */
    public static void dataStructureAndAlgorithm(){
        /*
         * 在容器类中，我们看到了如下数据结构的应用：
         * 1. 动态数组：ArrayList内部就是动态数组，HashMap内部的链表数组也是动态扩展的，ArrayDeque和PriorityQueue内部也都是动态扩展的数组。
         *
         * 2. 链表：LinkedList是用双向链表实现的，HashMap中映射到同一个链表数组的键值对是通过单向链表链接起来的，
         *    LinkedHashMap中每个元素还加入到了一个双向链表中以维护插入或访问顺序。
         *
         * 3. 哈希表：HashMap是用哈希表实现的，HashSet、LinkedHashSet和LinkedHashMap基于HashMap，内部当然也是哈希表。
         *
         * 4. 排序二叉树：TreeMap是用红黑树（基于排序二叉树）实现的，
         *    TreeSet内部使用TreeMap，当然也是红黑树，红黑树能保持元素的顺序且综合性能很高。
         *
         * 5. 堆：PriorityQueue是用堆实现的，堆逻辑上是树，物理上是动态数组，堆可以高效地解决一些其他数据结构难以解决的问题。
         *
         * 6. 循环数组：ArrayDeque是用循环数组实现的，通过对头尾变量的维护，实现了高效的队列操作。
         *
         * 7. 位向量：EnumSet和BitSet是用位向量实现的，对于只有两种状态，且需要进行集合运算的数据，使用位向量进行表示、位运算进行处理，精简且高效。
         */
    }

    /**
     * 设计思维和模式
     */
    public static void designPattern(){

    }
}
