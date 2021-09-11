package com.dongfeng.study.basicstudy.collection;

import java.util.*;

/**
 * <b> {@link java.util.LinkedList}学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-24 3:31
 */
public class LinkedListStudy<E> {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        Integer integer = list.get(1);
        list.indexOf(1);
        list.addFirst(3);
        list.add(1, 2);
        list.remove(0);
        Iterator<Integer> iterator = list.iterator();
        Iterator<Integer> integerIterator = list.descendingIterator();
        ListIterator<Integer> integerListIterator = list.listIterator();

        // LinkedList的用法
        usage();
        // LinkedList的基本原理
        basePrinciple();

        LinkedListStudy<Integer> study = new LinkedListStudy<>();
        // LinkedList add方法源码
        study.add(1);
        // LinkedList get方法源码
        study.get(0);
        // LinkedList indexOf方法源码
        int index = study.indexOf(1);
        // LinkedList add方法源码
        study.add(0, 3);
        // LinkedList remove方法源码
        study.remove(0);

        // LinkedList各个方法的时间复杂度和特点分析
        character();
    }

    /**
     * LinkedList 用法
     */
    public static void usage(){
        /*
         * LinkedList和ArrayList一样，同样实现了List接口，而List接口扩展了Collection接口，Collection接口又扩展了Iterable接口。
         * 所有这些接口的方法都是可以使用的。
         *
         * LinkedList还实现了队列接口Queue，【所谓队列就是先进先出，在尾部添加元素，从头部删除元素。】
         *
         * Queue扩展了Collection，它的主要操作有三个：
         * 1. 在尾部添加元素（add、offer）；
         * 2. 查看头部元素（element、peek），返回头部元素，但不改变队列；
         * 3. 删除头部元素（remove、poll），返回头部元素，并且从队列中删除。
         *
         * 每种操作都有两种形式，有什么区别呢？区别在于，对于特殊情况的处理不同。
         * 特殊情况是指队列为空或者队列为满，为空容易理解，为满是指队列有长度大小限制，而且已经占满了。
         * LinkedList的实现中，队列长度没有限制，但别的Queue的实现可能有。
         * 在队列为空时，element和remove会抛出异常NoSuchElementException，而peek和poll返回特殊值null；
         * 在队列为满时，add会抛出异常IllegalStateException，而offer只是返回false。
         *
         * 【栈也是一种常用的数据结构，与队列相反，它的特点是先进后出、后进先出。】
         * Java中没有单独的栈接口，栈相关方法包括在了表示双端队列的接口Deque中，主要有三个方法：
         * 1. void push(E e);
         *    push表示入栈，在头部添加元素，栈的空间可能是有限的，如果栈满了，push会抛出异常IllegalStateException。
         * 2. E pop();
         *    pop表示出栈，返回头部元素，并且从栈中删除，如果栈为空，会抛出异常NoSuch-ElementException。
         * 3. E peek();
         *    peek查看栈头部元素，不修改栈，如果栈为空，返回null。
         *
         * peek/piːk/  : 看一眼，瞥一眼。
         * LinkedList实现了Deque接口。
         *
         * Java中有一个类Stack，单词意思是栈，它也实现了栈的一些方法，如push/pop/peek等，但它没有实现Deque接口，它是Vector的子类，
         * 它增加的这些方法也通过synchronized实现了线程安全，具体就不介绍了。
         * 不需要线程安全的情况下，推荐使用LinkedList或ArrayDeque。
         *
         * 栈和队列都是在两端进行操作，栈只操作头部；队列两端都操作，但尾部只添加、头部只查看和删除。
         * 有一个更为通用的操作两端的双端队列接口Deque。
         * Deque扩展了Queue，包括了栈的操作方法，此外，它还有如下更为明确的操作两端的方法：
         * void addFirst(E e)
         * void addLast(E e)
         * E getFirst()
         * E getLast()
         * boolean offerFirst(E e)
         * boolean offerLast(E e)
         * E peekFirst()
         * E peekLast()
         * ...
         *
         * xxxFirst操作头部，xxxLast操作尾部。与队列类似，每种操作有两种形式，区别也是在队列为空或满时处理不同。
         * 为空时，getⅩⅩⅩ/removeⅩⅩⅩ会抛出异常，而peekⅩⅩⅩ/pollⅩⅩⅩ会返回null。
         * 队列满时，addⅩⅩⅩ会抛出异常，offerⅩⅩⅩ只是返回false。
         *
         * 【栈和队列只是双端队列的特殊情况，它们的方法都可以使用双端队列的方法替代，不过，使用不同的名称和方法，概念上更为清晰。】
         *
         * Deque接口还有一个迭代器方法，可以从后往前遍历：
         * Iterator<E> descendingIterator();
         *
         * 简单总结下：LinkedList的用法是比较简单的，与ArrayList用法类似，支持List接口，
         * 只是，LinkedList增加了一个接口Deque，可以把它看作队列、栈、双端队列，方便地在两端进行操作。
         * 如果只是用作List，那应该用ArrayList还是LinkedList呢？我们需要了解LinkedList的实现原理。
         */

        // 把LinkedList当作Queue使用也很简单，比如，可以这样：
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        while (queue.peek()!=null){
            System.out.println(queue.poll());
        }

        // 把LinkedList当作栈使用也很简单，比如，可以这样：
        Deque<Integer> stack = new LinkedList<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        while (stack.peek()!=null){
            System.out.println(stack.pop());
        }

        // Deque接口还有一个迭代器方法，可以从后往前遍历：
        Deque<String> deque = new LinkedList<>(Arrays.asList("a", "b", "c"));
        Iterator<String> it = deque.descendingIterator();
        while (it.hasNext()){
            System.out.print(it.next()+" ");
        }
    }

    /**
     * LinkedList 基本原理
     */
    public static void basePrinciple(){
        /*
         * 内部实现
         * ArrayList内部是数组，元素在内存是连续存放的，但LinkedList不是。
         * LinkedList直译就是链表，确切地说，它的内部实现是双向链表，每个元素在内存都是单独存放的，元素之间通过链接连在一起。
         * 每个对象（元素）存放在独立的结点中。每个结点存放着序列中下一个结点的引用和指向前驱结点的引用。
         *
         * LinkedList有一个内部类Node表示链表的节点。
         * LinkedList内部组成就是如下三个实例变量：
         * transient int size = 0;    // size表示链表长度，默认为0
         * transient Node<E> first;   // first指向头节点，last指向尾节点，初始值都为null
         * transient Node<E> last;
         * LinkedList的所有public方法内部操作的都是这三个实例变量。
         *
         * 需要看的方法源码：add，remove，indexOf，remove
         */
    }

    /**
     * LinkedList各个方法的时间复杂度和特点分析
     */
    public static void character(){
        /*
         * 用法上，LinkedList是一个List，但也实现了Deque接口，可以作为队列、栈和双端队列使用。
         * 实现原理上，LinkedList内部是一个双向链表，并维护了长度、头节点和尾节点，这决定了它有如下特点：
         *
         * 1. 按需分配空间，不需要预先分配很多空间。
         * 2. 随机访问效率低，按照索引位置访问效率比较低，必须从头或尾顺着链接找，效率为O(N/2)。
         * 3. 不管列表是否已排序，只要是按照内容查找元素，效率都比较低，必须逐个比较，效率为O(N)。
         * 4. 在两端添加、删除元素的效率很高，为O(1)。
         * 5. 在中间插入、删除元素，要先定位，效率比较低，为O(N)，但修改本身的效率很高，效率为O(1)。
         *
         * 如果需要对集合进行随机访问，就使用数组或ArrayList，而不要使用链表。
         */

        /*
         * addFirst() 添加队列头部，复杂度 O(1)
         *
         * removeFirst() 删除队列头部，复杂度 O(1)
         *
         * addLast() 添加队列尾部，复杂度 O(1)
         *
         * removeLast() 删除队列尾部，复杂度 O(1)
         *
         * getFirst() 获取队列头部，复杂度 O(1)
         *
         * getLast() 获取队列尾部，复杂度 O(1)
         *
         * get() 获取第n个元素，依次遍历，复杂度O(n)
         *
         * add(E) 添加到队列尾部，复杂度O(1)
         *
         * add(index, E) 添加到第n个元素后，需要先查找到第n个元素，复杂度O(n)
         *
         * remove() 删除元素，修改前后元素节点指针，复杂度O(1)
         */
    }


    // 链表长度，默认为0
    transient int size = 0;
    // first指向头节点，last指向尾节点，初始值都为null。
    transient Node<E> first;
    transient Node<E> last;
    // 和ArrayList一样，内部的修改次数
    private int modCount = 0;

    /**
     * add方法：
     * LinkedList的add方法，仅仅是复制过来方便看源码，写注释。
     * @see LinkedList#add(Object)
     */
    public boolean add(E e) {
        // 主要就是调用了linkLast方法
        linkLast(e);
        return true;
    }

    void linkLast(E e) {
        // 1. 创建一个新的节点newNode。l和last指向原来的尾节点，如果原来链表为空，则为null
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        // 2. 修改尾节点last，指向新的最后节点newNode
        last = newNode;
        // 3. 修改前节点的后向链接，如果原来的链表为空，则让头节点指向新节点，否则让前一个节点的next指向新节点
        if (l == null) {
            // 前一个节点为空（原来的链表为空）
            first = newNode;
        } else {
            l.next = newNode;
        }
        // 4. 增加链表大小
        size++;
        // modCount++的目的与ArrayList中是一样的，记录修改次数，便于迭代中间检测结构性变化。
        modCount++;

        // 可以看出，与ArrayList不同，Linked-List的内存是按需分配的，不需要预先分配多余的内存，
        // 添加元素只需分配新元素的空间，然后调节几个链接即可。
    }

    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * 根据索引访问元素：
     * LinkedList的get方法，仅仅是复制过来方便看源码，写注释。
     * @see LinkedList#get(int)
     */
    public E get(int index) {
        // 检查索引位置的有效性，如果无效，则抛出异常
        checkElementIndex(index);
        // 调用node方法查找对应的节点
        return node(index).item;
    }

    /**
     * 查找指定索引处的节点
     */
    Node<E> node(int index) {
        Node<E> x;

        // size>>1 等于 size/2，如果索引位置在前半部分，则从头节点开始查找；否则，从尾节点开始查找
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++) {
                // 每次往后查找一个i就加一，i=index的时候找到
                x = x.next;
            }
        } else {
            x = last;
            // size-1是最后一个节点的索引，i=index的时候找到
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
        }
        return x;

        // 可以看出，与ArrayList明显不同，ArrayList中数组元素连续存放，可以根据索引直接定位，
        // 而在LinkedList中，则必须从头或尾顺着链接查找，效率比较低。
    }

    /**
     * 根据内容查找索引：
     * LinkedList的indexOf方法，仅仅是复制过来方便看源码，写注释。
     * @see LinkedList#indexOf(Object)
     */
    public int indexOf(Object o) {
        // 从头节点顺着链接往后找，如果要找的是null，则找第一个item为null的节点，否则使用equals方法进行比较。
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    /**
     * 指定位置插入元素：
     * LinkedList的add方法，仅仅是复制过来方便看源码，写注释。
     * @see LinkedList#add(int, Object)
     */
    public void add(int index, E element) {
        // 检查索引位置的有效性，如果无效，则抛出异常
        checkPositionIndex(index);

        if (index == size) {
            // 如果index为size，添加到最后面
            linkLast(element);
        } else {
            // 否则，插入到index对应节点的前面
            // 通过node方法找到索引为index的节点
            linkBefore(element, node(index));
        }
    }

    /**
     * 在节点succ前面插入元素e
     */
    void linkBefore(E e, Node<E> succ) {
        // 参数succ表示新插入节点的后继节点，变量pred表示新插入节点的前驱节点。
        // 新节点插入在pred和succ中间。
        final Node<E> pred = succ.prev;
        // 新建一个节点newNode，前驱节点为pred，后继节点为succ。
        final Node<E> newNode = new Node<>(pred, e, succ);
        // 让后继节点的前驱指向新节点。
        succ.prev = newNode;
        // 让前驱节点的后继指向新节点。如果前驱节点为空，那么修改头节点指向新节点。
        if (pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }
        // 增加长度
        size++;
        modCount++;

        // 可以看出，在中间插入元素，LinkedList只需按需分配内存，修改前驱和后继节点的链接，
        // 而ArrayList则可能需要分配很多额外空间，且移动所有后续元素。
    }

    /**
     * 删除指定位置的元素：
     * LinkedList的remove方法，仅仅是复制过来方便看源码，写注释。
     * @see LinkedList#remove(int)
     */
    public E remove(int index) {
        // 检查索引
        checkElementIndex(index);
        // 通过node方法找到节点后，调用了unlink方法
        return unlink(node(index));
    }

    /**
     * 删除节点x
     */
    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        // 删除x节点，基本思路就是让x的前驱和后继直接链接起来，
        // next是x的后继，prev是x的前驱，具体分为两步。

        // 1. 让x的前驱节点的后继指向x的后继节点。如果x没有前驱节点，说明删除的是头节点，则修改头节点指向x的后继节点。
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        // 2. 让x的后继节点的前驱指向x的前驱节点。如果x没有后继节点，说明删除的是尾节点，则修改尾节点指向x的前驱节点。
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        // 释放引用以便原对象垃圾回收
        x.item = null;
        // 元素个数减一
        size--;
        modCount++;
        return element;
    }


    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }
    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }


    /**
     * 链表中的节点
     */
    private static class Node<E> {
        // 元素
        E item;
        // 当前节点的下一个节点，后继节点
        Node<E> next;
        // 当前节点的上一个节点，前驱节点
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}
