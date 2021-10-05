package com.dongfeng.study.basicstudy.java.collection;

import java.util.*;
import java.util.function.Consumer;

/**
 * <b> {@link ArrayList}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-24 0:09
 */
public class ArrayListStudy<E> {
    public static void main(String[] args) {

        // ArrayList基本原理
        basePrinciple();

        ArrayListStudy<String> study = new ArrayListStudy<>();
        // ArrayList add方法源码
        study.add("hello");
        // ArrayList remove方法源码
        study.remove(0);

        // ArrayList迭代
        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        list.add("china");
        iteratorStudy(list);

        // ArrayList中Iterator迭代器原理
        study.new Itr();

        // ArrayList实现的接口
        allInterface();

        // ArrayList的时间复杂度和特点
        character();
    }

    /**
     * ArrayList基本原理
     */
    public static void basePrinciple(){
        /*
         * List元素：有序，可重复，可通过下标随机访问元素。
         *
         * ArrayList内部有一个Object数组elementData，一般会有一些预留的空间，有一个整数size记录实际的元素个数。
         * 如下所示：
         * transient Object[] elementData;
         * private int size;
         * ArrayList的各种public方法内部操作的基本都是这个数组和这个整数，elementData会随着实际元素个数的增多而重新分配，
         * 而size则始终记录着实际的与元素个数。
         * 需要看的源码：add方法，remove方法。
         *
         */
        ArrayList<String> list = new ArrayList<>();
        list.add("arrayListStudy");
        list.remove(0);
    }

    /**
     * ArrayList迭代
     */
    public static void iteratorStudy(ArrayList<String> list){
        // ArrayList支持foreach语法：
        for (String s : list) {
            System.out.println(s);
        }

        // 这种foreach语法背后是怎么实现的呢？其实，编译器会将它转换为类似如下代码：
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        /*
         * 1. 迭代器接口：Iterator
         * ArrayList实现了Iterable接口，Iterable表示可迭代。
         * 只要对象实现了Iterable接口，就可以使用foreach语法。
         *
         * 1). Iterable表示对象可以被迭代，它有一个方法iterator()，返回Iterator对象，实际通过Iterator接口的方法进行遍历；
         * 2). 如果对象实现了Iterable，就可以使用foreach语法；
         * 3). 类可以不实现Iterable，也可以创建Iterator对象。
         *
         * 2. ListIterator
         * ListIterator扩展了Iterator接口，增加了一些方法，向前遍历、添加元素、修改元素、返回索引位置等。
         *
         * listIterator()方法返回的迭代器从0开始，而listIterator(int index)方法返回的迭代器从指定位置index开始。
         *
         * 3. 关于迭代
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
         * Java迭代器（Iterator）查找操作与位置变更是紧密相连的。
         * 查找一个元素的唯一方法是调用next，而在执行查找操作的同时，迭代器的位置随之向前移动。
         * 应该将Java迭代器认为是位于两个元素之间。当调用next时，迭代器就越过下一个元素，并返回刚刚越过的那个元素的引用。
         * （看ArrayList中Iterator的实现就明白了。）
         *
         * 调用remove方法之前需要调用next方法：
         * Iterator接口的remove方法将会删除上次调用next方法时返回的元素。如果想要删除指定位置上的元素，仍然需要越过这个元素。
         * 如果调用remove之前没有调用next将是不合法的。如果这样做，将会抛出一个IllegalStateException异常。
         */

        // 为什么删除（remove）元素之前要调用next方法？
        // 要删除一个list中所有包含"测试"的字符串元素，直觉上，代码可以这么写：
        for (String s : list) {
            if (s.contains("测试")){
                list.remove(s);
            }
        }
        // 但运行时会抛出异常：java.util.ConcurrentModificationException
        // 因为迭代器内部会维护一些索引位置相关的数据，要求在迭代过程中，容器不能发生结构性变化，否则这些索引位置就失效了。
        // 所谓【结构性变化就是添加、插入和删除元素】，只是修改元素内容不算结构性变化。

        // 如何正确删除元素，避免异常呢？可以使用迭代器的remove方法
        while (iterator.hasNext()){
            // 调用remove之前需要调用next
            if (iterator.next().contains("测试")){
                iterator.remove();
            }
        }

        /*
         * 迭代器的好处？
         * 为什么要通过迭代器这种方式访问元素呢？直接使用size()/get(index)语法不也可以吗？
         * 在一些场景下，确实没有什么差别，两者都可以。
         * 不过，foreach语法更为简洁一些，更重要的是，【迭代器语法更为通用，它适用于各种容器类。】
         *
         * 此外，【迭代器表示的是一种关注点分离的思想，将数据的实际组织方式与数据的迭代遍历相分离，是一种常见的设计模式。】
         * 需要访问容器元素的代码只需要一个Iterator接口的引用，不需要关注数据的实际组织方式，可以使用一致和统一的方式进行访问。
         * 而提供Iterator接口的代码了解数据的组织方式，可以提供高效的实现。
         *
         * 从封装的思路上讲，【迭代器封装了各种数据组织方式的迭代操作，提供了简单和一致的接口。】
         *
         */
    }

