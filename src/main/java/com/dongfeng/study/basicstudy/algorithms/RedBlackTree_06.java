package com.dongfeng.study.basicstudy.algorithms;

/**
 * <b> 红黑树（Red Black Tree）是一种自平衡二叉查找树 </b>
 *
 * <p> 红黑二叉查找树背后的基本思想是用标准的二叉查找树（完全由2-节点构成）和一些额外的信息（替换3-节点）来表示2-3树。我们将树中的链接分为两种类型：
 * 红链接将两个2-节点链接将两个2-节点连接起来构成一个3-节点，黑链接则是2-3树中的普通链接。
 * 确切地说，我们将3-节点表示为由一条左斜的红色链接（两个2-节点其中之一是另一个的左子节点）相连的两个2-节点。
 * 这种表示法的一个优点是，无需修改就可以直接使用标准二叉查找树的get方法。对于任意的2-3树，只要对节点进行转换，都可以立即派生出一颗对应的二叉查找树。
 * 我们将用这种方式表示2-3树的二叉查找树称为红黑二叉查找树（简称红黑树）。
 *
 * <p> 红黑树的另一种定义是含有红黑链接并满足下列条件的二叉查找树：
 * <ol>
 *     <li> 红链接均为左链接；
 *     <li> 没有任何一个结点同时和两条红链接相连；
 *     <li> 该树是完美黑色平衡的，即任意空链接到根结点的路径上的黑链接数量相同。
 * </ol>
 *
 * <p> 一棵大小为N的红黑树的高度不会超过2lgN。
 *
 * @author eastFeng
 * @date 2021-04-18 20:05
 */
public class RedBlackTree_06<K extends Comparable<K>,V> {

    // 红链接
    private static final boolean RED   = true;
    // 黑链接
    private static final boolean BLACK = false;

    /**
     * 表示红黑树的节点类
     */
    private static class Node<K extends Comparable<K>,V>{
        // 键
        private K key;
        // 值
        private V value;
        // 左连接
        private Node<K,V> left;
        // 右链接
        private Node<K,V> right;
        // 这颗子树中的节点总数
        private int size;
        // 由其父节点指向它的链接的颜色。true：红链接，false：黑链接。
        private boolean color;

        Node(K key, V value, int size, boolean color){
            this.key = key;
            this.value = value;
            this.size = size;
            this.color = color;
        }
    }

    private Node<K,V> root;

    public RedBlackTree_06(){
    }

    /**
     * 在红黑树中插入键值对
     *
     * @param key 键
     * @param value 值
     */
    public void put(K key, V value){
        root = put(root, key, value);
        root.color = BLACK;
    }


    /**
     * 在红黑树h中插入新的键值对<key, value>
     * @param h 红黑树
     * @param key 键
     * @param value 值
     * @return 插入新节点之后新的红黑树
     */
    private Node<K,V> put(Node<K,V> h, K key, V value){
        if (h == null){
            // 标准的插入操作，和父节点用红链接相连
            return new Node<>(key, value, 1, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0){
            h.left = put(h.left, key, value);
        }else if (cmp > 0){
            h.right = put(h.right, key, value);
        }else {
            h.value = value;
        }

        // 正常情况：红链接均为左链接，没有任何一个结点同时和两条红链接相连
        if (isRed(h.right) && !isRed(h.left)) {
            // 节点h的左连接是黑色，右链接是红色  --> 需要左旋转h的右链接
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)){
            // 节点h的左子节点同时和两个红链接相连 --> 需要右旋转h的左连接
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)){
            // 节点h的左连接和右链接均为红色
            flipColors(h);
        }
        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    /**
     * 指向节点node的链接是否是红链接
     *
     * @param node 节点
     * @return 红链接返回true，否则返回false
     */
    private boolean isRed(Node<K,V> node){
        if (node == null){
            return false;
        }
        return node.color == RED;
    }

    /**
     * <b> 左旋转h的右连接 </b>
     *
     * <p> 左旋转 ：一条红色的右链接需要被转化为左链接。
     * 在我们实现的某些操作中可能会出现红色右链接或者两条连续的红链接，但在操作完成前这些情况都会被小心地旋转并修复。
     * 旋转操作会改变红链接的指向。首先，假设我们有一条红色的右链接需要被转化为左链接。这个操作叫做左旋转，
     * 它对应的方法接受一条指向红黑树中的某个节点的链接作为参数。假设被指向的结点的右链接是红色的，
     * 这个方法会对树进行必要的调整并返回一个指向包含同一组键的子树且其左连接为红色的根节点的链接。
     * <b> 只是将两个键中的较小者作为根节点变为将较大者作为根节点。</b>
     *
     * <p> 节点h的右链接为红链接，需要把该红链接旋转到左边。
     * <p> 注意：节点h可以是其父节点的左连接或者右链接，节点h的颜色可黑可红。
     *
     * @param h  节点，该节点的右连接为红链接。
     * @return 旋转调整之后的根节点
     */
    private Node<K,V> rotateLeft(Node<K,V> h){
        if (h==null || h.right==null){
            return null;
        }

        // 根节点h的右链接x
        Node<K, V> x = h.right;

        // 将两个键中的较小者（节点h）作为根节点变为将较大者（节点x）作为根节点。
        // 将节点x作为根节点并返回

        h.right = x.left;
        // x作为根节点，则x的左连接肯定是x原来的根节点h
        x.left = h;
        x.color = h.color;
        // h现在是根节点x的红色左连接
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    /**
     * <b> 右旋转h的左连接 </b>
     * 与左旋转相反
     */
    private Node<K,V> rotateRight(Node<K,V> h){
        if (h==null || h.left==null){
            return null;
        }
        // 根节点h的左连接x
        Node<K, V> x = h.left;

        // 将两个键中的较大者（节点h）作为根节点变为将较小者（节点x）作为根节点。
        // 将节点x作为根节点并返回

        h.left = x.right;
        // x作为根节点，它的红节点肯定是它之前的根节点h
        x.right = h;
        x.color = h.color;
        // h现在是x的红色右链接
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node<K,V> h){
        if (h == null){
            return;
        }
        h.color = RED;
        if (h.left != null){
            h.left.color = BLACK;
        }
        if (h.right != null){
            h.right.color = BLACK;
        }
    }



    private int size(Node<K,V> node){
        if (node == null){
            return 0;
        }
        return node.size;
    }


}
