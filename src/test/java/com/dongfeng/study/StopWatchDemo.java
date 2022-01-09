package com.dongfeng.study;

import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author eastFeng
 * @date 2022-01-09 13:33
 */
public class StopWatchDemo {
    public static void main(String[] args) {

        // 记录时间戳 直观输出速度消耗的百分比

        try {
            StopWatch sw = new StopWatch("startTest");
            sw.start("step 1");
            TimeUnit.SECONDS.sleep(2);
            sw.stop();

            sw.start("step 2");
            TimeUnit.SECONDS.sleep(4);
            sw.stop();
            System.out.println(sw.prettyPrint());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
