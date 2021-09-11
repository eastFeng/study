package com.dongfeng.study.basicstudy.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <b> 优先队列 </b>
 *
 * <p> 元素被赋予优先级
 * <p> 应该支持两种操作：删除最大元素和插入元素。如果允许重复元素，最大表示的是所有最大元素之一。
 * <p> 用数组保存元素并按照一定条件排序，以实现高效地（对数级别的）删除最大元素和插入元素操作。
 * <p> 数据结构二叉堆能够很好地实现优先队列的基本操作。在二叉堆的数组中，每个元素都要保证大于等于另两个特定位置的元素。
 * 相应地，这些位置的元素又至少要大于等于数组中的另两个元素，以此类推。
 * <p> 当一棵二叉树的每个结点都大于等于它的两个子结点时，它被称为堆有序。根结点是堆有序的二叉树中的最大结点。
 * <p> 在一个二叉堆中，位置k的结点的父结点的位置为[k/2]，而它的两个子结点的位置则分别为2k和2k+1。
 * <p> 一棵大小为N的完全二叉树的高度为[lgN]。通过归纳很容易可以证明这一点，且当N达到2的幂时树的高度会加1。
 * <p><b> 用长度为N+1的私有数组pq[]来表示一个大小为N的堆，我们不会使用pq[0]，堆元素放在pq[1]至pq[N]中。
 * （因为是泛型类，所以该类中用ArrayList代替数组。）
 *
 *
 * @author eastFeng
 * @date 2021-04-15 22:32
 */
public class PriorityQueue<T extends Comparable<T>> {

    // 优先队列由一个基于堆的完全二叉树表示，存储于数组pq[1..N]中，pq[0]没有使用
//    private Comparable<T>[] pq = (T[]) new Object[Integer.MAX_VALUE];

    // Raw use of parameterized class 'Comparable' : 参数化类“Comparable”的原始用法
    // 使用原生态类型会丢失泛型在安全性和表述性方面掉的所有优势。
//    private Comparable[] a = new Comparable[16];

    private final ArrayList<T> pq;

    PriorityQueue(){
        pq = new ArrayList<>(16);
        // List需要先add，然后才能set，如果没有add就set会抛java.lang.IndexOutOfBoundsException异常
        // 索引为0的位置没用到，设为null
        pq.add(null);
    }

    PriorityQueue(int capacity){
        pq = new ArrayList<>(capacity);
        // List需要先add，然后才能set，如果没有add就set会抛java.lang.IndexOutOfBoundsException异常
        // 索引为0的位置没用到，设为null
        pq.add(null);
    }

    public boolean isEmpty(){
        // 索引为0的位置没用到，但是设为了null
        return pq.isEmpty() || pq.size()==1;
    }

    public int size(){
        // 索引为0的位置没用到
        if (pq.size() <= 1){
            return 0;
        }
        return pq.size()-1;
    }

    /**
     * 插入元素
     *
     * @param t 要插入的元素
     */
    public void insert(T t){
        // 添加到最后一个位置
        pq.add(t);
        // 新添加到最后一个位置的元素需要上浮，恢复堆的有序性
        swim(pq.size()-1);
    }

    /**
     * 删除并返回优先队列中最大的元素
     *
     * @return 优先队列中最大的元素
     */
    public T delMax(){
        if (isEmpty()){
            return null;
        }

        // 从根节点得到最大元素
        T max = pq.get(1);
        // 将其和最后一个节点交换
        exchange(1, pq.size()-1);
        // 防止游离，为了GC
        pq.remove(pq.size()-1);
        // 将第一个节点下沉，恢复堆的有序性
        sink(1);
        return max;
    }

    /**
     * 遍历优先队列
     * 对优先队列的每个元素执行给定的操作，直到处理完所有元素或该操作引发异常为止。
     *
     * @param action 为每个元素执行的操作
     */
    public void foreach(Consumer<? super T> action){
        Objects.requireNonNull(action);
        if (pq==null || pq.isEmpty() || pq.size()==1){
            return;
        }
        for (int i=1; i<pq.size(); i++){
            action.accept(pq.get(i));
        }
    }

