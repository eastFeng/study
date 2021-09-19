package com.dongfeng.study;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;

import java.util.concurrent.*;

/**
 * @author eastFeng
 * @date 2020-12-23 22:07
 */
public class CountDownLatchDemo {

    /**
     * 用于聚合所有的统计指标 ,, 因为在多线程中使用，所以用ConcurrentHashMap
     */
    private static final ConcurrentHashMap<String, String> STATISTICS_MAP = new ConcurrentHashMap<>();
    /**
     * 创建计数器，这里需要统计4个指标，需要4个线程，所以计算器的值设为4
     */
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(4);

    public static void main(String[] args) {
//        statistics();
//        demo();
        twoCountDownLatch();
    }

    /**
     * 运营系统有统计报表、业务为统计每日的用户新增数量、订单数量、商品的总销量、总销售额......等多项指标统一展示出来，
     * 因为数据量比较大，统计指标涉及到的业务范围也比较多，所以这个统计报表的页面一直加载很慢，所以需要对统计报表这块性能需进行优化。
     *
     * <p>统计报表页面涉及到的统计指标数据比较多，每个指标需要单独的去查询统计数据库数据，单个指标只要几秒钟，
     * 但是页面的指标有10多个，所以整体下来页面渲染需要将近一分钟。
     *
     * <p>任务时间长是因为统计指标多，而且指标是串行的方式去进行统计的，我们只需要考虑把这些指标从串行化的执行方式改成并行的执行方式，
     * 那么整个页面的时间的渲染时间就会大大的缩短，如何让多个线程同步的执行任务，我们这里考虑使用多线程，
     * 每个查询任务单独创建一个线程去执行，这样每个统计指标就可以并行的处理了。
     *
     * <p>因为主线程需要每个线程的统计结果进行聚合，然后返回给前端渲染，
     * 所以这里需要提供一种机制让主线程等所有的子线程都执行完之后再对每个线程统计的指标进行聚合。
     * 这里我们使用CountDownLatch 来完成此功能。
     */
    public static void statistics(){
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        Thread countUserThread = new Thread(() -> {
            try {
                System.out.println("正在统计新增用户数量");
                // 该任务执行需要三秒
                TimeUnit.SECONDS.sleep(3);
                // 保存统计结果值
                STATISTICS_MAP.put("userNumber", "1000");
                System.out.println("统计新增用户数量完毕");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 已经完成一个任务，调用一次countDown方法
                COUNT_DOWN_LATCH.countDown();
            }
        });

        Thread countOrderThread = new Thread(() -> {
            try {
                System.out.println("正在统计订单数量");
                // 该任务执行需要三秒
                TimeUnit.SECONDS.sleep(3);
                // 保存统计结果值
                STATISTICS_MAP.put("countOrders", "3000");
                System.out.println("统计订单数量完毕");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 已经完成一个任务，调用一次countDown方法
                COUNT_DOWN_LATCH.countDown();
            }
        });

        Thread countGoodsThread = new Thread(() -> {
            try {
                System.out.println("正在统计商品销量");
                // 该任务执行需要三秒
                TimeUnit.SECONDS.sleep(3);
                // 保存统计结果值
                STATISTICS_MAP.put("countGoods", "5600");
                System.out.println("统计商品销量完毕");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 已经完成一个任务，调用一次countDown方法
                COUNT_DOWN_LATCH.countDown();
            }
        });

        Thread countMoneyThread = new Thread(() -> {
            try {
                System.out.println("正在统计总销售额");
                // 该任务执行需要三秒
                TimeUnit.SECONDS.sleep(3);
                // 保存统计结果值
                STATISTICS_MAP.put("countMoney", "123456789");
                System.out.println("统计总销售额完毕");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 已经完成一个任务，调用一次countDown方法
                COUNT_DOWN_LATCH.countDown();
            }
        });

        countUserThread.start();
        countOrderThread.start();
        countGoodsThread.start();
        countMoneyThread.start();

        try {
            // 阻塞当前线程, 直到计数器的值为0, 等待所有任务完成
            COUNT_DOWN_LATCH.await();
            long endTime = System.currentTimeMillis();
            System.out.println("------------统计指标全部完成--------------");
            System.out.println("统计结果为: "+ JSON.toJSONString(STATISTICS_MAP));
            System.out.println("任务执行时间为: "+ (endTime-startTime)/1000 +"秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void demo(){
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService pool = Executors.newFixedThreadPool(8);

        for (int i=0; i<6; i++){
            pool.execute(new WorkerRunnable(latch, i));
        }

        try {
            // 阻塞当前线程, 直到计数器的值为0 , 等待所有任务完成
            latch.await();
            long endTime = System.currentTimeMillis();
            System.out.println("time : "+ (endTime-startTime)/1000 + "秒");
            System.out.println("await finish=============================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用两个CountDownLatch
     *
     * <p> startC: new CountDownLatch(1);
     * <p> 多个个新开启的线程都调用了startC.await() 进行阻塞等待，它们阻塞在栅栏上，
     * 只有当条件满足的时候（startC.countDown()），它们才能同时通过这个栅栏，目的是让所有的线程站在一个起跑线上。
     */
    public static void twoCountDownLatch(){
        long startTime = System.currentTimeMillis();
        // 用来控制各个任务同时开始的计数器
        CountDownLatch startC = new CountDownLatch(1);
        // 做任务的计数器
        CountDownLatch doneC = new CountDownLatch(6);

        for (int i=0; i<6; i++){
            new Thread(new Worker(startC, doneC), "t"+i).start();
        }

        try {
            // 睡眠一秒，确保上面每个线程都启动起来
            TimeUnit.SECONDS.sleep(1);
            // 因为startC的计数器的值设置的是1:  所以调用一次 countDown方法 ,所有的await方法都可以通过了
            startC.countDown();
            // 阻塞当前线程, 直到计数器的值为0 , 等待所有任务完成
            doneC.await();
            long endTime = System.currentTimeMillis();
            // 花费的时间应该要减去上面主线程休眠的一秒的
            System.out.println("time : "+ (endTime-startTime)/1000 + "秒");
            System.out.println("await finish=============================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------static runnable class------------------------------------------------//

    public static class WorkerRunnable implements Runnable{
        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable(CountDownLatch countDownLatch, int i){
            doneSignal = countDownLatch;
            this.i = i;
        }

        @Override
        public void run() {
            try {
                // 做任务
                doWork(i);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 这个线程的任务完成了，调用countDown方法
                doneSignal.countDown();
            }
        }

        void doWork(int i) throws InterruptedException {
            System.out.println("第-- "+i+" --个任务 start");
            // 做任务需要的时间
            int i1 = RandomUtil.randomInt(1, 5);
            TimeUnit.SECONDS.sleep(i1);
            System.out.println("第-- "+i+" --个任务 end 执行时间"+i1+"秒");
        }
    }

    static class Worker implements Runnable{
        private final CountDownLatch startC;
        private final CountDownLatch doneC;

        Worker(CountDownLatch startC, CountDownLatch doneC){
            this.startC = startC;
            this.doneC = doneC;
        }

        @Override
        public void run() {
            try {
                // 为了让所有线程同时开始任务，我们让所有线程先阻塞在这里
                // 等大家都准备好了，再打开这个门栓
                startC.await();
                doWork();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 这个线程的任务完成了，调用countDown方法
                doneC.countDown();
            }
        }

        void doWork() throws InterruptedException {
            System.out.println("线程: "+Thread.currentThread().getName()+" start");
            //做任务需要的时间
            int i1 = RandomUtil.randomInt(1, 5);
            TimeUnit.SECONDS.sleep(i1);
            System.out.println("线程: "+Thread.currentThread().getName()+" end 执行时间"+i1+"秒");
        }
    }
}
