package com.dongfeng.study.basicstudy.java.thread;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

/**
 * @author eastFeng
 * @date 2020-09-17 0:06
 */
@Slf4j(topic = "----")
public class CommonMethod {
    public static void main(String[] args) {
//        startAndRun();
//        sleep();
//        sleep_interrupt();
//        timeunit_sleep();
//        test();
//        join_test();
//        test_2();
        multiThreadJoin();
//        joinWithTimeout();
//        interrupt();
//        interrupt1();
    }


    // -------------------------------Thread method-----------------------------------
    /**
     * start:
     * <ol>
     * <li> 开启一个新线程，在新的线程运行run方法中的代码
     * <li> start方法只是让线程进入就绪，里面的代码不一定立刻运行(CPU的时间片还没分给它)
     * <li> 每个线程的start方法只能调用一次，如果调用多次会抛出IllegalThreadStateException异常
     * </ol>

     *
     * getState: 获取线程状态
     * <p> Java语言一共定义了6种状态，在任意一个时间点，一个线程只能有且只有其中的一个状态
     * <ol>
     * <li>          NEW : 创建后尚未启动
     * <li>     RUNNABLE : 有可能正在执行，也有可能正在等待着操作系统为它分配执行时间
     * <li>      BLOCKED : 阻塞状态，没有获取锁。当前线程不能获得锁的时候，它会加入等待队列等待，线程的状态会变为BLOCKED。
     * <li>     WAITING : 无限期等待，这种状态的线程不会被分配处理器执行时间，他们要等待被其他线程显示的唤醒。
     * <li> TIMED_WAITING : 限期等待，这种状态的线程也不会被分配处理器执行时间，不过无须等待被其他线程显示唤醒，在一定时间之后它们会由操作系统自动唤醒
     * <li>   TERMINATED : 线程已经结束执行
     */
    public static void startAndRun(){
        Thread t1 = new Thread(() -> log.info("【running...】"));

        log.info("【start方法之前 state:{}】", t1.getState());
        t1.start();
        log.info("【start方法之后 state:{}】", t1.getState());

        // 直接调用run方法不会开启新线程 : 还是在main线程中执行
//        t1.run();
//        t1.start();
    }