    /**
     * <b>由下至上的堆有序化（上浮）</b>
     *
     * <p> 如果堆的有序状态因为某个结点变得比它的父结点更大而被打破，那么我们就需要通过交换它和它的父结点来修复堆。
     * 交换后，这个结点比它的两个子结点都大（一个是曾经的父结点，另一个比它更小，因为它是曾经父结点的子结点），但这个结点仍然可能比它现在的父结点更大。
     * 我们可以一遍遍地用同样的办法恢复秩序，将这个结点不断向上移动直到我们遇到了一个更大的父结点或是到达了堆的顶部。
     * 只要记住位置k的结点的父结点的位置是[k/2]，这个过程实现起来很简单。
     *
     * <p> 该方法中的循环可以保证只有位置k上的结点大于它的父结点时堆的有序状态才会被打破。因此只要该结点不再大于它的父结点，堆的有序状态就恢复了。
     *
     * <p> 至于方法名，当一个结点太大的时候它需要浮（swim）到堆的更高层。
     *
     * @param k 优先队列中位置为k的元素要上浮
     */
    private void swim(int k){
        // k/2: 父节点索引
        while (k>1 && less(k/2, k)){
            // 子节点大于父节点，交换并继续和下一个父节点比较
            exchange(k, k/2);
            k = k/2;
        }
    }

    /**
     * <b>由上至下的堆有序化（下沉）</b>
     *
     * <p> 如果堆的有序状态因为某个结点变得比它的两个子结点或是其中之一更小了而被打破了，那么我们可以通过将它和它的两个子结点中的较大者交换来恢复堆。
     * 交换可能会在子结点处继续打破堆的有序状态，因此我们需要不断地用相同的方式将其修复，将结点向下移动直到它的子结点都比它更小或是到达了堆的底部。
     *
     * <p> 至于方法名，当一个结点太小的时候它需要沉（sink）到堆的更低层。
     *
     * @param k 优先队列中位置为k的元素要下沉
     */
    private void sink(int k){
        int size = pq.size()-1;
        while (2*k <= size){  // 当子节点存在时
            // 其中一个子节点索引（索引较小的）
            int j = 2*k;

            if (j<size && less(j, j+1)) {
                // 存在另一个子节点，并且另一个子节点是子节点中的较大者
                j++;
            }

            if (!less(k, j)) {
                // 该节点不小于最大子节点，跳出循环，下沉结束
                break;
            }

            // 该节点小于最大子节点，则交换并判断是否继续下沉
            exchange(k, j);
            k = j;
        }
    }

    /**
     * 有限列表上left位置上的元素是否小于right位置上的元素
     *
     * @param left 索引left
     * @param right 索引right
     * @return 小于返回true，否则返回false。
     */
    private boolean less(int left, int right){
        return pq.get(left).compareTo(pq.get(right)) < 0;
    }

    /**
     * 交换有限队列两个位置上的元素
     *
     * @param i 索引i
     * @param j 索引j
     */
    private void exchange(int i, int j){
        T temp = pq.get(i);
        pq.set(i, pq.get(j));
        pq.set(j, temp);
    }

    /**
     * 该优先队列是否有序（该有序指的是二叉堆有序）
     *
     * @return 有序返回true，否则返回false。
     */
    public boolean isSorted(){
        int listSize = pq.size();
        if (listSize == 0 || listSize == 1 || listSize==2){
            return true;
        }
        sink(2);
        int k = 1;
        while (2*k <= listSize-1){  // 存在子节点
            // 其中一个子节点索引（索引较小的）
            int j = 2*k;
            if (j<listSize-1 && less(j, j+1)){
                ++j;
            }

            if (less(k, j)){
                return false;
            }
            k++;
        }
        return true;
    }

    public static void main(String[] args) {
        // java不支持泛型类型的数组
//        Comparable<String>[] comparables = new Comparable<String>[12];

//        LinkedList<String> stringList = new LinkedList<>();
//
//        ArrayList<String> list = new ArrayList<>(16);
//        list.forEach(t->{
//            System.out.println("hhh"+t);
//        });
//        list.add(null);
//        list.add(null);
//        list.add(null);
//        list.add(null);
//        list.add(null);
//        list.set(1, "hh");
//        list.set(2, "jj");
//        list.set(3, "kk");
//        System.out.println("size: "+list.size());
//        for (int i=0; i< list.size(); i++){
//            System.out.println("index: "+i+ " ,,,,, value: "+list.get(i));
//        }

    }



}
