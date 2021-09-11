package com.dongfeng.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author eastFeng
 * @date 2021-01-27 10:08
 */
public class PhaserDemo {
    public static void main(String[] args) {

    }

    public static void test(){
        // 使用线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        Phaser phaser = new Phaser();
        // 1. 注册一个 party
        phaser.register();

        for (int i = 0; i < 10; i++) {

            phaser.register();

            threadPool.execute(() -> {
                // 2. 每个线程到这里进行阻塞，等待所有线程到达栅栏
                phaser.arriveAndAwaitAdvance();

                // doWork()
            });
        }
        phaser.arriveAndAwaitAdvance();
    }
}
