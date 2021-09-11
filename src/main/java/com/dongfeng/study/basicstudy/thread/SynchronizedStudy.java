package com.dongfeng.study.basicstudy.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <b> 理解synchronized </b>
 * @author eastFeng
 * @date 2020-09-18 23:00
 */
@Slf4j(topic = "----")
public class SynchronizedStudy {
    // 普通对象锁
    private final Object LOCK = new Object();
    // 静态对象锁
    private static final Object STATIC_LOCK = new Object();

    public static void main(String[] args) {
        // synchronized原理
        basicConcept();

//        aQuestion();

//        for (int i=0; i<100; i++){
//            questionDeal();
//        }

//        thread_unsafe();
        thread_localVariable();
    }

    /**
     * synchronized基本原理
     */
    public static void basicConcept(){
        /*
         * 共享内存有两个重要问题，一个是竞态条件，另一个是内存可见性，解决这两个问题的一个方案是使用synchronized关键字。
         *
         * synchronized可以用于修饰类的实例方法、静态方法和代码块。
         * 对实例方法，synchronized锁住(保护)的是当前实例对象(this)
         * 对静态方法，synchronized锁住(保护)的是类对象(Class)，实际上，每个对象都有一个锁和一个等待队列，类对象也不例外。
         * 对代码块，synchronized同步的对象可以是任意对象，任意对象都有一个锁和等待队列，或者说，任何对象都可以作为锁对象。
         *
         *
         * synchronized锁住(保护)的是对象，而不是方法体里面的代码，线程只有获取synchronize锁住(保护)的对象才能进入方法体。
         * 只要访问的是同一个对象的synchronized方法，即使是不同的代码，也会被同步顺序访问。
         * 比如，对于一个类Counter中的两个实例方法getCount和incr，
         * 对同一个Counter对象，一个线程执行getCount，另一个执行incr，它们是不能同时执行的，会被synchronized同步顺序执行。
         *
         * 执行synchronized方法的过程大致如下：
         * 1）尝试获得锁，如果能够获得锁，继续下一步，否则加入等待队列，阻塞并等待唤醒。
         * 2）执行方法体代码。
         * 3）释放锁，如果等待队列上有等待的线程，从中取一个并唤醒，如果有多个等待的线程，唤醒哪一个是不一定的，不保证公平性。
         *
         * 线程不能获得锁的时候，它会加入等待队列等待，线程的状态会变为BLOCKED。
         *
         * 需要说明的是，synchronized方法不能防止非synchronized方法被同时执行，
         * 所以，一般在保护变量时，需要在所有访问该变量的方法上加上synchronized。
         *
         * 1．可重入性
         * synchronized有一个重要的特征，它是可重入的，也就是说，
         * 对同一个执行线程，它在获得了锁之后，在调用其他需要同样锁的代码时，可以直接调用。
         *
         * 可重入是通过记录锁的持有线程和持有数量来实现的，当调用被synchronized保护的代码时，
         * 检查对象是否已被锁，如果是，再检查是否被当前线程锁定，如果是，增加持有数量，
         * 如果不是被当前线程锁定，才加入等待队列，当释放锁时，减少持有数量，当数量变为0时才释放整个锁。
         *
         * 2．内存可见性
         * 对于复杂一些的操作，synchronized可以实现原子操作，避免出现竞态条件。
         * synchronized除了保证原子操作外，它还有一个重要的作用，就是保证内存可见性，
         * 在释放锁时，所有写入都会写回内存，而获得锁后，都会从内存中读最新数据。
         * 不过，如果只是为了保证内存可见性，使用synchronized的成本有点高，有一个更轻量级的方式，那就是给变量加修饰符volatile。
         *
         * 3．死锁
         * 使用synchronized或者其他锁，要注意死锁。
         * 所谓死锁就是类似这种现象，比如，有a、b两个线程，a持有锁A，在等待锁B，而b持有锁B，在等待锁A,
         * a和b陷入了【互相等待，最后谁都执行不下去】。
         *
         * 怎么避免死锁呢？
         * 首先，应该尽量避免在持有一个锁的同时去申请另一个锁，
         * 如果确实需要多个锁，所有代码都应该按照相同的顺序去申请锁。
         * 比如，对于上面的例子，可以约定都先申请lockA，再申请lockB。
         */
    }


    /**
     * <p> 1. 多个线程之间存在共享资源
     * <p> 2. 多个线程操作共享资源---->读写操作(只读取是没问题的)
     *
     * <p> 一段代码块内如果存在对【共享资源】的多线程【读写】操作，称这段代码块为【临界区】
     * 多个线程在临界区内执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了【竞态条件】
     */
    static int n = 0;
    public static void aQuestion(){
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                n++; //临界区
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                n--; //临界区
            }
        });

        t1.start();
        t2.start();

        try {
            // 主线程等待线程t1和t2执行完
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("n: {}", n);
    }

    /**
     * 为了避免临界区的竞态条件发生，有多种手段可以达到目：
     * <p> 1. 阻塞式的解决方案: synchronized, Lock
     * <p> 2. 非阻塞式的解决方案: 原子变量
     *
     * <p> synchronized俗称【对象锁】，它锁住的是对象，它采用【互斥】的方式让同一时刻最多只有一个线程获取对象锁，其他线程再想获取这个对象锁就会被阻塞住，
     * 这样就可以保证用有锁的线程可以安全的执行临界区内的代码，不用担心线程上下文切换
     *
     * <p> synchronized使用【对象锁】保证了【临界区内代码的原子性】
     */
    public static void questionDeal(){
        Object obj = new Object();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (obj){
                    n++; //临界区
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (obj){
                    n--; //临界区
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("n: {}", n);
    }



    public static void synchronized_1(){
        List<String> aList = new ArrayList<>();
    }

    /**
     * 局部变量是线程安全的  线程私有，没有暴露给外部------> 线程安全
     *
     * <p> 局部变量引用的是对象，，则不一定
     */
    // list 是外部对象 非线程安全
    static List<String> list = new ArrayList<>(1);
    public static void thread_unsafe(){
        for (int i=0; i<2; ++i){
            new Thread(()->{
                for (int j=0; j<200; ++j){
                    list.add("1");
                    list.remove(0);
                }
            }).start();
        }
    }

    public static void thread_localVariable(){
        for (int i=0; i<3; ++i){
            new Thread(()->{
                //list是局部变量  线程安全
                List<String> list = new ArrayList<>(1);
                for (int j=0; j<200; ++j){
                    list.add("1");
                    list.remove(0);
                }
            }).start();
        }
    }

    /**
     * 常见的线程安全类:
     * <p> String, Integer, StringBuffer, Random, Vector, HashTable, java.util.concurrent包下的类
     *
     * <p> 这里说他们是线程安全指的是，多个线程调用它们同一个实例的某一个方法时，是线程安全的。
     * 也可以理解为：它们的每个方法是原子的
     *
     * <p> 【注意】: 它们多个方法的组合不是原子的 ，，就像cpu指令一样，单个指令是原子的，但是多个指令组合在一起就不是原子的了。
     */


    public static void wait_1(){
        String lock = "lock";
        try {
            // 导致当前线程等待，直到另一个线程调用此对象的notify()方法或notifyAll()方法。
            // 当前线程必须拥有该对象的监视器。该线程释放该监视器的所有权 --------------> 释放锁
            // 并等待另一个线程通过调用notify方法或notifyAll方法来通知等待该对象监视器的线程。
            // 然后线程等待，直到它可以重新获得监视器的所有权并继续执行。
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
