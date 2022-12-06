package com.dongfeng.study.basicstudy.sourcecode.java8.util.concurrent.locks;

/**
 * 用于创建锁和其他同步类的基本线程阻塞原语。
 *
 * @author eastFeng
 * @date 2021-01-19 18:56
 */
public class LockSupport {

    /**
     * 不能被实例化
     */
    private LockSupport() {}

    /**
     * Hotspot implementation via intrinsics API
     * 通过 intrinsics API 实现 Hotspot
     */
    private static final sun.misc.Unsafe UNSAFE;
    private static final long parkBlockerOffset;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> tk = Thread.class;
            parkBlockerOffset = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("parkBlocker"));
            SEED = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private static void setBlocker(Thread t, Object arg) {
        // Even though volatile, hotspot doesn't need a write barrier here.
        // 即使是volatile 类型的参数, hotspot在这里也不需要写屏障。
        UNSAFE.putObject(t, parkBlockerOffset, arg);
    }


    public static void unpark(Thread thread) {
        if (thread != null){
            UNSAFE.unpark(thread);
        }
    }

    /**
     * 除非许可证可用，否则出于线程调度目的禁用当前线程。
     * @param blocker 负责此线程的同步对象
     */
    public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

    public static void parkNanos(Object blocker, long nanos) {
        if (nanos > 0) {
            Thread t = Thread.currentThread();
            setBlocker(t, blocker);
            UNSAFE.park(false, nanos);
            setBlocker(t, null);
        }
    }

    public static void parkUntil(Object blocker, long deadline) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(true, deadline);
        setBlocker(t, null);
    }

    public static Object getBlocker(Thread t) {
        if (t == null){
            throw new NullPointerException();
        }
        return UNSAFE.getObjectVolatile(t, parkBlockerOffset);
    }

    public static void park() {
        UNSAFE.park(false, 0L);
    }

    public static void parkNanos(long nanos) {
        if (nanos > 0) {
            UNSAFE.park(false, nanos);
        }
    }

    public static void parkUntil(long deadline) {
        UNSAFE.park(true, deadline);
    }

    static final int nextSecondarySeed() {
        int r;
        Thread t = Thread.currentThread();
        if ((r = UNSAFE.getInt(t, SECONDARY)) != 0) {
            r ^= r << 13;   // xorshift
            r ^= r >>> 17;
            r ^= r << 5;
        }
        else if ((r = java.util.concurrent.ThreadLocalRandom.current().nextInt()) == 0){
            r = 1; // avoid zero
        }
        UNSAFE.putInt(t, SECONDARY, r);
        return r;
    }
}