    /**
     * sleep : Thread类的静态方法
     * <ol>
     * <li> 调用sleep方法会让【当前线程】从Running进入Timed Waiting状态，当前线程暂停执行指定的时间，让出cpu給其他线程---->哪个线程调用，哪个线程休眠
     * <li> 其他线程可以使用interrupt方法打断正在睡眠的线程，这时sleep方法会抛出InterruptedException异常
     * <li> 睡眠结束后的线程未必会立刻得到执行(等待CPU分配时间片给它)
     * <li> 建议使用{@link TimeUnit#sleep(long)}方法代替{@link Thread#sleep(long)}来获得更好的可读性
     * </ol>
     *
     * yield :
     * <ol>
     * <li> 调用yield方法会让当前线程从Running状态进入Runnable状态，然后调度执行其他同优先级的线程。
     *      如果这是没有其他同优先级的线程，那么不能保证让当前线程暂停的效果
     * <li> 具体的实现依赖于操作系统的任务调度器
     * </ol>
     */
    public static void sleep(){
        Thread t2 = new Thread(() -> {
            try {
                // t2线程调用,t2休眠
                Thread.sleep(2000);
                // TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");

        log.info("【start方法之前 t2.state:{}】", t2.getState());
        t2.start();
        log.info("【start方法之后 t2.state:{}】", t2.getState());

        try {
            // 主线程(被main方法调用)调用，主线程休眠
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("【t2休眠之后 t2.state:{}】", t2.getState());
    }

    /**
     * sleep打断演示
     */
    public static void sleep_interrupt(){
        Thread t1 = new Thread(() -> {
            try {
                log.info("【enter sleep...】");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.info("【wake up...】");
                e.printStackTrace();
            }
        });

        t1.start();
        try {
            //主线程休眠
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("【interrupt...】");
        // 主线程休眠结束 打断正在休眠的t1线程
        t1.interrupt();
    }

    /**
     * TimeUnit的sleep方法
     */
    public static void timeunit_sleep(){
        log.info("【sleep start...】");
        try {
            // 可读性更好
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("【sleep end...】");
    }

    /**
     * setPriority: 设置线程优先级
     * <ol>
     * <li> 线程优先级会提示调度器优先调度该线程，但它仅仅是个提示，调度器可以忽略它
     * <li> 如果CPU比较忙，那么优先级高的线程会获得更多的时间片，但CPU闲的时候，优先级几乎没什么用
     * </ol>
     */
    public static void yieldAndPriority(){
        // 这个方法只需要测试短短几秒，就能看到效果

        Thread t1 = new Thread(() -> {
            int count = 0;
            for (; ;) {
                System.out.println("---->1 " + count++);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            int count = 0;
            for (; ;) {
//                Thread.yield();
                System.out.println("             ---->2 "+count++);
            }
        }, "t2");

//        t1.setPriority(Thread.MIN_PRIORITY);
//        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }

    static int r = 0;
    public static void test(){
        log.info("【 main 开始...】");
        Thread t1 = new Thread(() -> {
            log.info("【t1 开始...】");
            try {
                log.info("【t1 休眠 sleep 开始...】");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("【t1 休眠 sleep 技术...】");
            r = 10;
            log.info("【t1 结束】");
        }, "t1");
        t1.start();

        // 打印出来的r是0
        log.info("【main线程中r结果为: {}】", r);
        log.info("【main 结束】");

        // 原因：因为主线程和线程t1是并行执行的，t1线程需要1秒之后才能把10赋值给r
        // 而主线程一开始就要打印r的结果，所以只能打印出r=0
    }

    /**
     * join: 等待线程运行结束(Waits for this thread to die)----> 等待调用这个方法的线程执行结束
     * <p> 比如线程A，哪个线程调用A的join方法，哪个线程就等待线程A执行完
     * <p> 用join方法解决上面test方法中的问题
     */
    public static void join_test(){
        log.info("【main线程 开始...】");
        Thread t1 = new Thread(() -> {
            log.info("【t1线程 开始...】");
            try {
                log.info("【t1线程 休眠 sleep 开始...】");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("【t1线程 休眠 sleep 技术...】");
            r = 10;
            log.info("【t1线程 结束】");
        }, "t1");
        t1.start();
        try {
            log.info("【main线程 调用 t1线程的join方法...】");
            // 主线程调用t1的join方法，主线程等待t1运行结束
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // r: 10
        log.info("【main线程中r结果为: {}】", r);
        log.info("【main线程 结束】");
    }

    /**
     * 主线程在调用子线程的star()方法之前给r赋值
     */
    public static void test_2(){
        log.info("【main线程 开始...】");
        Thread t = new Thread(() -> {
            log.info("【t1线程 开始...】");
            try {
                log.info("【t1 休眠 sleep 开始...】");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("【t1 休眠 sleep 技术...】");
            r = 10;
        }, "t1");

        r = 2;
        t.start();

        // r: 2
        log.info("【main线程中r的结果为: {}】", r);
        log.info("【main线程 结束】");
    }


    /**
     * 等待两个线程运行结束
     */
    static int r1 = 0;
    static int r2 = 0;
    public static void multiThreadJoin(){
        Thread t1 = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 20;
        }, "t2");

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        log.info("【main线程 调用join方法 begin...】");
        try {
            t1.join();
            log.info("【main线程 调用t1线程的join end...】");
            t2.join();
            log.info("【main线程 调用t2线程的join end...】");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("main线程中 r1:{} r2:{} cost:{}", r1, r2, end-start);
    }

    /**
     * join(long millis): 等待线程运行结束，【最多】等待millis毫秒
     * <p> 【最多】：
     * <ol>
     * <li> 如果已经等了millis毫秒线程还没有结束，就不会继续等待了
     * <li> 如果还没有等够millis毫秒线程就结束了，也不会继续等了
     * </ol>
     */
    public static void joinWithTimeout(){
        // 第一种情况
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        }, "t1");
        long start1 = System.currentTimeMillis();
        t1.start();
        try {
            // 最多等待t1 2秒
            t1.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end1 = System.currentTimeMillis();
        log.info("【r1:{} cost:{}】", r1, end1-start1);


        // 第二种情况
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 20;
        }, "t2");
        long start2 = System.currentTimeMillis();
        t2.start();
        try {
            // 最多等待t2 3秒
            t2.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end2 = System.currentTimeMillis();
        log.info("【r2:{} cost:{}】", r2, end2-start2);
    }

    /**
     * 线程睡眠状态被打断之后会抛出异常，并且打断标记(isInterrupted)会变
     */
    public static void interrupt(){
        Thread t1 = new Thread(() -> {
            try {
                log.info("sleep...");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        log.info("【打断之前 t1打断标记:{}】", t1.isInterrupted());
        try {
            // 主线程睡眠是为了 等t1睡眠之后再打断它(t1)
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        log.info("【打断之后 t1打断标记:{}】", t1.isInterrupted());
    }

    /**
     * 打断正常执行的线程不会抛出异常，但是打断标记会改变
     */
    public static void interrupt1(){
        Thread t1 = new Thread(() -> {
            while (true) {
                // 利用打断标记优雅地退出循环，结束线程
                if (Thread.currentThread().isInterrupted()) {
                    log.info("【被打断了，退出循环】");
                    break;
                }
                log.info("running...");
            }
        });

        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("interrupt---");
        t1.interrupt();
    }
}
