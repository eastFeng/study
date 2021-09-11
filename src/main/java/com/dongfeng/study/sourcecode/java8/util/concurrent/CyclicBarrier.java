package com.dongfeng.study.sourcecode.java8.util.concurrent;

import com.dongfeng.study.sourcecode.java8.util.concurrent.locks.Condition;
import com.dongfeng.study.sourcecode.java8.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Cyclic /ˈsaɪklɪk/ : 循环的，周期性的
 * <p> Barrier /ˈbæriə(r)/ : 屏障，栅栏
 * <p> Cyclic Barrier : 可重复使用的栅栏，周期性的栅栏，不是用了一次就没用了的。
 *
 * <p> CyclicBarrier允许一组线程在到达某个栅栏<b>互相等待</b>，直到最后一个线程到达栅栏点，栅栏才会打开，处于阻塞状态的线程恢复继续执行。
 * 比如在打王者的时候，十个人必须全部加载到100%，才可以开局。
 *
 * <p> 一种同步辅助工具，允许一组线程全部等待对方到达一个公共屏障点。
 * CyclicBarrier 在涉及固定大小的线程方的程序中非常有用，这些线程偶尔必须相互等待。
 * 这个屏障被称为循环屏障，因为它可以在等待的线程被释放后重新使用。
 *
 * <p> CyclicBarrier 是基于 Condition 来实现的
 *
 * @author eastFeng
 * @date 2020-12-24 21:07
 */
public class CyclicBarrier {

    /**
     * CyclicBarrier 是可以重复使用的，我们把每次从开始使用到穿过栅栏当做“一代” 或者 “一个周期”
     */
    private static class Generation{
        // 栅栏是否被打破，默认false，没有被打破
        boolean broken = false;
    }

    /**
     * 独占锁（互斥锁）
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * CyclicBarrier 是基于 Condition 的
     * Condition 是“条件”的意思，CyclicBarrier 的等待线程通过barrier 的“条件”是大家都到了栅栏上。
     */
    private final Condition trip = lock.newCondition();

    /**
     * 参与的线程数 : 屏障需要拦截的线程数
     */
    private final int parties;

    /**
     * 如果设置了这个，代表越过栅栏之前，要执行相应的操作
     */
    private final Runnable barrierCommand;

    /**
     * 代表当前所处的 “代”
     */
    private Generation generation = new Generation();

    /**
     * 还没有到栅栏的线程数，这个值初始化为 parties，然后递减
     * 还没有到栅栏的线程数 = parties - 已经到栅栏的数量
     */
    private int count;

    /**
     * 开启新的一代，当最后一个线程到达栅栏上的时候，调用这个方法来唤醒其他线程，同时初始化 “下一代”
     */
    private void nextGeneration(){
        // 1.首先唤醒所有在栅栏上等待的线程
        trip.signalAll();
        // 2.更新 count 值
        count = parties;
        // 3.重新生成 “新一代”
        generation = new Generation();
    }

    /**
     * 打破一个栅栏
     */
    private void breakBarrier(){
        // 1.设置状态 broken 为 true
        generation.broken = true;
        // 2.重置 count 为初始值 parties
        count = parties;
        // 3.唤醒所有已经在等待的线程
        trip.signalAll();
    }


