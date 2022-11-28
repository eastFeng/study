package com.dongfeng.study.basicstudy.java.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * <b> 线程的基本协作机制 </b>
 *
 * @author eastFeng
 * @date 2021-05-06 16:13
 */
public class BasicCooperationMechanism_6 {
    public static void main(String[] args) {
        /*
         * 多线程之间除了竞争访问同一个资源外，也经常需要相互协作的。
         * 怎么协作呢？ Java中多线程协作的基本机制是 wait/notify。
         *
         * 并发编程领域两大核心问题:
         * 【互斥】即同一时刻只允许一个线程访问共享资源。
         * 【同步】即线程之间如果通信、协作。
         *
         * 协作关键要想清楚协作的共享变量和条件是什么。
         */

        // 多线程协作场景
        cooperationScenic();

        // wait/notify
        waitAndNotify();

        // 生产者消费者模式
        producerAndConsumer();

        // 同时开始
//        sameTimeStart();
    }

    /**
     * 多线程协作场景
     */
    public static void cooperationScenic(){
        /*
         * 多线程之间需要协作的场景有很多，比如：
         *
         * 1）生产者/消费者协作模式：
         *   这是一种常见的协作模式，生产者线程和消费者线程通过共享队列进行协作，
         *   生产者将数据或任务放到队列上，而消费者从队列上取数据或任务，
         *   如果队列长度有限，在队列满的时候，生产者需要等待，而在队列为空的时候，消费者需要等待。
         *
         * 2）同时开始：
         *   类似运动员比赛，在听到比赛开始枪响后同时开始，在一些程序，尤其是模拟仿真程序中，要求多个线程能同时开始。
         *
         * 3）等待结束：
         *   主从协作模式也是一种常见的协作模式，主线程将任务分解为若干子任务，为每个子任务创建一个线程，
         *   主线程在继续执行其他任务之前需要等待每个子任务执行完毕。
         *
         * 4）异步结果：
         *   在主从协作模式中，主线程手工创建子线程的写法往往比较麻烦，一种常见的模式是将子线程的管理封装为异步调用，
         *   异步调用马上返回，但返回的不是最终的结果，而是一个一般称为Future的对象，通过它可以在随后获得最终的结果。
         *
         * 5）集合点：
         *   类似于学校或公司组团旅游，在旅游过程中有若干集合点，比如出发集合点，
         *   每个人从不同地方来到集合点，所有人到齐后进行下一项活动，
         *   在一些程序，比如并行迭代计算中，每个线程负责一部分计算，然后在集合点等待其他线程完成，
         */
    }

