package com.dongfeng.study.basicstudy.thread;

import lombok.extern.slf4j.Slf4j;

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
            lock.unlock();
        }
    }
}
