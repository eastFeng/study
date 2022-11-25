package com.dongfeng.study.basicstudy.algorithms;

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
        PriorityQueue_04<Integer> priorityQueue04 = new PriorityQueue_04<>();
        System.out.println("priorityQueue size: "+ priorityQueue04.size());
        System.out.println("priorityQueue isEmpty: "+ priorityQueue04.isEmpty());
        System.out.println("priorityQueue maxValue: "+ priorityQueue04.delMax());

        priorityQueue04.insert(3);
        priorityQueue04.insert(6);
        priorityQueue04.insert(1);
        priorityQueue04.insert(99);

        priorityQueue04.foreach(t -> System.out.println(t+", "));

        System.out.println("priorityQueue isSorted: "+ priorityQueue04.isSorted());

//        System.out.println("priorityQueue maxValue: "+priorityQueue.delMax());
        System.out.println("priorityQueue size: "+ priorityQueue04.size());
        System.out.println("priorityQueue isEmpty: "+ priorityQueue04.isEmpty());
    }

    public static void bstTest(){
        BinarySearchTree_05<String, String> bstTree = new BinarySearchTree_05<>();

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
