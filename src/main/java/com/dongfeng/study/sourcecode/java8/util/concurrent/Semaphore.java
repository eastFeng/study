package com.dongfeng.study.sourcecode.java8.util.concurrent;

import com.dongfeng.study.sourcecode.java8.util.concurrent.locks.AQS;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <b>信号量</b>
 *
 * <p>Semaphore类似一个资源池（可以类比线程池），每个线程需要调用 acquire() 方法获取资源，然后才能执行，执行完后，需要 release 资源，让给其他的线程用。</p>
 *
 * <p>用于做限流处理，比如同时只允许5五个人（5个线程）访问，超过五个人访问就需要等待</p>
 * <p> Semaphore是基于AQS的共享模式的使用
 *
 * <p>Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源。
 * Semaphore可以用于做流量控制，特别公用资源有限的应用场景，比如数据库连接。</p>
 * <p>
 * <p>
 * <p>计数信号灯。从概念上讲，信号量维护一组许可证。如果有必要的话，每个线程都会在获得许可证之前阻塞，然后取得许可证。
 * 每次释放都会增加一个许可证。</p>
 * @author eastFeng
 * @date 2020-12-24 21:08
 */
public class Semaphore implements Serializable {
    private static final long serialVersionUID = 2940643610942098176L;

    /**
     * 所有的实现机制都通过AQS（AbstractQueuedSynchronizer）的子类实现
     */
    private final Sync sync;

    /**
     * 信号量的同步实现。使用AQS的state字段表示许可证。有公平和非公平版本子类。
     *
     * <p>默认包内可见
     */
    abstract static class Sync extends AQS {
        private static final long serialVersionUID = 6028267436634400205L;

        /**
         * @param permits 许可证数量
         */
        Sync(int permits){
            setState(permits);
        }

        /**
         * 获取剩余许证可数量
         */
        final int getPermits(){
            return getState();
        }

        /**
         * 非公平方式获取共享锁  （获取许可证）
         * @param acquires 要获取的数量
         * @return 许可证剩余数量
         */
        final int nonfairTryAcquireShared(int acquires){
            // 自旋方式
            for (;;){
                int available = getState();
                int remaining = available - acquires;
                if (remaining<0      // 超过最大可获取数量（已经没有许可证可用）
                        ||compareAndSetState(available, remaining)  // CAS更新成功
                ){
                    return remaining;
                }
            }
        }

        /**
         * 在共享模式下尝试释放锁 （增加许可证数量）
         * @param releases 释放的数量
         * @return 释放锁是否成功
         */
        @Override
        protected final boolean tryReleaseShared(int releases){
            // 自旋方式
            for (;;){
                int current = getState();
                int next = current + releases;
                if (next < current){  // releases为负数
                    throw new Error("Maximum permit count exceeded");
                }
                if (compareAndSetState(current, next)){
                    return true;
                }
            }
        }

        /**
         * 减少许可证数量
         * @param reductions 减少的数量
         */
        final void reducePermits(int reductions){
            // 自旋方式
            for (;;){
                int current = getState();
                int next = current - reductions;
                if (next > current){  // reductions为负数
                    throw new Error("Permit count underflow");
                }
                if (compareAndSetState(current, next)){
                    return;
                }
            }
        }

        /**
         * 许可证数量设为0
         */
        final int drainPermits(){
            for (;;){
                int current = getState();
                if (current==0 || compareAndSetState(current, 0)){
                    return current;
                }
            }
        }
    }

    /**
     * 默认包内可见
     */
    static final class NonfairSync extends Sync{
        private static final long serialVersionUID = -1812761355474414149L;

        /**
         * @param permits 许可证数量
         */
        NonfairSync(int permits) {
            super(permits);
        }

        @Override
        protected int tryAcquireShared(int acquires){
            return nonfairTryAcquireShared(acquires);
        }
    }

    /**
     * 默认包内可见
     */
    static final class FairSync extends Sync{

        private static final long serialVersionUID = 2029148720844290521L;

        /**
         * @param permits 许可证数量
         */
        FairSync(int permits) {
            super(permits);
        }

        /**
         * 公平方式获取共享锁 （获取许可证）
         * @param acquires 要获取的数量
         * @return 许可证剩余数量
         */
        @Override
        protected int tryAcquireShared(int acquires){
            for (;;){
                if (hasQueuedPredecessors()){
                    // 有线程等待获取锁的时间比当前线程长
                    return -1;
                }
                int available = getState();
                int remaining = available - acquires;
                if (remaining<0     // 超过最大可获取数量（已经没有许可证可用）
                        || compareAndSetState(available, remaining)){
                    return remaining;
                }
            }
        }
    }

    /**
     * 构造函数 默认是非公平锁
     * @param permits 许可证数量
     */
    public Semaphore(int permits){
        sync = new NonfairSync(permits);
    }

    /**
     * 构造函数
     * @param permits 许可证数量
     * @param fair 是否公平锁
     */
    public Semaphore(int permits, boolean fair){
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }

