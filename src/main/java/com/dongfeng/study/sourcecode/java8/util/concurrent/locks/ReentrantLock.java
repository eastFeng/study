package com.dongfeng.study.sourcecode.java8.util.concurrent.locks;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <b>可重入互斥锁</b>
 *
 * <p>一种可重入互斥锁，其基本行为和语义与使用synchronized方法和语句访问的隐式监视器锁相同，但是具有扩展功能</p>
 *
 * <p>ReentrantLock属于上次成功获取锁但是尚未释放它的线程。当锁不属于其他线程时，调用lock方法的线程将返回并成功获取锁。
 * 如果当前线程已经拥有锁了，则lock方法立即返回。
 * </p>
 *
 * <p></p>
 * @author eastFeng
 * @date 2020-11-07 14:16
 */
public class ReentrantLock implements Lock, Serializable {

    private static final long serialVersionUID = -387816488097605170L;

    private final Sync sync;

    // AbstractOwnableSynchronizer.getExclusiveOwnerThread方法返回的就是独占模式下持有锁的线程

    abstract static class Sync extends AQS {

        private static final long serialVersionUID = 2852343815877713842L;

        /**
         * 执行Lock.lock
         */
        abstract void lock();

        /**
         * 获取锁 非公平方式
         *
         * @param acquires 重入次数
         * @return 是否获取锁成功
         */
        final boolean nonfairTryAcquire(int acquires){
            final Thread current = Thread.currentThread();
            int state = getState();
            if (state == 0){
                // 1. 锁是空闲的
                if (compareAndSetState(0, acquires)){
                    // CAS更新state的值成功
                    // 有可能有其他线程同时也尝试获取锁调用tryAcquire方法，所以要以CAS方式更新state值
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }else if (current == getExclusiveOwnerThread()){
                // 2. 锁不是空闲的，但是持有锁的线程就是当前线程 ----> 更新state的值
                int nextState = state + acquires;
                if (nextState < 0){
                    throw new Error("超过最大锁计数");
                }
                // 更新state值，不需要CAS方式更新，因为不存在其他线程竞争，只有当前线程能到这里
                setState(nextState);
                return true;
            }
            // 3. 锁不是空闲的，但是持有锁的线程不是当前线程
            return false;
        }

        /**
         * 释放锁  只有持有锁的线程才可以释放锁
         *
         * @return 是否成功释放
         */
        @Override
        protected final boolean tryRelease(int releases){
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread()){
                // 如果当前要释放锁线程不是持有锁的线程
                throw new IllegalMonitorStateException();
            }
            // 是否完全释放锁
            boolean free = false;
            if (c == 0){
                // releases == state，c==0,说明没有嵌套锁了，可以释放了，否则还不能释放掉
                free = true;
                // 将占有锁的线程设为null，释放锁
                setExclusiveOwnerThread(null);
            }
            // 更新state的值 ----> 只有持有锁的线程才会走到这里，所以不用CAS方式
            setState(c);
            return free;
        }

        /**
         * 持有锁的线程是否是当前线程
         */
        @Override
        protected final boolean isHeldExclusively(){
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition(){
            // 实例化一个ConditionObject
            return new ConditionObject();
        }

        /**
         * 获取持有锁的线程
         */
        final Thread getOwner(){
            return getState()==0 ? null : getExclusiveOwnerThread();
        }

        /**
         * 当前线程持有锁的次数：如果持有锁的线程是当前线程，则返回state值，否则，返回0（没有持有锁）；
         */
        final int getHoldCount(){
            return isHeldExclusively() ? getState() : 0;
        }

        /**
         * 是否上锁：true是的；false不是，锁是空闲的
         */
        final boolean isLock(){
            return getState() != 0;
        }

        // 反序列化
        private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            // 重置为解锁状态
            setState(0);
        }
    }

    /**
     * 【非公平锁】
     * 非公平锁的同步对象
     */
    static final class NonfairSync extends Sync{
        private static final long serialVersionUID = -76702844625342460L;

        @Override
        void lock() {
            // 与公平锁相比，直接先进行一次CAS，成功就返回了
            if (compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
            }else {
                acquire(1);
            }
        }

        @Override
        protected final boolean tryAcquire(int acquires){
            return nonfairTryAcquire(acquires);
        }
    }


    /**
     * 【公平锁】
     * 公平锁的同步对象
     */
    static final class FairSync extends Sync{
        private static final long serialVersionUID = 5835038930227853222L;

        // 争锁
        @Override
        void lock() {
            acquire(1);
        }

        /**
         * 尝试获取锁 公平方式
         *
         * @param acquires 获取参数
         * @return true: 1.锁是空闲的 2.重入锁，线程本来就持有锁
         */
        @Override
        protected final boolean tryAcquire(int acquires){
            Thread current = Thread.currentThread();
            int state = getState();

            if (state == 0){
                // 1. 锁是空闲的
                // 虽然此时此刻锁是空闲的，但是这是公平锁，要讲究先来后到，看看有没有别的线程在队列中等了半天
                if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)){
                    // 没有线程比当前线程等待时间更长（因为是公平锁），并且CAS更新state字段成功
                    // 有可能有其他线程同时也尝试获取锁调用tryAcquire方法，所以要以CAS方式更新state值

                    // 获取到锁，标记一下
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }else if(current==getExclusiveOwnerThread()){
                // 2. 锁不是空闲的，并且持有锁的线程就是当前线程 ----> 更新state的值
                int nextState = state + acquires;
                if (nextState < 0){
                    throw new Error("超过最大锁计数");
                }
                // 更新state值，不需要CAS方式更新，因为不存在其他线程竞争，只有当前线程能到这里
                setState(nextState);
                return true;
            }
            // 3. 锁不是空闲的，但是持有锁的线程不是当前线程
            return false;
        }
    }

    /**
     * 创建可重入锁 默认非公平锁
     */
    public ReentrantLock(){
        sync = new NonfairSync();
    }

    /**
     * 根据给定的公平策略创建可重入锁的实例
     *
     * @param isFair 是否公平锁
     */
    public ReentrantLock(boolean isFair){
        if (isFair){
            sync = new FairSync();
        }else {
            sync = new NonfairSync();
        }
    }

    /**
     * 获取锁
     * <p>如果锁是空闲的，立即返回，将锁保持计数设置为1（state值设为1）。
     * <p>如果当前线程已经持有锁，那么保持计数将增加1（state值增加1），并且该方法立即返回。
     * <p>如果锁由另一个线程持有，则当前线程将因线程调度而被禁用，并处于休眠状态，直到获得锁，此时锁保持计数设置为1（state值设为1）。
     */
    @Override
    public void lock() {
        sync.lock();
    }

    /**
     * 获取锁，除非当前线程被中断。
     *
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 仅当调用时未被另外一个线程持有时才获取锁（非公平方式）
     *
     * <p>如果锁是空闲的，立即获取锁，将state值设为1，并返回true。
     * 即使是公平锁策略，调用tryLock将立即获取该锁（如果该锁空闲），无论其他线程是否正在等待该锁。
     * <p>如果当前线程已经持有此锁，则state将增加1，并且该方法返回true。
     * <p>如果锁由另一个线程持有，则此方法将立即返回值false。
     *
     * @return 如果锁是空闲的并且被当前线程获取，或者锁已经被当前线程持有，则为true；否则为false
     */
    @Override
    public boolean tryLock() {
        // 只支持非公平的方式
        return sync.nonfairTryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
