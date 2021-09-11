package com.dongfeng.study;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.*;

/**
 * CyclicBarrier允许一组线程在到达某个栅栏互相等待，直到最后一个线程到达栅栏点，栅栏才会打开，处于阻塞状态的线程恢复继续执行。
 *
 * 就比如说我们在打王者的时候，十个人必须全部加载到100%，才可以开局。
 * 否则只要有一个人没有加载到100%,那这个游戏就不能开始。
 * 先加载完成的玩家必须等待最后一个玩家加载成功才可以。
 * @author eastFeng
 * @date 2021-01-04 16:49
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        // 定义玩家
        String[] heroes = {"孙悟空", "猪八戒", "甄姬", "虞姬", "明世隐"};

        // 使用线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i=0; i<5; i++){
            threadPool.execute(new Player(heroes[i], cyclicBarrier));
        }

        threadPool.shutdown();
    }

    public static class Player implements Runnable{
        private final String hero;
        private final CyclicBarrier barrier;

       public Player(String hero, CyclicBarrier barrier){
           this.hero = hero;
           this.barrier = barrier;
       }

        @Override
        public void run() {
            try {
                // 每一个英雄加载成功的时间不一样，用随机数
                int anInt = RandomUtil.randomInt(1, 5);
                long start = System.currentTimeMillis();
                TimeUnit.SECONDS.sleep(anInt);
                System.out.println(hero + "加载完成，加载时间为"+anInt+"秒，等待其他玩家加载完成");
                barrier.await();
                long end = System.currentTimeMillis();
                System.out.println(hero +": 看到所有玩家加载成功，比赛开始！ 总共用时："+ (end-start)/1000 +"秒");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
