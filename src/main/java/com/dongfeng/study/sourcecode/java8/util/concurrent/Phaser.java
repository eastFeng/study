package com.dongfeng.study.sourcecode.java8.util.concurrent;

import java.util.concurrent.ForkJoinPool;

/**
 * @author eastFeng
 * @date 2021-01-26 17:51
 */
public class Phaser {
    // 这个类实现了X10“clocks”的扩展

    /**
     * 主要的状态表示，保留4个bit位字段： unarrived
     */
    private volatile long state;

    private static final int  MAX_PARTIES     = 0xffff;
    private static final int  MAX_PHASE       = Integer.MAX_VALUE;
    private static final int  PARTIES_SHIFT   = 16;
    private static final int  PHASE_SHIFT     = 32;
    /**
     * to mask ints
     */
    private static final int  UNARRIVED_MASK  = 0xffff;
    /**
     * to mask longs
     */
    private static final long PARTIES_MASK    = 0xffff0000L;
    private static final long COUNTS_MASK     = 0xffffffffL;
    private static final long TERMINATION_BIT = 1L << 63;

    /**
     * 一些特定值
     */
    private static final int  ONE_ARRIVAL     = 1;
    private static final int  ONE_PARTY       = 1 << PARTIES_SHIFT;
    private static final int  ONE_DEREGISTER  = ONE_ARRIVAL|ONE_PARTY;
    private static final int  EMPTY           = 1;


    // 一些private方法

    private static int unarrivedOf(long s) {
        int counts = (int)s;
        return (counts == EMPTY) ? 0 : (counts & UNARRIVED_MASK);
    }

    private static int partiesOf(long s) {
        return (int)s >>> PARTIES_SHIFT;
    }

    private static int phaseOf(long s) {
        return (int)(s >>> PHASE_SHIFT);
    }

    private static int arrivedOf(long s) {
        int counts = (int)s;
        return (counts == EMPTY) ? 0 :
                (counts >>> PARTIES_SHIFT) - (counts & UNARRIVED_MASK);
    }


//
//    private final Phaser parent;
//
//    private final Phaser root;

    // static final class QNode implements ForkJoinPool.ManagedBlocker

    static final class QNode implements ForkJoinPool.ManagedBlocker{

        final Phaser phaser;
        final int phase;
        final boolean interruptible;
        final boolean timed;
        boolean wasInterrupted;
        long nanos;
        final long deadline;
        volatile Thread thread; // nulled to cancel wait
        QNode next;


        QNode(Phaser phaser, int phase, boolean interruptible,
              boolean timed, long nanos) {
            this.phaser = phaser;
            this.phase = phase;
            this.interruptible = interruptible;
            this.nanos = nanos;
            this.timed = timed;
            this.deadline = timed ? System.nanoTime() + nanos : 0L;
            thread = Thread.currentThread();
        }

        @Override
        public boolean block() throws InterruptedException {
            return false;
        }

        @Override
        public boolean isReleasable() {
            return false;
        }
    }
}
