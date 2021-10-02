package com.dongfeng.study.basicstudy.thread;

/**
 * <b> 线程池 </b>
 *
 * @author eastFeng
 * @date 2021-10-02 18:50
 */
public class ThreadPool {

    public static void main(String[] args) {
        // 异步任务
        yiBuRenWu();
    }


    public static void yiBuRenWu(){
        /*
         * 任务执行服务涉及的基本接口：
         * Runnable和Callable：表示要执行的异步任务。
         * Executor和ExecutorService：表示执行服务。
         * Future：表示异步任务的结果。
         *
         * Runnable没有返回结果，而Callable有，Runnable不会抛出异常，而Callable会。
         */


    }


    public static void basicConcept(){
        /*
         * 线程池是并发程序中一个非常重要的概念和技术。
         *
         * 线程池，顾名思义，就是一个线程的池子，里面有若干线程，
         * 它们的目的就是执行提交给线程池的任务，执行完一个任务后不会退出，而是继续等待或执行新任务。
         *
         * 线程池主要由两个概念组成：一个是任务队列；另一个是工作者线程。
         * 工作者线程主体就是一个循环，循环从队列中接受任务并执行，任务队列保存待执行的任务。
         *
         * 线程池的优点是显而易见的：
         * 它可以重用线程，避免线程创建的开销。
         * 任务过多时，通过排队避免创建过多线程，减少系统资源消耗和竞争，确保任务有序完成。
         */
    }
}
