package com.dongfeng.study.basicstudy.collection;


import java.util.ArrayDeque;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * <b> {@link java.util.ArrayDeque}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-24 18:36
 */
public class ArrayDequeStudy<E> {

    public static void main(String[] args) {
        /*
         * LinkedList实现了队列接口Queue和双端队列接口Deque, Java容器类中还有一个双端队列的实现类【ArrayDeque，它是基于数组实现的】。
         * 我们知道，一般而言，由于需要移动元素，数组的插入和删除效率比较低，但ArrayDeque的效率却非常高。
         * 【ArrayDeque实现了Deque接口，同LinkedList一样，它的队列长度也是没有限制的。】
         */
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        // 基本原理
        basePrinciple();
        // ArrayDeque 构造方法源码
        ArrayDequeStudy<Integer> study = new ArrayDequeStudy<>(8);
        // ArrayDeque add方法源码
        study.add(1);
        // ArrayDeque addFirst方法源码
        study.addFirst(2);
        // ArrayDeque contains方法源码
        boolean contains = study.contains(1);
        // ArrayDeque size方法源码
        int size = study.size();
        // ArrayDeque toArray方法源码
        Object[] objects = study.toArray();

        // ArrayDeque特点分析
        character();
    }

    /**
     * ArrayDeque基本原理
     */
    public static void basePrinciple(){
        /*
         * ArrayDeque内部主要有如下实例变量：
         *
         * transient Object[] elements;
         * transient int head;
         * transient int tail;
         *
         * elements就是存储元素的数组。ArrayDeque的高效来源于head和tail这两个变量，
         * 它们使得物理上简单的从头到尾的数组变为了一个逻辑上循环的数组，避免了在头尾操作时的移动。
         *
         * 1. 循环数组
         * 对于一般数组，比如arr，第一个元素为arr[0]，最后一个为arr[arr.length-1]。
         * 但对于ArrayDeque中的数组，它是一个逻辑上的循环数组，
         * 所谓循环是指元素到数组尾之后可以接着从数组头开始，数组的长度、第一个和最后一个元素都与head和tail这两个变量有关，
         * 具体来说：
         * 1) 如果head和tail相同，则数组为空，长度为0。
         * 2) 如果tail大于head，则第一个元素为elements[head]，最后一个为elements[tail-1]，长度为tail-head，元素索引从head到tail-1。
         * 3) 如果tail小于head，且为0，则第一个元素为elements[head]，最后一个为elements[elements.length-1]，
         *    元素索引从head到elements.length-1。
         * 4) 如果tail小于head，且大于0，则会形成循环，第一个元素为elements[head]，最后一个是elements[tail-1]，
         *    元素索引从head到elements.length-1，然后再从0到tail-1。
         *
         * 原理总结：
         * 内部它是一个动态扩展的循环数组，通过head和tail变量维护数组的开始和结尾，数组长度为2的幂次方，
         * 使用高效的位操作进行各种判断，以及对head和tail进行维护。
         *
         */
    }

    /**
     * ArrayDeque特点分析
     */
    public static void character(){
        /*
         * ArrayDeque实现了双端队列，内部使用循环数组实现，这决定了它有如下特点：
         * 1. 在两端添加、删除元素的效率很高，动态扩展需要的内存分配以及数组复制开销可以被平摊，具体来说，添加N个元素的效率为O(N)。
         * 2. 根据元素内容查找和删除的效率比较低，为O(N)。
         * 3. 与ArrayList和LinkedList不同，没有索引位置的概念，不能根据索引位置进行操作。
         *
         * ArrayDeque和LinkedList都实现了Deque接口，应该用哪一个呢？
         * 如果只需要Deque接口，从两端进行操作，一般而言，ArrayDeque效率更高一些，应该被优先使用；
         * 如果同时需要根据索引位置进行操作，或者经常需要在中间进行插入和删除，则应该选LinkedList。
         *
         * 无论是ArrayList、LinkedList还是Array-Deque，按内容查找元素的效率都很低，都需要逐个进行比较，有没有更有效的方式呢？
         * 让我们下一章来看各种Map和Set。
         */
    }



    transient Object[] elements;
    transient int head;
    transient int tail;
    private static final int MIN_INITIAL_CAPACITY = 8;

    public ArrayDequeStudy(){
        // 默认构造方法，分配一个长度为16的数组
        elements = (E[]) new Object[16];
    }

    /**
     * 构造参数：
     * ArrayDeque的构造参数学习，仅仅是复制过来方便看源码，写注释。
     * @see ArrayDeque#ArrayDeque(int)
     */
    public ArrayDequeStudy(int numElements) {
        // 不是简单地分配给定的长度，而是调用了allocateElements
        allocateElements(numElements);
    }