    /**
     * wait/notify
     */
    public static void waitAndNotify(){
        /*
         * Java的根父类是Object, Java在Object类而非Thread类中定义了一些线程协作的基本方法，使得每个对象都可以调用这些方法，
         * 这些方法有两类，一类是wait，另一类是notify。
         *
         * 主要有两个wait方法：
         * public final void wait() throws InterruptedException {
         *         wait(0);
         *     }
         * public final native void wait(long timeout) throws InterruptedException;
         * 一个带时间参数，单位是毫秒，表示最多等待这么长时间，参数为0表示无限期等待；
         * 一个不带时间参数，表示无限期等待，实际就是调用wait(0)。
         * 在等待期间都可以被中断，如果被中断，会抛出InterruptedException异常。
         *
         * wait实际上做了什么呢？它在等待什么？
         * 我们知道，每个对象都有一把锁和等待队列，一个线程在进入synchronized代码块时，会尝试获取锁，如果获取不到则会把当前线程加入等待队列中，
         * 其实，除了用于锁的等待队列，每个对象还有另一个等待队列，表示条件队列，该队列用于线程间的协作。
         * 调用wait方法就会把当前线程放到条件队列上并阻塞，表示当前线程执行不下去了，它需要等待一个条件，这个条件它自己改变不了，需要其他线程改变。
         * 当其他线程改变了条件后，应该调用Object的notify方法：
         * public final native void notify();
         * public final native void notifyAll();
         *
         * notify做的事情就是从条件队列中选一个线程，将其从队列中移除并唤醒，
         * notifyAll和notify的区别是，它会移除条件队列中所有的线程并全部唤醒。
         *
         * wait/notify方法只能在synchronized代码块内被调用，
         * 如果调用wait/notify方法时，当前线程没有持有对象锁，会抛出异常java.lang.IllegalMonitor-StateException。
         *
         * 如果wait必须被synchronized保护，那一个线程在wait时，
         * 另一个线程怎么可能调用同样被synchronized保护的notify方法呢？它不需要等待锁吗？
         * 我们需要进一步理解wait的内部过程，虽然是在synchronized方法内，但调用wait时，线程会释放对象锁。
         * wait的具体过程是：
         * 1. 把当前线程放入条件等待队列，释放对象锁，阻塞等待，线程状态变为WAITING或TIMED_WAITING。
         * 2. 等待时间到或被其他线程调用notify/notifyAll从条件队列中移除，这时，要重新竞争对象锁：
         *    2.1 如果能够获得锁，线程状态变为RUNNABLE，并从wait调用中返回。
         *    2.2 否则，该线程加入对象锁等待队列，线程状态变为BLOCKED，只有在获得锁后才会从wait调用中返回。
         *
         * 线程从wait调用中返回后，不代表其等待的条件就一定成立了，它需要重新检查其等待的条件，一般的调用模式是：
         * // synchronized (lock){
         * //      while (条件不成立){
         * //         lock.wait();
         * //      }
         * //     // 执行条件满足后的操作
         * //  }
         *
         * 调用notify会把在条件队列中等待的线程唤醒并从队列中移除，但它不会释放对象锁，
         * 也就是说，只有在包含notify的synchronized代码块执行完后，等待的线程才会从wait调用中返回。
         *
         * 简单总结一下，wait/notify方法看上去很简单，但往往难以理解wait等的到底是什么，而notify通知的又是什么，
         * 我们需要知道，【它们被不同的线程调用，但共享相同的锁和条件等待队列（相同对象的synchronized代码块内），
         * 它们围绕一个共享的条件变量进行协作】，这个条件变量是程序自己维护的，当条件不成立时，线程调用wait进入条件等待队列，
         * 另一个线程修改了条件变量后调用notify，调用wait的线程唤醒后需要重新检查条件变量。
         * 从多线程的角度看，它们围绕共享变量进行协作，从调用wait的线程角度看，它阻塞等待一个条件的成立。
         * 【我们在设计多线程协作时，需要想清楚协作的共享变量和条件是什么，这是协作的核心】。
         * 接下来，我们通过一些场景进一步理解wait/notify的应用。
         *
         * 只能有一个条件等待队列，这是Java wait/notify机制的局限性，这使得对于等待条件的分析变得复杂。
         */

        /*
         * 为什么wait和notify方法定义在Object中？而不是Thread类中？
         *
         * 一个很明显的原因是JAVA提供的锁是对象级的而不是线程级的，每个对象都有锁，通过线程获得。
         * 如果线程需要等待某些锁那么调用对象中的wait()方法就有意义了。
         * 如果wait()方法定义在Thread类中，线程正在等待的是哪个锁就不明显了。
         * 简单的说，由于wait，notify和notifyAll都是锁级别的操作，所以把他们定义在Object类中因为锁属于对象。
         */
    }

