package com.dongfeng.study.basicstudy.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Java SDK并发包中通过Lock和Condition两个接口来实现管程，其中Lock用户解决互斥问题，Condition用户解决同步问题。
 *
 * @author eastFeng
 * @date 2020-11-05 16:31
 */
@Slf4j(topic = "------")
public class LockAndCondition {
    public static void main(String[] args) {

        // 原子变量和CAS
        atomicAndCAS();


    }

    public static void atomicAndCAS(){
        /*
         * Java并发包中的基本原子变量类型有以下几种。
         * AtomicBoolean：原子Boolean类型，常用来在程序中表示一个标志位。
         * AtomicInteger：原子Integer类型。
         * AtomicLong：原子Long类型，常用来在程序中生成唯一序列号。
         * AtomicReference：原子引用类型，用来以原子方式更新复杂类型。
         *
         * 之所以称为原子变量，是因为它包含一些以原子方式实现组合操作的方法
         */

        AtomicInteger atomicInteger = new java.util.concurrent.atomic.AtomicInteger(1);
        /*
         * public final boolean compareAndSet(int expect, int update)
         *
         * compareAndSet是一个非常重要的方法，比较并设置，我们以后将简称为CAS。
         * 该方法有两个参数expect和update，以原子方式实现了如下功能：
         * 如果当前值等于expect，则更新为update，否则不更新，如果更新成功，返回true，否则返回false。
         */

        /*
         * 与synchronized锁相比，这种原子更新方式代表一种不同的思维方式。
         *
         * synchronized是悲观的，它假定更新很可能冲突，所以先获取锁，得到锁后才更新。
         *
         * 原子变量的更新逻辑是乐观的，它假定冲突比较少，但使用CAS更新，也就是进行冲突检测，
         * 如果确实冲突了，那也没关系，继续尝试就好了。
         *
         * synchronized代表一种阻塞式算法，得不到锁的时候，进入锁等待队列，等待其他线程唤醒，有上下文切换开销。
         *
         * 原子变量的更新逻辑是非阻塞式的，更新冲突的时候，它就重试，不会阻塞，不会有上下文切换开销。
         * 对于大部分比较简单的操作，无论是在低并发还是高并发情况下，这种乐观非阻塞方式的性能都远高于悲观阻塞式方式。
         *
         * CAS是Java并发包的基础，基于它可以实现高效的、乐观、非阻塞式数据结构和算法，
         * 它也是并发包中锁、同步工具和各种容器的基础。
         */

    }


    private final Lock lock = new ReentrantLock();
    int value;

    public void addOne(){
        // 获取锁
        lock.lock();
        try {
            value += 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
