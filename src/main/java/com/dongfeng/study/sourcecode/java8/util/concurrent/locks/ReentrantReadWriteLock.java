package com.dongfeng.study.sourcecode.java8.util.concurrent.locks;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author eastFeng
 * @date 2021-01-20 11:15
 */
public class ReentrantReadWriteLock implements ReadWriteLock, Serializable {

    private static final long serialVersionUID = 973095444940728721L;

//    private final ReentrantReadWriteLock.ReadLock readLock;
//
//    private final ReentrantReadWriteLock.WriteLock writeLock;


    @Override
    public Lock readLock() {
        return null;
    }

    @Override
    public Lock writeLock() {
        return null;
    }

    static final long getThreadId(Thread thread) {
        return UNSAFE.getLongVolatile(thread, TID_OFFSET);
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long TID_OFFSET;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> tk = Thread.class;
            TID_OFFSET = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("tid"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * ReentrantReadWriteLock的同步实现。分为公平和非公平的版本。
     */
    abstract static class Sync extends AQS{
        private static final long serialVersionUID = 4567018828172593960L;


        // << : 左移运算符
        static final int SHARED_SHIFT   = 16;
        // 1<<16 = 65536
        static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
        static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
        static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;

        static int sharedCount(int c){
            // >>> : 无符号右移
            return c >>> SHARED_SHIFT;
        }

        static int exclusiveCount(int c){
            return c & EXCLUSIVE_MASK;
        }

        /**
         * 在ThreadLocal中维护
         */
        static final class HoldCounter{
            int count = 0;
            final long tid = getThreadId(Thread.currentThread());
        }

        static final class ThreadLocalHoldCounter
                extends ThreadLocal<HoldCounter> {
            @Override
            public HoldCounter initialValue() {
                return new HoldCounter();
            }
        }
    }

    public static class ReadLock implements Lock, Serializable{
        private static final long serialVersionUID = -2096672137945974775L;

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    public static class WriteLock implements Lock, Serializable{
        private static final long serialVersionUID = 2731254725610727287L;

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