    public ArrayDequeStudy(Collection<? extends E> c) {
        // 分配数组
        allocateElements(c.size());
        addAll(c);
    }

    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }

    /**
     * 计算应该分配的数组的长度
     *
     * <p> 如果numElements小于8，就是8。
     * <p> 在numElements大于等于8的情况下，分配的实际长度是严格大于numElements并且为2的整数次幂的最小数。
     * 比如，如果numElements为10，则实际分配16，如果num-Elements为32，则为64。
     *
     * <p>为什么要为2的幂次数呢？我们待会会看到，这样会使得很多操作的效率很高。
     * 为什么要严格大于numElements呢？因为循环数组必须时刻至少留一个空位，tail变量指向下一个空位，
     * 为了容纳numElements个元素，至少需要numElements+1个位置。
     */
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            // Too many elements, must back off
            if (initialCapacity < 0) {
                // Good luck allocating 2 ^ 30 elements
                initialCapacity >>>= 1;
            }
        }
        return initialCapacity;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        // 循环调用add方法
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 从尾部添加：
     * ArrayDeque的add方法，仅仅是复制过来方便看源码，写注释
     * @see ArrayDeque#add(Object)
     */
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /**
     * 从尾部添加：
     * ArrayDeque的addLast方法，仅仅是复制过来方便看源码，写注释
     * @see ArrayDeque#addLast(Object)
     */
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        // 将元素添加到tail处
        elements[tail] = e;
        // 然后tail指向下一个位置，如果队列满了，则调用doubleCapacity方法扩展数组。
        // tail的下一个位置是(tail+1) & (elements.length-1)，如果与head相同，则队列就满了。
        // 进行与操作保证了索引在正确范围，和(elements.length-1)相与就可以得到下一个正确位置，是因为elements.length是2的幂次方，
        // (elements.length-1)是奇数后几位全是1，无论是正数还是负数，和(elements.length-1)相与都能得到期望的下一个正确位置。
        if ( (tail = (tail + 1) & (elements.length - 1)) == head) {
            doubleCapacity();
        }
    }

    /**
     * 从头部添加：
     * ArrayDeque的addFirst方法，仅仅是复制过来方便看源码，写注释
     * @see ArrayDeque#addFirst(Object)
     */
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        // 在头部添加，要先让head指向前一个位置，然后再赋值给head所在位置。
        // head的前一个位置是(head-1) & (elements.length-1)。
        // 刚开始head为0，如果elements.length为8，则(head-1) & (elements.length-1)的结果为7。
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail) {
            // 数组扩容
            doubleCapacity();
        }
    }


    /**
     * 将数组扩大为原来的两倍
     */
    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        // number of elements to the right of p
        int r = n - p;
        // 新数组长度：原数组长度乘以2
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, deque too big");
        }
        // 分配一个长度翻倍的新数组a，将head右边的元素复制到新数组开头处，再复制左边的元素到新数组中，
        // 最后重新设置head和tail, head设为0, tail设为n。
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    /**
     * 从头部删除：
     * ArrayDeque的removeFirst方法，仅仅是复制过来方便看源码，写注释。
     * @see ArrayDeque#removeFirst()
     */
    public E removeFirst() {
        E x = pollFirst();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    /**
     * 从头部删除：
     * ArrayDeque的pollFirst方法，仅仅是复制过来方便看源码，写注释。
     * @see ArrayDeque#pollFirst()
     */
    public E pollFirst() {
        int h = head;
        @SuppressWarnings("unchecked")
        E result = (E) elements[h];
        // Element is null if deque empty
        if (result == null) {
            return null;
        }
        // 将原头部位置置为null，
        // 然后head置为下一个位置，下一个位置为(h+1) & (elements.length-1)。
        // 从尾部删除的代码是类似的。
        elements[h] = null;
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    /**
     * 查看队列中元素个数：
     * ArrayDeque的size方法，仅仅是复制过来方便看源码，写注释。
     * ArrayDeque没有单独的字段维护长度。
     *
     * @see ArrayDeque#size()
     */
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    /**
     * 检查给定元素是否存在：
     * ArrayDeque的contains方法，仅仅是复制过来方便看源码，写注释。
     * @see ArrayDeque#contains(Object)
     */
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        // 就是从head开始遍历并进行对比，循环过程中没有使用tail，而是到元素为null就结束了，
        // 这是因为在ArrayDeque中，有效元素不允许为null。

        int mask = elements.length - 1;
        int i = head;
        Object x;
        while ( (x = elements[i]) != null) {
            if (o.equals(x)) {
                return true;
            }
            i = (i + 1) & mask;
        }
        return false;
    }


    /**
     * toArray方法：
     * ArrayDeque的toArray方法，仅仅是复制过来方便看源码，写注释。
     * @see ArrayDeque#toArray()
     */
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }

    /**
     * 队列所有元素复制到数组a中
     */
    private <T> T[] copyElements(T[] a) {
        // 如果head小于tail，就是从head开始复制size个，
        // 否则，复制逻辑与doubleCapacity方法中的类似，先复制从head到末尾的部分，然后复制从0到tail的部分。

        if (head < tail) {
            System.arraycopy(elements, head, a, 0, size());
        } else if (head > tail) {
            int headPortionLen = elements.length - head;
            System.arraycopy(elements, head, a, 0, headPortionLen);
            System.arraycopy(elements, 0, a, headPortionLen, tail);
        }
        return a;
    }



}