    /**
     * 获取一个许可证，在获取到一个许可证、或者被其他线程中断之前当前线程一直处于阻塞状态。
     *
     * <p> 如果有许可证，获得许可证，并立即返回，将可用许可证数量减一。
     *
     * <p> 如果没有可用的许可证，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到发生以下两种情况之一：
     * <pre>
     * 1. 其他线程调用这个信号量Semaphore的release方法，并且当前线程是下一个将被分配一个许可证的线程
     * 2. 其他线程中断当前线程
     * </pre>
     * 然后会抛出InterruptedException并清除当前线程的中断状态。
     * @throws InterruptedException 如果当前线程被中断
     */
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * <b>不响应中断 </b>
     * <p> 从这个信号量中获取一个许可，当前线程阻塞直到有一个许可证可用。
     *
     * <p> 如果有许可证，获得许可证，并立即返回，将可用许可证数量减一。
     *
     * <p>如果没有可用的许可证，则当前线程将被禁用以进行线程调度，并处于休眠状态，
     * 直到其他线程调用这个信号量Semaphore的release方法，并且当前线程是下一个将被分配一个许可证的线程。
     *
     * <p>如果当前线程在等待许可证时被中断，那么它将继续等待，
     * 但是线程被分配许可证的时间可能会与没有中断的情况下收到许可证的时间相比发生变化。
     * 当线程确实从此方法返回时，将设置其中断状态。
     */
    public void acquireUninterruptibly(){
        sync.acquireShared(1);
    }

    /**
     * <b>不阻塞线程（不会进入阻塞队列）</b>
     *
     * <p> 尝试从这个信号量获取一个许可证，前提是在调用时有一个许可证可用。
     * <p> 获取许可证（如果有），并立即返回，值为true，将可用许可证的数量减少一个。</p>
     * <p> 如果没有可用的许可证，则此方法将立即返回值false。</p>
     *
     * <p>即使将此信号量设置为使用公平排序策略，对tryAcquire()的调用也会立即获取许可证（如果有许可证可用），无论其他线程当前是否正在等待。
     * 这种“讨价还价”行为在某些情况下是有用的，尽管它破坏了公平。
     * 如果想遵守公平性设置，那么使用tryAcquire(0，TimeUnit.SECONDS)这几乎是等效的（它还检测到中断）。</p>
     *
     * @return 如果获得许可证，则为true；否则为false
     */
    public boolean tryAcquire(){
        return sync.nonfairTryAcquireShared(1) >= 0;
    }

    /**
     * <b>不阻塞线程（不会进入阻塞队列）</b>
     * <p>尝试获取许可证，在超时时间内循环尝试获取，直到尝试获取成功或超时返回，不阻塞线程。</p>
     *
     * @param timeout 超时时间
     * @param unit 超时时间单位
     * @return 如果获得许可证，则为true；否则为false
     * @throws InterruptedException 如果当前线程被中断
     */
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /**
     * 释放一个许可证
     */
    public void release(){
        sync.releaseShared(1);
    }

    /**
     * 获取多个许可证
     * @param permits 要获取的许可证数量
     * @throws InterruptedException 当前线程被打断
     */
    public void acquire(int permits) throws InterruptedException {
        if (permits < 0){
            throw new IllegalArgumentException();
        }
        sync.acquireSharedInterruptibly(permits);
    }

    /**
     * 获取多个许可证
     * @param permits 要获取的许可证数量
     */
    public void acquireUninterruptibly(int permits){
        if (permits < 0){
            throw new IllegalArgumentException();
        }
        sync.acquireShared(permits);
    }

    /**
     * 尝试获取多个许可证
     * @param permits 要获取的许可证数量
     * @return 如果获得permits个许可证，则为true；否则为false
     */
    public boolean tryAcquire(int permits){
        if (permits < 0){
            throw new IllegalArgumentException();
        }
        return sync.nonfairTryAcquireShared(permits) >= 0;
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
        if (permits < 0){
            throw new IllegalArgumentException();
        }
        return sync.tryAcquireSharedNanos(permits, unit.toNanos(timeout));
    }

    /**
     * 释放多个许可证
     * @param permits 要释放的许可证数量
     */
    public void release(int permits){
        if (permits < 0){
            throw new IllegalArgumentException();
        }
        sync.releaseShared(permits);
    }

    /**
     * 返回可用的许可证数量
     */
    public int availablePermits(){
        return sync.getPermits();
    }

    /**
     * 清空许可证
     * @return 清空许可证之前的许可证数量
     */
    protected int drainPermits(){
        return sync.drainPermits();
    }

    protected void reducePermits(int reduction){
        if (reduction < 0){
            throw new IllegalArgumentException();
        }
        sync.reducePermits(reduction);
    }

    /**
     * 是否是公平锁
     * @return true: 是公平锁
     */
    protected boolean isFair(){
        return sync instanceof FairSync;
    }

    /**
     * 阻塞队列是否有线程等待
     * @return true: 有
     */
    public final boolean hashQueuedThreads(){
        return sync.hasQueuedThreads();
    }

    /**
     * @return 阻塞队列的长度
     */
    public final int getQueueLength(){
        return sync.getQueueLength();
    }

    /**
     * @return 阻塞队列中线程的集合
     */
    protected Collection<Thread> getQueuedThreads(){
        return sync.getQueuedThreads();
    }

    @Override
    public String toString(){
        return super.toString() + "[Permits = " + sync.getPermits() + "]";
    }
}
