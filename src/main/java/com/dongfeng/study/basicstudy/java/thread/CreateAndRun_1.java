package com.dongfeng.study.basicstudy.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author eastFeng
 * @date 2020-09-16 22:21
 */
@Slf4j(topic = "----")
public class CreateAndRun_1 {
    public static void main(String[] args) {
//        way1();
//        way2();
//        way3();
//        way4();
        try {
            way5();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        watchThread();
    }

    /**
     * 方式一：创建Thread类的匿名内部类
     */
    public static void way1(){
        // 通过匿名内部类方法创建线程对象
        Thread t1 = new Thread(){
          @Override
          public void run(){
              // 要执行的任务
              log.info("【t1 running】");
          }
        };

        // 设置线程名字
        t1.setName("t1");
        // 调用start方法启动线程
        t1.start();
        log.info("【{}】", t1.getName());
    }

    /**
     * 方法二：使用Runnable配合Thread
     * <p> 把【线程】和【任务(要执行的代码)】分开
     * <p> Thread代表线程
     * <p> Runnable代表可运行的任务(线程要执行的代码)
     * 优点：
     * <ol>
     * <li> 用Runnable更容易与线程池等高级API结合
     * <li> 用Runnable让任务类脱离了Thread继承体系，更灵活
     * </ol>
     */
    public static void way2(){
        // 通过匿名内部类方式创建Runnable接口对象
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                log.info("【running】");
            }
        };

        Thread t2 = new Thread(task2, "t2");
        t2.start();
        log.info("【{}】", t2.getName());
    }

    /**
     * lambda表达式写法
     */
    public static void way3(){
        Runnable task3 = ()-> log.info("【running】");

        Thread t3 = new Thread(task3, "t3");
        t3.start();
        log.info("【{}】", t3.getName());
    }

    /**
     * lambda 更精简写法
     */
    public static void way4(){
        Thread t4 = new Thread(()->log.info("【running】"), "t4");

        t4.start();
        log.info("【{}】", t4.getName());

        // 或者
        new Thread(()->log.info("【running】"), "t5").start();
    }


    /**
     * FutureTask
     */
    public static void way5() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("【running...】");
                Thread.sleep(1000);
                return null;
            }
        });

        Thread t5 = new Thread(futureTask, "t5");
        t5.start();

        // 获取返回值
        Integer integer = futureTask.get();
        log.info("【futureTask返回值: {}】", integer);
    }

    //--------------------------------

    /***
     * 观察线程运行现象
     */
    public static void watchThread(){
        new Thread(()->{
            int i = 0;
            while (i<=1000){
                i++;
                log.info("running...");
            }
        }).start();

        new Thread(()->{
            int i = 0;
            while (i<=1000){
                i++;
                log.info("【running---】");
            }
        }).start();
    }

    //-------------------------------------------------------------------------------------------
    /*
     * windows下查看进程和线程
     * 1. 任务管理器可以用来查看进程和线程，也可以用来杀死进程；
     *
     * 2. cmd中：
     *      tasklist : 查看进程
     *      taskkill : 杀死进程
     *           jsp : 查看所有java进程
     *
     */
}