    /**
     * 生产者消费者模式
     */
    public static void producerAndConsumer(){
        /*
         * 在生产者/消费者模式中，协作的共享变量是队列，
         * 生产者往队列上放数据，如果满了就wait，而消费者从队列上取数据，如果队列为空也wait。
         */

        MyBlockQueue<Integer> myBlockQueue = new MyBlockQueue<>(10);

        Thread producerThread = new Thread() {
            int num = 0;
            @Override
            public void run() {
                for (int i=0; i<1000; i++) {
                    try {
                        myBlockQueue.put(num);
                        num++;
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread consumerThread = new Thread() {
            @Override
            public void run() {
                for (int i=0; i<1000; i++) {
                    try {
                        Integer take = myBlockQueue.take();
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 运行该程序，会看到生产者和消费者线程的输出交替出现。并且是先生产再消费。
        /*
         * 我们实现的MyBlockingQueue主要用于演示，Java提供了专门的阻塞队列实现，包括：
         * 1. 接口BlockingQueue和BlockingDeque。
         * 2. 基于数组的实现类ArrayBlockingQueue。
         * 3. 基于链表的实现类LinkedBlockingQueue和LinkedBlockingDeque。
         * 4. 基于堆的实现类PriorityBlockingQueue。
         */
    }

    /**
     * 生产者/消费者模式中队列作类。
     * <p> MyBlockingQueue是一个长度有限的队列，长度通过构造方法的参数进行传递，有两个方法：put和take。
     *    put是给生产者使用的，往队列上放数据，满了就wait，放完之后调用notifyAll，通知可能的消费者。
     *    take是给消费者使用的，从队列中取数据，如果为空就wait，取完之后调用notifyAll，通知可能的生产者。
     * <p> put和take都调用了wait方法，但它们的目的是不同的，或者说，它们等待的条件是不一样的，
     *     put等待的是队列不为满，而take等待的是队列不为空，但它们都会加入相同的条件等待队列。
     *     由于条件不同但又使用相同的等待队列，所以要调用notifyAll而不能调用notify，
     *     因为notify只能唤醒一个线程，如果唤醒的是同类线程就起不到协调的作用。
     *
     * @param <E> 队列元素类型
     */
    private static class MyBlockQueue<E>{
        // 多线程协作的共享变量
        private Queue<E> queue;
        private int limit;

        public MyBlockQueue(int limit){
            this.limit = limit;
            queue = new ArrayDeque<>(limit);
        }

        /**
         * <b> 往队列中添加元素 </b>
         * <p> 对实例方法，synchronized锁住(保护)的是当前实例对象(this)
         */
        public synchronized void put(E e) throws InterruptedException {
            // 当队列已满，添加元素的条件不成立，调用wait方法等待
            // 线程从wait调用中返回后，不代表其等待的条件就一定成立了，它需要重新检查其等待的条件(放在while循环中)
            while (queue.size() == limit){
                // wait方法：当前线程放入条件队列中，并且释放对象锁，线程状态变为WAITING
                wait();
            }

            // 走到这里，说明队列已经是不满的，添加元素的条件成立，可以往里面添加元素了
            queue.add(e);

//            System.out.println("producer put "+ e);
            System.out.println("生产者 生产 ---- "+ e);

            // 唤醒所有线程（主要是为了唤醒消费者线程从队列中取元素）
            notifyAll();
        }

        /**
         * 从队列中取元素
         * <p> 对实例方法，synchronized锁住(保护)的是当前实例对象(this)
         */
        public synchronized E take() throws InterruptedException {
            // 当队列为空，从队列中取元素的条件不成立，调用wait方法等待
            while (queue.isEmpty()){
                // wait方法：当前线程放入条件队列中，并且释放对象锁，线程状态变为WAITING
                wait();
            }

            // 到这里说明，队列已经不为空，取元素的条件成立
            E poll = queue.poll();

//            System.out.println("consumer take "+ poll);
            System.out.println("消费者 消费 "+ poll);

            // 唤醒所有线程（主要是为了唤醒生产者线程往队列中添加元素）
            notifyAll();
            return poll;
        }
    }

    /**
     * 同时开始
     */
    public static void sameTimeStart(){
        /*
         * 同时开始，类似于运动员比赛，在听到比赛开始枪响后同时开始，
         * 下面，我们模拟这个过程。
         * 这里，有一个主线程和N个子线程，每个子线程模拟一个运动员，主线程模拟裁判，
         * 它们协作的共享变量是一个开始信号。我们用一个类FireFlag来表示这个协作对象
         */

        FireFlag fireFlag = new FireFlag();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        int num = 10;
        Thread[] threads = new Thread[10];
        for (int i=0; i<num; i++){
            threads[i] = new Thread(){
                @Override
                public void run(){
                    try {
                        // 调用waitForFire()等待枪响
                        fireFlag.waitForFire();
                        System.out.println("start run "+ Thread.currentThread().getName() + " time:"+format.format(System.currentTimeMillis()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            threads[i].start();
        }

        // 主线程应该调用fire()发射比赛开始信号。
        fireFlag.fire();
    }

    /**
     * 子线程应该调用waitForFire()等待枪响，而主线程应该调用fire()发射比赛开始信号。
     */
    private static class FireFlag{
        private boolean fire = false;

        /**
         * 等待枪响
         */
        public synchronized void waitForFire() throws InterruptedException {
            // 当条件不成立时，等待
            while (!fire){
                wait();
            }
        }

        /**
         * 发射比赛开始信号
         */
        public synchronized void fire(){
            this.fire = true;
            notifyAll();
        }
    }


}
