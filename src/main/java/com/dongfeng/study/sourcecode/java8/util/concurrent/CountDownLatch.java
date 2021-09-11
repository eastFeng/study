package com.dongfeng.study.sourcecode.java8.util.concurrent;

import com.dongfeng.study.sourcecode.java8.util.concurrent.locks.AQS;

import java.util.concurrent.TimeUnit;

/**
 * <b>计数器</b>
 *
 * <p> countdown: 倒计时，倒数读秒    latch: 门栓，栅栏
 *
 * <p> 一种同步辅助工具，<b>允许一个或者多个线程等待其他线程中正在执行的一组操作完成</b>。
 *     例如，应用程序的主线程希望在负责启动框架服务的线程已经启动所有的框架服务之后再执行。
 *
 * <p> 当计数count的值为0时，表示所有的线程都已经完成一些任务。
 *
 * <p> CountDownLatch是基于AQS的共享模式的使用
 *
 * <p> 不可重复使用 : 计数无法重置。如果需要重置计数的版本，请考虑使用CyclicBarrier
 *
 * <p> 初始化为N的倒计时锁存器可用于使一个线程等待N个线程完成某个操作，或者某个操作已完成N次。
 *
 * <p> CountDownLatch有一个有用的特性，它不要求调用countDown方法的线程等待计数count的值减到0。
 * 它只是阻塞调用await的线程直到count的值减到0。
 * @author eastFeng
 * @date 2020-12-23 21:35
 */
public class CountDownLatch {

    private static final class Sync extends AQS {

        private static final long serialVersionUID = -1588772357837628519L;

        /**
         * count == state
         * @param count 计数器
         */
        Sync(int count){
            setState(count);
        }

        int getCount(){
            return getState();
        }

        @Override
        protected int tryAcquireShared(int acquires){
            // 1 : 获取成功， -1 : 获取不成功
            return (getState()==0) ? 1 : -1;
        }

        /**
         * 尝试释放共享模式的锁 : 这个方法很简单，用自旋的方法实现 state 减 1
         *
         * @param releases 该参数在方法中没用到
         * @return state减到0时返回true
         */
        @Override
        protected boolean tryReleaseShared(int releases){
            for (;;){
                int c = getState();
                if (c == 0){
                    // 设置的count已经被用完
                    return false;
                }
                int nextC = c - 1;
                if (compareAndSetState(c, nextC)){
                    // CAS成功 ，就返回
                    return nextC == 0;
                }

                // 没有减一赋值成功，继续循环
            }
        }
    }


    /**
     * 实例变量sync
     */
    private final Sync sync;

    /**
     * 用给定的count构造一个CountDownLatchJava8实例
     * @param count 个数
     */
    public CountDownLatch(int count){
        if (count < 0){
            throw new IllegalArgumentException("count < 0");
        }
        this.sync = new Sync(count);
    }

    /**
     * 【主要方法】
     * <p> 使当前线程等待，直到锁的state值到0 或者 线程被中断。
     * <p> 如果当前state为零，则此方法立即返回。
     * <p> 该方法是一个阻塞方法，当state的值减为0的时候，该方法才会返回（阻塞结束）
     * <p> 该方法可以被多个线程调用
     * <p> 如果当前线程在进入此方法时设置其中断状态 或者 在等待的时候被中断，则抛出InterruptedException异常。
     * @throws InterruptedException 线程中断异常
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, timeUnit.toNanos(timeout));
    }

    /**
     * 【主要方法】
     * <p> 每次调用都会将state（也就是count计数器）减1，直到state的值为0；如果state的值为0，释放所有等待的线程。
     * <p> 如果当前计数（count）等于零，则什么也不会发生。
     * <p> 该方法可以被多个线程调用
     */
    public void countDown(){
        sync.releaseShared(1);
    }

    public long getCount(){
        return sync.getCount();
    }

    @Override
    public String toString(){
        return super.toString() + "[Count = "+sync.getCount()+"]";
    }
}
