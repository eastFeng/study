package com.dongfeng.study.basicstudy.thread;

import org.apache.poi.ss.formula.functions.T;

import java.util.concurrent.Future;

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

        //
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
         * Thread类定义了如下关于中断的三个方法：
         * 1. public void interrupt()
         * 2. public boolean isInterrupted()
         *
         * 3. public static boolean interrupted() {
         *      // 返回当前线程是否被中断，并重置中断标志（重新设置为未中断）
         *      return currentThread().isInterrupted(true);
         *    }
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
         * WAITING/TIMED_WAITING：线程在等待某个条件或超时。
         * BLOCKED：线程在等待锁，试图进入同步块。
         * NEW/TERMINATED：线程还未启动或已结束。
         */

        // 1. RUNNABLE
        runnableReaction();

        // 2. WAITING/TIMED_WAITING
        waitingReaction();

        // BLOCKED

        /*
         * 4. NEW/TERMINATE
         * 如果线程尚未启动（NEW），或者已经结束（TERMINATED），则调用interrupt()对它没有任何效果，中断标志位也不会被设置。
         */
    }

    public static void runnableReaction(){
        /*
         * 1. RUNNABLE
         * 如果线程在运行中，且没有执行IO操作，interrupt()只是会设置线程的中断标志位，没有任何其他作用。
         * 线程应该在运行过程中合适的位置检查中断标志位，
         * 比如，如果主体代码是一个循环，可以在循环开始处进行检查，如下所示：
         */
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    // 当前线程没有中断才进入

                    System.out.println("当前线程没有中断 " + Thread.currentThread().getName());
                }
            }
        };
        t1.start();
    }

    public static void waitingReaction(){
        /*
         * 2. WAITING/TIMED_WAITING
         * 线程调用join/wait/sleep方法会进入WAITING或TIMED_WAITING状态，
         * 在这些状态时，对线程对象调用interrupt()会使得该线程抛出InterruptedException。
         * 需要注意的是，抛出异常后，中断标志位会被清空，而不是被设置。比如，执行如下代码：
         */

        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 输出当前线程是否被中断
                    System.out.println(isInterrupted());
                }
            }
        };
        t2.start();
        try {
            // 主线程睡眠100毫秒
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        // 调用t2线程的interrupt方法，中断t2线程
        t2.interrupt();
        /*
         * InterruptedException是一个受检异常，线程必须进行处理。
         *
         * 捕获到InterruptedException，通常表示希望结束该线程，线程大致有两种处理方式：
         * 1）向上传递该异常，这使得该方法也变成了一个可中断的方法，需要调用者进行处理；
         * 2）有些情况，不能向上传递异常，比如Thread的run方法，它的声明是固定的，不能抛出任何受检异常，
         * 这时，应该捕获异常，进行合适的清理操作，清理后，
         * 一般应该调用Thread的interrupt方法设置中断标志位，使得其他代码有办法知道它发生了中断。
         */
    }

    public static void blockedReaction(){
        /*
         * 3. BLOCKED
         * 如果线程在等待锁，对线程对象调用interrupt()只是会设置线程的中断标志位，线程依然会处于BLOCKED状态，
         * 也就是说，interrupt()并不能使一个在等待锁的线程真正“中断”。
         * 我们看段代码：
         */

        final Object lock = new Object();

        Thread t3 = new Thread(){
            @Override
            public void run() {
                synchronized (lock){
                    while (!Thread.currentThread().isInterrupted()){
                        // 当前线程没有被中断才会进入while循环
                    }
                    System.out.println(Thread.currentThread().getName() + " exit");
                }
            }
        };
        t3.setName("t3");
        t3.start();
        try {
            Thread.sleep(1000);
            // 中断t3线程
            t3.interrupt();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如何正确地取消/关闭线程
     */
    public static void correctWay(){
        /*
         * interrupt方法不一定会真正“中断”线程，它只是一种协作机制，
         * 如果不明白线程在做什么，不应该贸然地调用线程的interrupt方法，以为这样就能取消线程。
         *
         * 对于以线程提供服务的程序模块而言，它应该封装取消/关闭操作，
         * 提供单独的取消/关闭方法给调用者，外部调用者应该调用这些方法而不是直接调用interrupt。
         * Java并发库的一些代码就提供了单独的取消/关闭方法，比如，Future接口提供了如下方法以取消任务：
         *
         * boolean cancel(boolean mayInterruptIfRunning);
         */
    }
}







































