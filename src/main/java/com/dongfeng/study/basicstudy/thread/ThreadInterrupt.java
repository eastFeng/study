package com.dongfeng.study.basicstudy.thread;

/**
 * <b> 线程的中断 </b>
 * @author eastFeng
 * @date 2021-05-06 18:22
 */
public class ThreadInterrupt {
    public static void main(String[] args) {
        /*
         * 如何在Java中取消或关闭一个线程？
         * 在Java中，停止一个线程的主要机制是中断，中断并不是强迫终止一个线程，它是一种协作机制，
         * 是给线程传递一个取消信号，但是由线程来决定如何以及何时退出。
         */

        // 取消/关闭的场景
        scenic();

        // 取消/关闭的机制
        mechanism();

        // 线程对中断的反应
        threadReactionToInterrupt();
    }

    /**
     * 取消/关闭的场景
     */
    public static void scenic(){
        /*
         * 我们知道，通过线程的start方法启动一个线程后，线程开始执行run方法，run方法运行结束后线程退出，
         * 那为什么还需要结束一个线程呢？有多种情况，比如：
         * 1）很多线程的运行模式是死循环，比如在生产者/消费者模式中，消费者主体就是一个死循环，它不停地从队列中接受任务，执行任务，
         *   在停止程序时，我们需要一种“优雅”的方法以关闭该线程。
         *
         * 2）在一些图形用户界面程序中，线程是用户启动的，完成一些任务，比如从远程服务器上下载一个文件，在下载过程中，用户可能会希望取消该任务。
         *
         * 3）在一些场景中，比如从第三方服务器查询一个结果，我们希望在限定的时间内得到结果，如果得不到，我们会希望取消该任务。
         *
         * 4）有时，我们会启动多个线程做同一件事，比如类似抢火车票，我们可能会让多个好友帮忙从多个渠道买火车票，
         *   只要有一个渠道买到了，我们会通知取消其他渠道。
         */
    }

    /**
     * 取消/关闭的机制
     */
    public static void mechanism(){
        /*
         * 在Java中，停止一个线程的主要机制是中断，中断并不是强迫终止一个线程，它是一种协作机制，
         * 是给线程传递一个取消信号，但是由线程来决定如何以及何时退出。
         *
         * Thread类定义了如下关于中断的方法：
         * public void interrupt()
         * public boolean isInterrupted()
         *
         * public static boolean interrupted() {
         *   // 返回当前线程是否被中断，并重置中断标志（重新设置为未中断）
         *   return currentThread().isInterrupted(true);
         * }
         *
         * 这三个方法名字类似，比较容易混淆，我们解释一下。isInterrupted()和interrupt()是实例方法，调用它们需要通过线程对象；
         * interrupted()是静态方法，实际会调用Thread.currentThread()操作当前线程。
         * 每个线程都有一个标志位，表示该线程是否被中断了。该标志为如果为true就是被中断了。
         * 所以isInterrupted和interrupted方法如果为true就是线程被中断了。
         *
         * 1）实例方法isInterrupted：返回对应线程的中断标志位是否为true（测试此线程是否已中断），线程的中断状态不受此方法的影响。
         *
         * 2）静态方法interrupted：返回当前线程的中断标志位是否为true（测试当前线程是否已中断），
         *   但它还有一个重要的副作用，就是清空中断标志位（重置标志位为未中断，无论当前线程是否被中断），
         *   也就是说，连续两次调用interrupted()，第一次返回的结果为true，第二次一般就是false（除非同时又发生了一次中断）。
         *
         * 3）实例方法interrupt：表示中断对应的线程。
         *
         */
    }

    /**
     * 线程对中断的反应
     */
    public static void threadReactionToInterrupt(){
        /*
         * interrupt方法对线程的影响与线程的状态和在进行的IO操作有关。
         * 我们主要考虑线程的状态，IO操作的影响和具体IO以及操作系统有关，我们就不讨论了。
         * 线程状态有：
         * RUNNABLE：线程在运行或具备运行条件只是在等待操作系统调度。
         *
         */
    }

}







































