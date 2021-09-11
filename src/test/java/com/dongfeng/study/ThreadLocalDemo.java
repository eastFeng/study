package com.dongfeng.study;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author eastFeng
 * @date 2021-01-21 16:19
 */
@Slf4j
public class ThreadLocalDemo {
    public static void main(String[] args) {
        threadLocalTest();
    }

    public static void threadLocalTest(){
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        ThreadLocal<Integer> threadLocal2 = new ThreadLocal<>();

        ThreadLocal<String> threadLocal1 = ThreadLocal.withInitial(() -> {
            return "sss";
        });

        // 每个线程都有个ThreadLocal.ThreadLocalMap对象
        // ThreadLocalMap对象可以存放该线程的多个局部(本地)变量 --> 存放在了Entry数组中

        IntStream.range(0, 5).forEach(a->
                new Thread(()->{
                    threadLocal.set(a + " -- "+ RandomUtil.randomInt(0, 10));
                    threadLocal2.set(a);
                    log.info("线程和local的值分别是:{}", threadLocal.get());
                    log.info("threadLocal2:{}", threadLocal2.get());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start()
        );
    }
}