    /**
     * ArrayList实现的接口
     */
    public static void allInterface(){
        /*
         * public class ArrayList<E> extends AbstractList<E>
         *         implements List<E>, RandomAccess, Cloneable, java.io.Serializable
         * （其中，List接口实现了Collection接口，Collection接口实现了Iterable接口）
         *
         * 除了Iterable接口，ArrayList主要实现的接口有：Collection，ArrayList，RandomAccess。
         *
         * 1. Collection
         * Collection表示一个数据集合，数据间没有位置或顺序的概念。
         * 抽象类AbstractCollection对Collection集合中的一些方法都提供了默认实现，实现的方式就是利用迭代器方法逐个操作。
         * ArrayList继承了AbstractList，而AbstractList又继承了AbstractCollection,
         * ArrayList对其中一些方法进行了重写，以提供更为高效的实现。
         *
         * 2. List
         * List表示有顺序或位置的数据集合，它扩展了Collection。
         *
         * 3. RandomAccess
         * RandomAccess是一个标记接口，用于声明类的一种属性。
         * RandomAccess接口表明支持高效的随机访问：实现了RandomAccess接口的类表示可以随机访问，可随机访问就是具备类似数组那样的特性，
         * 数据在内存是连续存放的，根据索引值就可以直接定位到具体的元素，访问效率很高。
         *
         * 有没有声明RandomAccess有什么关系呢？主要用于一些通用的算法代码中，它可以根据这个声明而选择效率更高的实现。
         * 比如，Collections类中有一个方法binarySearch，在List中进行二分查找，
         * 它的实现代码就根据list是否实现了RandomAccess而采用不同的实现机制。
         *
         */
    }

    /**
     * ArrayList的时间复杂度和特点
     */
    public static void character(){
        /*
         * ArrayList特点
         *
         * 对于ArrayList，它的特点是内部采用动态数组实现，这决定了以下几点：
         * 1. 随机访问效率高，按照索引位置进行访问效率很高，用算法描述中的术语，效率是O(1)，简单说就是可以一步到位。
         * 2. 除非数组已排序，否则按照内容查找元素效率比较低，具体是O(N), N为数组内容长度，也就是说，性能与数组长度成正比。
         * 3. 添加元素的效率还可以，重新分配和复制数组的开销被平摊了，具体来说，添加N个元素的效率为O(N)。
         * 4. 插入和删除元素的效率（性能）比较低，因为需要移动元素，具体为O(N)。
         *
         * 需要说明的是，ArrayList不是线程安全的。
         */

        /*
         * ArrayList各个方法的时间复杂度：
         * get() 直接读取下标，复杂度 O(1)
         *
         * add(E) 直接在队尾添加，复杂度 O(1)
         *
         * add(index, E) 在第n个元素后插入，n后面的元素需要向后移动，复杂度 O(n)
         *
         * remove() 删除元素后面的元素需要逐个前移，复杂度 O(n)
         */
    }


