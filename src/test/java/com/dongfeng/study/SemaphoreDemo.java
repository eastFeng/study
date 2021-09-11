package com.dongfeng.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author eastFeng
 * @date 2021-01-26 17:37
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        test1();
    }


    public static void test1(){
        // 使用线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        Semaphore semaphore = new Semaphore(5);
        for (int i=0; i<20; i++){
            int finalI = i;
            threadPool.execute(()->{
                try {
//                    int availablePermits = semaphore.availablePermits();
//                    if (availablePermits > 0){
//
//                    }
                    semaphore.acquire();
                    System.out.println("获得许可证 : "+ finalI);
                    TimeUnit.SECONDS.sleep(3);
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("发生InterruptedException异常 : "+ finalI);
                }
            });
        }

        threadPool.shutdown();
    }
}