    private int dowait(boolean timed, long nanos) throws BrokenBarrierException, InterruptedException, TimeoutException {
        final ReentrantLock lock = this.lock;
        // 先要通过ReentrantLock获取独占锁，然后在 finally 中记得释放锁
        // Condition 中，condition的 await() 会释放锁，被 signal() 唤醒的时候需要重新获取锁
        lock.lock();

        try {
            final  Generation g = generation;
            // 检查栅栏是否被打破，如果被打破，抛出 BrokenBarrierException 异常
            if (g.broken){
                throw new BrokenBarrierException();
            }
            // 检查中断状态，如果中断了，抛出 InterruptedException 异常
            if (Thread.interrupted()){
                // 打破栅栏
                breakBarrier();
                throw new InterruptedException();
            }

            // index 是该方法的返回值，注意到的是，index 是从 count 递减后得到的值
            // 【每一个线程调用这个方法都会使 count 减 1】
            int index = --count;
            // 如果index 等于 0，说明所有的线程都到了栅栏上了，准备通过
            if (index == 0){
                boolean ranAction = false;
                try {
                    // 如果在初始化的时候，指定了通过栅栏前需要执行的操作，在这里会得到执行
                    final Runnable command = barrierCommand;
                    if (command != null){
                        command.run();
                    }
                    // 如果 ranAction 为 true，说明执行 command.run() 的时候，没有发生异常退出的情况
                    ranAction = true;
                    // 唤醒等待的线程，然后开启新一代
                    nextGeneration();
                    return 0;
                } finally {
                    // 如果try里面发生了异常，则在异常被抛给上层之前执行
                    if (!ranAction){  //只有执行指定的操作command.run()的时候发生异常，ranAction 才会为false
                        // 打破栅栏意味着唤醒所有等待的线程，设置 broken 为true，充值count 为 parties
                        breakBarrier();
                    }
                }
            }
            // 如果最后一个线程调用 await，那么上面就返回了

            // 下面的操作是给那些不是最后一个到达栅栏的线程执行的，
            // 如果该线程不是最后一个调用await方法的线程，则它会一直处于等待状态，除非发生以下情况：
            // 1. 最后一个线程达到。
            // 2. 指定的超时过期（如果设置了超时时间）。
            // 3. 其他线程中断了当前线程。
            // 4. 其他等待线程被中断（其他线程中断了当前线程之外的等待的线程中的一个）。。。。。 3和4可以总结为：某个参与等待的线程被中断。
            // 5. 其他等待线程指定的超时过期。
            // 6. 其他线程对此栅栏调用了 reset 方法。
            for (;;){
                try {
                    // 如果带有超时机制，调用超时的 Condition 的 await 方法等待，直到最后一个线程调用 await
                    if (!timed){  // 没有超时机制
                        // 没有超时时间的等待，【直接等待，直到被唤醒】
                        trip.await();
                    }else if (nanos > 0){  // 带有超时机制，并且超时时间 > 0
                        // 带有超时时间的等待，等待指定时间
                        nanos = trip.awaitNanos(nanos);
                    }
                } catch (InterruptedException e) {
                    // 到这里，说明等待的线程在 await（Condition 的 await）的时候被中断
                    if (g==generation && !g.broken){  //新的一代没有产生，并且栅栏没有被打破
                        // 打破栅栏
                        breakBarrier();
                        // 打破栅栏后，重新抛出这个 InterruptedException 异常
                        throw e;
                    }else {
                        // 到这里，说明 g!=generation，说明新的一代已经产生，即最后一个线程 await 执行完成
                        // 那么此时就没有必要再抛出 InterruptedException 异常，记录下这个中断信息即可
                        // 或者是栅栏已经被打破了，那么也不应该抛出 InterruptedException 异常，而是之后抛出BrokenBarrierException 异常
                        Thread.currentThread().interrupt();
                    }
                }

                // 当有任何一个等待的线程中断后，就会调用breakBarrier方法，就会唤醒其他的线程，其他线程醒来后，也要抛出异常
                // 唤醒后，检查栅栏是否被 ”打破“
                if (g.broken){
                    throw new BrokenBarrierException();
                }

                // 这个for循环除了异常，就是要从这里退出了。 g != generation表示正常换代了。
                // 最后一个线程在执行完指定任务（如果有的话），会调用 nextGeneration 来开启一个新的代，然后释放掉锁，
                // 其他线程从 Condition 的 await 方法中得到锁并返回，然后到这里的时候，其实就会满足 g!=generation，
                // 那什么时候不满足呢？ barrierCommand 执行过程中抛出了异常，那么会执行打破栅栏操作，
                // 设置broken 为 true，然后唤醒这些线程。这些线程会从上面的 if(g.broken) 这个分支抛出 BrokenBarrierException异常返回，
                // 当然，还有最后一种可能，那就是 await 超时，此种情况不会从上面的 if 分支异常返回，也不会从这里返回，会执行后面的代码
                if (g != generation){
                    return index;
                }

                // 如果醒来发现超时了，打破栅栏，抛出异常
                if (timed && nanos<0){
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            // 释放独占锁
            lock.unlock();
        }
    }


    public CyclicBarrier(int parties, Runnable barrierAction){
        if (parties <= 0){
            throw new IllegalArgumentException("parties <= 0");
        }
        this.parties = parties;
        this.count = parties;
        this.barrierCommand = barrierAction;
    }


    public CyclicBarrier(int parties){
        this(parties, null);
    }

    public int getParties(){
        return parties;
    }

    /**
     * <b>不带超时机制的等待通过栅栏的 await 方法</b>
     *
     * <p> 调用await方法的线程告诉CyclicBarrier自己已经到达屏障，然后当前线程被阻塞。直到parties个参与线程调用了await方法。
     */
    public int await() throws BrokenBarrierException, InterruptedException {
        try {
            return dowait(false, 0);
        } catch (TimeoutException e) {
            throw new Error(e);
        }
    }

    /**
     * <b>带超时机制的等待通过栅栏的 await 方法</b>
     * <p> 等待直到所有参与方（参与的线程）都对此栅栏调用了wait，或者指定的等待时间已过。
     * <p> 如果当前线程如果不是最后到达的线程，则出于线程调度的目的，它将被禁用，并处于休眠状态，直到发生以下情况之一：
     * <ul>
     * <li> 最后一个线程达到
     * <li> 指定的超时过期
     * <li> 其他线程中断了当前线程
     * <li> 其他线程中断了当前线程之外的等待的线程中的一个（其他等待线程被中断）
     * <li> 其他等待线程指定的超时过期
     * <li> 其他线程对此栅栏调用了 reset 方法
     * </ul>
     * 如果当前线程：
     * <ul>
     * <li> 在进入此方法时设置了中断状态
     * <li> 等待时被中断
     * </ul>
     * 会抛出 InterruptedException 异常，并清除当前线程的中断状态。
     * <p> 如果超过了指定的超时时间，则抛出 TimeoutException 异常。如果超时时间等于或者小于零，则该方法根本不会等待。
     * <p> 如果在线程等待时重置了栅栏，或者在调用 await时栅栏被打破，或者在任何线程等待时，则抛出 BrokenBarrierException 异常。
     * <p> 如果任何线程在等待时被中断，那么所有其他等待的线程都将抛出 BrokenBarrierException 异常，并且屏障处于打破状态。
     * <p> 如果当前线程是最后一个到达的线程，会唤醒其他所有的等待线程
     */
    public int await(long timeout, TimeUnit unit) throws BrokenBarrierException, InterruptedException, TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }

    /**
     * 判断栅栏是否被打破
     * <pre>
     * 三种情况下栅栏会被打破：
     * 1. 中断：如果某个等待的线程发生了中断，那么会打破栅栏，同时抛出 InterruptedException 异常
     * 2. 超时：打破栅栏，同时抛出 TimeoutException
     * 3. 指定执行的操作抛出了异常
     * </pre>
     */
    public boolean isBroken(){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return generation.broken;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 重置一个栅栏
     *
     * 如果初始化时，指定了线程 parties = 4，前面有 3 个线程调用了 await 等待，
     * 在第 4 个线程调用 await 之前，我们调用 reset 方法，那么会发生什么？
     * 首先，打破栅栏，那意味着所有等待的线程（3个等待的线程）会唤醒，await 方法会通过抛出 BrokenBarrierException 异常返回。
     * 然后开启新的一代，重置了 count 和 generation，相当于一切归零了。
     */
    public void reset(){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 打破当前的栅栏
            breakBarrier();
            // 生成一个新的 ”一代“
            nextGeneration();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取达到栅栏的线程的个数
     */
    public int getNumberWaiting(){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return parties - count;
        } finally {
            lock.unlock();
        }
    }

}


