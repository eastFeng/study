package com.dongfeng.study.basicstudy.algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eastFeng
 * @date 2021-04-16 1:36
 */
public class Test {
    public static void main(String[] args) {
//        priorityQueueTest();

        bstTest();
    }

    public static void priorityQueueTest(){
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        System.out.println("priorityQueue size: "+priorityQueue.size());
        System.out.println("priorityQueue isEmpty: "+priorityQueue.isEmpty());
        System.out.println("priorityQueue maxValue: "+priorityQueue.delMax());

        priorityQueue.insert(3);
        priorityQueue.insert(6);
        priorityQueue.insert(1);
        priorityQueue.insert(99);

        priorityQueue.foreach(t -> System.out.println(t+", "));

        System.out.println("priorityQueue isSorted: "+priorityQueue.isSorted());

//        System.out.println("priorityQueue maxValue: "+priorityQueue.delMax());
        System.out.println("priorityQueue size: "+priorityQueue.size());
        System.out.println("priorityQueue isEmpty: "+priorityQueue.isEmpty());
    }

    public static void bstTest(){
        BinarySearchTree<String, String> bstTree = new BinarySearchTree<>();

//        System.out.println("size: " + bstTree.size());
//        System.out.println("上海: " + bstTree.get("上海"));


        bstTree.put("A", "134");
        bstTree.put("s", "134");
        bstTree.put("c", "134");
        bstTree.put("d", "134");
        bstTree.put("b", "134");
        bstTree.put("q", "134");

//        System.out.println("上海: " + bstTree.get("上海"));
//        System.out.println("size: " + bstTree.size());

        bstTree.inOrderTraversal((k,v)-> System.out.println(k + "=" + v));
    }
}