    // 一些常量
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    // 保存元素的数组
    transient Object[] elementData;
    // 元素实际个数
    private int size;
    // 内部的修改次数，方便在迭代中检测结构性修改变化
    protected transient int modCount = 0;

    public ArrayListStudy(){
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public ArrayListStudy(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    public ArrayListStudy(Collection<? extends E> c) {
        if (c == null){
            throw new NullPointerException();
        }
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class) {
                elementData = Arrays.copyOf(elementData, size, Object[].class);
            }
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * ArrayList迭代器实现原理：Iterator迭代器的实现原理。
     *
     * <p> 仅仅是从ArrayList复制过来方便看源码，写注释。 </p>
     */
    private class Itr implements Iterator<E> {
        // 下一个要返回的元素的索引（位置），初始值为0
        int cursor;
        // 最后一个返回的元素的索引，如果没有，为-1
        int lastRet = -1;
        // expectedModCount: 期望的修改次数，初始化为外部类当前的修改次数modCount（成员内部类可以直接访问外部类的实例变量）
        // 每次发生结构性变化的时候modCount都会增加，
        // 而每次迭代器操作的时候都会检查expectedModCount是否与modCount相同，这样就能检测出结构性变化。
        int expectedModCount = modCount;


        Itr() {}

        @Override
        public boolean hasNext() {
            // 下一个要返回的元素的索引不等于size说明还有
            return cursor != size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            // 检查是否发生了结构型变化
            checkForComodification();
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            Object[] elementData = ArrayListStudy.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            // 到这里没有发生结构性变化，更新cursor和lastRet的值，以保持其语义，
            // 然后返回对应的元素
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        /**
         * 需要注意的是，调用remove方法前必须先调用next
         */
        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            // 检查是否发生了结构性变化
            checkForComodification();

            try {
                // 调用了ArrayList的remove方法
                ArrayListStudy.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayListStudy.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayListStudy.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }



    /**
     * ArrayList的add方法，仅仅是复制过来方便看源码，写注释。
     *
     * @see ArrayList#add(Object)
     */
    public boolean add(E e){
        // Increments modCount!!
        // 确保数组容量是够的
        ensureCapacityInternal(size + 1);
        // 数组添加新元素e，元素个数也加一
        elementData[size++] = e;
        return true;
    }

    /**
     * ArrayList的add方法，仅仅是复制过来方便看源码，写注释。
     * @see ArrayList#remove(int)
     */
    public E remove(int index) {
        rangeCheck(index);

        // 内部修改次数加1
        modCount++;
        // 该索引处的旧值
        E oldValue = elementData(index);

        // 计算要移动的元素个数，index索引后所有的元素都要往前移动一位。
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            // 往前移动
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
        }
        // 将size减一，同时释放引用以便原对象垃圾回收
        elementData[--size] = null;

        return oldValue;
    }


    /**
     * 确保数组容量是够的
     */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // 数组空的，则首次至少要分配的大小为DEFAULT_CAPACITY, DEFAULT_CAPACITY的值为10
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // 增加内部的修改次数
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0) {
            // 需要的长度大于当前数组的长度，调用grow方法进行数组扩容
            grow(minCapacity);
        }
    }

    /**
     * 数组扩容：扩容为原来的1.5倍
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;

        // newCapacity：数组新容量
        // newCapacity为oldCapacity的1.5倍。右移一位相当于除以2
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        if (newCapacity - minCapacity < 0) {
            // 如果扩展1.5倍还是小于minCapacity，就扩展为minCapacity
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            // 容量太大
            newCapacity = hugeCapacity(minCapacity);
        }

        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            // overflow
            throw new OutOfMemoryError();
        }

        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }



    E elementData(int index) {
        return (E) elementData[index];
    }

    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
}
