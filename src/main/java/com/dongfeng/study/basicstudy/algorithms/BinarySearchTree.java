package com.dongfeng.study.basicstudy.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;

/**
 * <b> 二叉查找树（也叫二叉排序树、二叉搜索树） </b>
 *
 * <p> 在二叉树中，每个结点只能有一个父结点（只有一个例外，也就是根结点，它没有父结点），而且每个结点都只有左右两个链接，分别指向自己的左子结点和右子结点。
 * 尽管链接指向的是结点，但我们可以将每个链接看做指向了另一颗二叉树，而这颗树的根结点就是被指向的结点。因此我们可以将二叉树第那个以为一个空连接，
 * 或者是一个有左右两个链接的节点，每个链接都指向一颗（独立的）子二叉树。在二叉查找树中个，每个节点还包含一个键和一个值，键之间也有顺序之分以支持高效的查找。
 *
 * <p> 一颗二叉查找树（BST）是一颗二叉树，其中每个节点都包含一个Comparable的键（以及相关联的值）。
 * <b> 且每个节点的键都大于其左子树中的任意节点的键而小于右子树中的任意节点的键。</b>
 *
 * <p> 根据数据表示的递归结构我们马上就能得到，在二叉查找树中查找一个键的递归算法：
 * 如果树是空的，则查找未命中；如果被查找的键和根结点的键相等，查找命中，否则我们就（递归地）在适当的子树中继续查找。
 * 如果被查找的键较小就选择左子树，较大则选择右子树。
 *
 * <p> 使用二叉查找树的算法的运行时间取决于树的形状，而树的形状又取决于键被插入的先后顺序。
 * 在最好的情况下，一棵含有N个结点的树是完全平衡的，每条空链接和根结点的距离都为～lgN。
 * 在最坏的情况下，搜索路径上可能有N个结点。
 *
 * <p>二叉查找树的性质：
 * <ol>
 *     <li> 若左子树不为空，则左子树上所有结点的值均小于它的根结点的值；
 *     <li> 若右子树不为空，则右子树上所有结点的值均大于它的根结点的值；
 *     <li> 左、右子树也分别为二叉查找树；
 * </ol>
 *
 *
 * @author eastFeng
 * @date 2021-04-16 20:49
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    /**
     * 二叉查找树节点
     *
     * @param <K>
     * @param <V>
     */
    private static class Node<K extends Comparable<K>, V>{
        // 键
        private K key;
        // 值
        private V value;
        // 指向左子树的左链接
        private Node<K,V> left;
        // 指向右子树的右链接
        private Node<K,V> right;
        // 以该节点为根节点的子树中的节点的总数
        // count = x.left.count + x.right.count
        private int count;

        public Node(K key, V value, int count) {
            this.key = key;
            this.value = value;
            this.count = count;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue){
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        @Override
        public String toString(){
            return key + "=" + value;
        }

        @Override
        public int hashCode(){
            return Objects.hash(key, value);
        }
        @Override
        public boolean equals(Object obj){
            if (this == obj){
                return true;
            }
            if (obj instanceof Node){
                Node<?, ?> anotherNode = (Node<?, ?>) obj;
                return Objects.equals(key, anotherNode.key) && Objects.equals(value, anotherNode.value);
            }
            return true;
        }
    }


    /**
     * 二叉查找树的根节点
     */
    private transient Node<K,V> root;

    /**
     * 返回二叉树中个节点的个数
     */
    public int size(){
        return size(root);
    }

    /**
     * 获取key对应的value
     *
     * @param key 键
     * @return 值
     */
    public V get(K key){
        Node<K,V> e;
        return (e=getNode(root, key)) == null ? null : e.value;
    }

    /**
     * 插入键值对
     *
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value){
        root = putNode(root, key, value);
    }

    /**
     * 插入键值对的非递归实现 --- 推荐
     *
     * @param key 键
     * @param value 值
     * @return 键对应的旧值，如果没有则为null
     */
    private V put2(K key, V value){
        if (key == null){
            throw new NullPointerException();
        }

        Node<K, V> t = this.root;
        if (t == null){
            compare(key, key); // 类型检查

            root = new Node<>(key, value, 1);
            return null;
        }

        Node<K, V> parent;
        int cmp;
        do {
            // 下一个t节点的父节点
            parent = t;

            cmp = key.compareTo(t.key);
            if (cmp < 0){
                t = t.left;
            }else if (cmp > 0){
                t = t.right;
            }else {
                // 命中
                return t.setValue(value);
            }
        }while (t != null);

        // 走到这里说明没找到，需要parent节点下面插入新节点。parent有个子节点是为空的。
        Node<K, V> newNode = new Node<>(key, value, 1);
        if (cmp < 0){
            // 比
            parent.left = newNode;
        }else {
            parent.right = newNode;
        }
        return null;
    }

    /**
     * 获取二叉树中的最小节点
     */
    public Node<K,V> min(){
        return getMin(root);
    }

    /**
     * 获取二叉树中的最大节点
     */
    public Node<K,V> max(){
        return getMax(root);
    }

    public K select(int n){
        Node<K, V> select = select(root, n);
        return select == null ? null : select.key;
    }

    public int rank(K key){
        return rank(root, key);
    }

    /**
     * 删除最小节点
     */
    public void deleteMin(){
        root = deleteMin(root);
    }

    /**
     * 删除最大节点
     */
    public void deleteMax(){
        root = deleteMax(root);
    }

    /**
     * 删除键为key的节点
     * @param key 键key
     */
    public void delete(K key){
        root = delete(root, key);
    }

    /**
     * 前序遍历
     * @param action 为二叉查找树每个节点执行的操作
     */
    public void preOrderTraversal(BiConsumer<? super K, ? super V> action){
        preOrderTraversal(root, action);
    }

    /**
     * 中序遍历
     * @param action 为二叉查找树每个节点执行的操作
     */
    public void inOrderTraversal(BiConsumer<? super K, ? super V> action){
        inOrderTraversal(root, action);
    }

    /**
     * 后序遍历
     * @param action 为二叉查找树每个节点执行的操作
     */
    public void postOrderTraversal(BiConsumer<? super K, ? super V> action){
        postOrderTraversal(root, action);
    }

    /**
     * 范围查找在[lo, hi]之内的节点
     *
     * @param lo key最小值
     * @param hi key最大值
     */
    public List<Node<K,V>> rangeLookup(K lo, K hi){
        List<Node<K,V>> list = new ArrayList<>(16);
        rangeLookup(root, list, lo, hi);
        return list;
    }



    //--------------------------------------------------------private method-------------------------------------------//

    /**
     * 从根节点node中查找key
     *
     * @param node 根节点
     * @param key 键
     * @return Node<K,V>
     */
    private Node<K,V> getNode(Node<K,V> node, K key){
        if (key == null){
            throw new NullPointerException();
        }

        // 非递归方式
        while (node != null){
            int cmp = key.compareTo(node.key);
            if (cmp < 0){
                // 左子树
                node = node.left;
            }else if (cmp > 0){
                // 右子树
                node = node.right;
            }else {
                return node;
            }
        }
        return null;

        // 递归方式实现，不推荐。
//        if (node != null){
//            int compareTo = key.compareTo(node.key);
//
//            if (compareTo < 0){
//                // 左子树
//                return getNode(node.left, key);
//            }else if (compareTo > 0){
//                // 右子树
//                return getNode(node.right, key);
//            }else {
//                // 当前节点
//                return node;
//            }
//        }
//        return null;
    }

    final int compare(Object k1, Object k2) {
        return ((Comparable<? super K>)k1).compareTo((K)k2);
    }

    /**
     * 在节点node插入键值对
     *
     * @param node 节点
     * @param key 键
     * @param value 值
     * @return 键key对应的旧值
     */
    private Node<K,V> putNode(Node<K,V> node, K key, V value){

        if (node == null){
            return new Node<K, V>(key, value, 1);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0){
            // 插入左子树
            node.left = putNode(node.left, key, value);
        }else if(cmp > 0) {
            // 插入右子树
            node.right = putNode(node.right, key, value);
        }else {
            // 当前节点，更新value
            node.value = value;
        }
        // 重新计算二叉树节点数目
        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }

    /**
     * 获取二叉树node的节点的个数
     */
    private int size(Node<K,V> node){
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    /**
     * 获取二叉树node中的最小节点
     */
    private Node<K,V> getMin(Node<K,V> node){

        // 非递归实现
//        while (node != null){
//            if (node.left == null){
//                // 左链接（左子树、左子节点）为null，则父节点就是最小节点
//                return node;
//            }
//            node = node.left;
//        }
//        return null;

        if (node==null || node.left == null){
            // 左链接（左子树、左子节点）为null，则父节点就是最小节点
            return node;
        }
        // 左链接（左子树、左子节点）不为null，返回左子树的最小节点
        return getMin(node.left);
    }

    /**
     * 获取二叉树node中的最大节点
     */
    private Node<K,V> getMax(Node<K,V> node){
        while (node != null){
            if (node.right == null){
                // 右链接（右子树、右子节点）为null，则父节点就是最大节点
                return node;
            }
            node = node.right;
        }
        return null;
    }

    /**
     * 向下取整函数 : 小于等于key的最大键
     *
     * <p> 如果给定的键key小于二叉查找树的根结点的键，那么小于等于key的最大键floor(key)一定在根结点的左子树中；
     * 如果给定的键key大于二叉查找树的根结点，那么只有当根结点右子树中存在小于等于key的结点时，小于等于key的最大键才会出现在右子树中，
     * 否则根结点就是小于等于key的最大键。
     */
    private Node<K,V> floor(Node<K,V> node, K key){
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node;
        }else if (cmp < 0) {
            return floor(node.left, key);
        }

        // cmp > 0 : 键key大于结点node
        // 只有当node节点的右子树中存在小于等于key的节点时，
        // 小于等于key的最大键才会出现在右子树中，否则node就是小于等于key的最大键。
        Node<K, V> t = floor(node.right, key);
        if (t != null){
            return t;
        }else {
            return node;
        }
    }

    // 找到排名为k的键（即树中正好有k个小于它的键）

    /**
     * 找到排名为k的键（即树中正好有k个小于它的键）
     * <p>
     * 如果左子树中的结点数t大于k，那么我们就继续（递归地）在左子树中查找排名为k的键；
     * 如果t等于k，我们就返回根结点中的键；
     * 如果t小于k，我们就（递归地）在右子树中查找排名为（k-t-1）的键。
     */
    private Node<K,V> select(Node<K,V> node, int k){
        if (node == null) {
            return null;
        }

        int size = size(node.left);
        if (size > k) return select(node.left, k);
        else if (size < k) return select(node.right, k-size-1);
        else return node;
    }


    /**
     * 返回以node为根节点的二叉查找树树中小于key键的数量
     */
    private int rank(Node<K,V> node, K key){
        if (node == null) {
            return 0;
        }

        int cmp = key.compareTo(node.key);
        // 在节点node的左子树中寻找key的排名
        if (cmp < 0) return rank(node.left, key);
        else if (cmp > 0) return 1 + size(node.left) + rank(node.right, key);
        else return size(node.left);
    }

    /**
     * 删除最小元素
     * <p> 要不断深入根结点的左子树中直至遇见一个空链接，然后将指向该结点的链接指向该结点的右子树（只需要在递归调用中返回它的右链接即可）。
     * 此时已经没有任何链接指向要被删除的结点，因此它会被垃圾收集器清理掉。
     */
    private Node<K,V> deleteMin(Node<K,V> node){
        if (node == null) {
            // 根节点为null，没有节点可删除
            return null;
        }
        if (node.left == null) {
            // 左子树为null，则根节点node为最小节点，需要删除
            // 返回该节点的右子树（右子结点）即可
            return node.right;
        }
        // 左子树不为null，则最小元素在左子树中，删除左子树中的最小元素
        node.left = deleteMin(node.left);
        // 重新计算节点数
        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }

    /**
     * 删除最大元素
     */
    private Node<K,V> deleteMax(Node<K,V> node){
        if (node == null){
            return null;
        }
        if (node.right == null){
            return node.left;
        }

        // 右子树不为null
        node.right = deleteMax(node.right);
        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }

    /**
     * 在删除结点x后用它的后继结点填补它的位置。因为x有一个右子结点，因此它的后继结点就是其右子树中的最小结点。
     * 这样的替换仍然能够保证树的有序性，因为x.key和它的后继结点的键之间不存在其他的键。
     * <p> 我们能够用4个简单的步骤完成将x替换为它的后继结点的任务：
     * <ol>
     *     <li> 将指向即将被删除的结点的链接保存为t；
     *     <li> 将x指向它的后继结点min(t.right)；
     *     <li> 将x的右链接（原本指向一棵所有结点都大于x.key的二叉查找树）指向deleteMin(t. right)，
     *          也就是在删除后所有结点仍然都大于x.key的子二叉查找树；
     *     <li> 将x的左链接（本为空）设为t.left（其下所有的键都小于被删除的结点和它的后继结点）。
     * </ol>
     * @param node
     * @param key 要删除的key
     * @return
     */
    private Node<K,V> delete(Node<K,V> node, K key){
        if (node == null || key==null){
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0){
            node.left = delete(node.left, key);
        }else if (cmp > 0){
            node.right = delete(node.right, key);
        }else {
            // 如果左右子节点都为null，则返回null（删除自己后啥也没有了）
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // 走到这里说明左右子节点都不为null

            // 将指向即将被删除的节点的链接保存为t
            Node<K, V> t = node;
            // 将node指向它的后继节点getMin(t.right)
            node = getMin(t.right);
            // 将node的右连接（原本指向一颗所有节点都大于node.key的二叉查找树）指向deleteMin(t.right)
            // 也就是在删除后所有节点仍然都大于node.key的子二叉查找树
            node.right = deleteMin(t.right);
            // 将node的左连接（本为null）设为t.left（其下所有的键都小于被删除的节点和它的后继节点）
            node.left = t.left;
        }
        node.count = size(node.left) + size(node.right) + 1;
        return node;
    }
    /**
     * <b> 前序遍历 </b>
     * 前序遍历首先访问根结点然后遍历左子树，最后遍历右子树。在遍历左、右子树时，仍然先访问根结点，然后遍历左子树，最后遍历右子树。
     * <ol>
     *     <li> 访问根结点。
     *     <li> 前序遍历左子树。
     *     <li> 前序遍历右子树。
     * </ol>
     * @param node 要前序遍历的二叉树
     * @param action 为每个节点执行的操作
     */
    private void preOrderTraversal(Node<K,V> node, BiConsumer<? super K, ? super V> action){
        // 递归版
//        if (node != null){
//            // 访问根结点
//            action.accept(node.key, node.value);
//            // 前序遍历左子树
//            preOrderTraversal(node.left, action);
//            // 前序遍历右子树
//            preOrderTraversal(node.right, action);
//        }

        /*
         * 前序遍历非递归版：
         * 1. 申请一个空栈，将根结点node压入栈中。
         * 2. 弹出栈顶元素，因为是先序遍历，所以直接输出这个栈顶元素。因为先序遍历的遍历顺序是：根-->左-->右，
         *    但是因为栈是先进后出，所以应该先推入右子结点，再推入左子结点(如果有左子结点和右子结点的话)。
         * 3. 不断重复第2步，直到栈为空。
         */
        Stack<Node<K,V>> stack = new Stack<>();
        if (node != null){
            stack.push(node);
            while (!stack.empty()){
                // 先访问根节点
                Node<K, V> top = stack.pop();
                action.accept(top.key, top.value);
                // 先推入右子结点，再推入左子结点
                if (top.right != null){
                    stack.push(top.right);
                }
                if (top.left != null){
                    stack.push(top.left);
                }
            }
        }
    }


    /**
     * <b> 中序遍历 </b>
     * 中序遍历首先遍历左子树，然后访问根结点，最后遍历右子树。
     * <ol>
     *     <li> 中序遍历左子树
     *     <li> 访问根结点
     *     <li> 中序遍历右子树
     * </ol>
     * @param node 要中序遍历的二叉树
     * @param action 为每个节点执行的操作
     */
    private void inOrderTraversal(Node<K,V> node, BiConsumer<? super K, ? super V> action){
        // 递归版
//        if (node != null){
//            // 中序遍历左子树
//            inOrderTraversal(node.left, action);
//            // 访问根节点
//            action.accept(node.key, node.value);
//            // 中序遍历右子树
//            inOrderTraversal(node.right, action);
//        }

        /*
         * 中序遍历的非递归算法的关键：在中序遍历过某节点的整个左子树后，如何找到该节点的跟以及右子树。
         * 非递归基本思想:
         * 1. 建立一个栈
         * 2. 根节点进栈，（中序）遍历左子树
         * 3. 根节点出栈，输出根节点，（中序）遍历右子树
         */

        /*
         * 中序遍历非递归版 : 使用栈
         *
         * 1. 申请一个空栈。
         * 2. 如果当前结点node不为空，就将结点进栈，然后让node变为node结点的左子结点。
         *    如果当前结点node为空，就弹出栈顶元素并打印，再让当前结点node变为弹出的栈顶元素的右子结点。
         * 3. 不断重复第2步，直到栈空并且当前结点为空的时候。
         */
        Stack<Node<K,V>> stack = new Stack<>();
        while (node!=null || !stack.empty()){
            if (node != null){
                // 节点不为null，先入栈，去访问它的左子树
                stack.push(node);
                node = node.left;
            }else {
                // 节点为null，弹出栈顶元素（该节点的根节点），然后访问右子树
                Node<K, V> top = stack.pop();
                action.accept(top.key, top.value);
                node = top.right;
            }
        }

        // 中序遍历非递归版本二
        //        while (node!=null || stack.empty()){
//            while (node != null){ // 这里一直while循环到最左边结点
//                stack.push(node);
//                node = node.left;
//            }
//            Node<K, V> top = stack.pop();
//            action.accept(top.key, top.value);
//            node = top.right;
//        }
    }


    /**
     * <b> 后序遍历 </b>
     * 后序遍历首先遍历左子树，然后遍历右子树，最后访问根结点，在遍历左、右子树时，仍然先遍历左子树，然后遍历右子树，最后遍历根结点。
     * <ol>
     *     <li> 后序遍历左子树。
     *     <li> 后序遍历右子树。
     *     <li> 访问根结点。
     * </ol>
     * @param node 要后序遍历的二叉树
     * @param action 为每个节点执行的操作
     */
    private void postOrderTraversal(Node<K,V> node, BiConsumer<? super K, ? super V> action){
//        // 后续遍历递归版
//        if (node != null){
//            // 后序遍历左子树
//            postOrderTraversal(node.left, action);
//            // 后序遍历右子树
//            postOrderTraversal(node.right, action);
//            // 访问根结点
//            action.accept(node.key, node.value);
//        }

        /*
         * 后序遍历的非递归算法是三种顺序中最复杂的，原因在于，后序遍历是先访问左、右子树,再访问根节点，而在非递归算法中，
         * 利用栈回退时，并不知道是从左子树回退到根节点，还是从右子树回退到根节点，如果从左子树回退到根节点，此时就应该去访问右子树，
         * 而如果从右子树回退到根节点，此时就应该访问根节点。所以相比前序和后序，必须得在压栈时添加信息，以便在退栈时可以知道是从左子树返回，
         * 还是从右子树返回进而决定下一步的操作。
         *
         * 添加信息的方法：创建一个辅助栈，当左子树的点压入，辅助栈压入1，当右子树的点压入，辅助栈压入2
         */

        Stack<Node<K,V>> stack = new Stack<>();
        // 标记结点的右子树是否访问过
        Node<K,V> r = null;
        while(!stack.isEmpty() || node != null){
            if (node != null) {
                stack.push(node);
                node = node.left;
            }else{
                node = stack.peek();
                // 判断结点右子树状态，是空或者已经访问过，满足就可以访问根结点。
                if(node.right == null || node.right == r){
                    action.accept(node.key, node.value);
                    stack.pop();
                    r = node;
                    node = null;
                }else {
                    node = node.right;
                }
            }
        }

    }

    /**
     * 范围查找在[lo, hi]之内的节点
     * @param node 二叉树
     * @param list 存放范围查找结果的集合
     * @param lo key最小值
     * @param hi key最大值
     */
    private void rangeLookup(Node<K,V> node, List<Node<K,V>> list, K lo, K hi){
        if (node == null){
            return;
        }

        int cmpLo = lo.compareTo(node.key);
        int cmpHi = hi.compareTo(node.key);

        if (cmpLo<=0 && cmpHi>=0){
            // lo比当前节点小并且hi比当前节点大
            list.add(node);
        }

        // 到这里，上面两个条件至少有一个不满足

        if (cmpLo < 0){
            // lo和hi都比当前节点小，在当前节点左子树中寻找
            rangeLookup(node.left, list, lo, hi);
        }
        if (cmpHi > 0){
            // lo和hi都比当前节点大，在当前节点右子树中寻找
            rangeLookup(node.right, list, lo, hi);
        }
    }


    /**
     * 测试node是否是有序的、正确的二叉查找树
     */
    private boolean isSorted(Node<K,V> node){
        if (node==null || node.count==1){
            return true;
        }

        return false;
    }










}
