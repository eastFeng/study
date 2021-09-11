package com.dongfeng.test;

import java.util.concurrent.TimeUnit;

/**
 * @author eastFeng
 * @date 2020-12-22 13:54
 */
public class ThreadTest {
    public static void main(String[] args) {
        newThread();
    }


    public static void newThread(){
        Thread t1 = new Thread(() -> {
            try {
                //休眠2秒
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(Thread.currentThread());
            System.out.println("after 2 second sleep t1 state : "+Thread.currentThread().getState());
        }, "t1");

        System.out.println("before start t1 state : "+t1.getState());
        t1.start();
        System.out.println("after start t1 state : "+t1.getState());

        try {
            //主线程休眠1秒
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main sleep 1 second, t1 state : "+t1.getState());
    }
}
