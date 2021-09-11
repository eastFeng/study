package com.dongfeng.test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.google.common.collect.Lists;
import com.sun.xml.bind.v2.model.core.ID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.sql.In;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author eastFeng
 * @date 2020-11-16 13:54
 */
@Slf4j(topic = "------")
public class CheckTest {
    private static final ReentrantLock lock = new ReentrantLock();
    //Condition依赖于Lock产生
    private static final Condition notFull = lock.newCondition();
    private static final Condition notEmpty = lock.newCondition();
    private static final Object[] items = new Object[100];
    int putptr, takeptr, count;


    public static void main(String[] args) {
        String objectId = "000000000000000000000000";
        System.out.println(objectId.length());

        List<Integer> integerList = new ArrayList<>();
        List<Integer> asList = Arrays.asList(1, 2, 3);
        //List<Integer> 转为 List<String>
        List<String> transform = Lists.transform(integerList, item -> {
            return "" + item;
        });
        System.out.println(transform.size());
        transform.forEach(System.out::print);

        CountDownLatch countDownLatch = new CountDownLatch(1);
    }

    @Test
    public void base64Test(){
        String encodeStr = "MUNGMjA1QjEwNDIyRDg4Mg==";

        String decodeStr = Base64.decodeStr(encodeStr);
        System.out.println(decodeStr);

        int length = decodeStr.length();
        System.out.println(length);

        //substring: 开始下标包括，结束下标不包括

        String passwordPrefix = decodeStr.substring(length-3, length);
        System.out.println("passwordPrefix: "+passwordPrefix);
        String passwordSuffix = decodeStr.substring(0, length-3);
        System.out.println("passwordSuffix: "+passwordSuffix);

        System.out.println(passwordPrefix+passwordSuffix);
    }

    @Test
    public void stringToCharArray(){
        String str1 = "abcdefghijklmnopqrstuvwsyz";
        char[] chars1 = str1.toCharArray();
        System.out.println("chars1.length: "+chars1.length);
        for (int i=0; i<chars1.length; i++){
            System.out.print(chars1[i]);
        }

        System.out.println();

        String str2 = "我爱你中国";
        char[] chars2 = str2.toCharArray();
        System.out.println("chars2.length: "+chars2.length);
    }

    @Test
    public void equalTest(){
        String str = "140104196102202260";
        System.out.println(str.equals("140104196102202260"));
    }


    @Test
    public void conditionTest(){
    }

    //生产
    public void put(Object o)throws InterruptedException{
        //在使用Condition的时候，必须先持有相应的锁。
        lock.lock();   // 1. 获取锁
        try {
            while (count == items.length){
                //队列已满，等待，直到not full才能继续生产
                notFull.await();    //2. 释放锁---->获取锁, 释放锁到获取锁的过程中, 其他线程（消费线程）可以获取锁 进行消费
            }
            items[putptr] = o;
            if (++putptr == items.length){
                putptr = 0;
            }
            ++count;
            //生产成功，队列已经not empty
            notEmpty.signalAll(); // 唤醒其他所有线程
        } finally {
            lock.unlock();
        }
    }

    //消费
    public Object take() throws InterruptedException {
        lock.unlock();
        try {
            while (count == 0){
                //队列为空，等待，直到not empty才能继续消费
                notEmpty.await();
            }
            Object item = items[takeptr];
            if (++takeptr == items.length){
                takeptr = 0;
            }
            --count;
            //消费掉一个，队列已经not full
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void threadLocalTest(){
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        ThreadLocal<Integer> threadLocal2 = new ThreadLocal<>();

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

    //身份证校验
    @Test
    public void isValidCard(){
        String idCard = "312231198911120439";

        boolean validCard = IdcardUtil.isValidCard(idCard);
        log.info("身份证号码：{}，是否有效：{}", idCard, validCard);

        String province = IdcardUtil.getProvinceByIdCard(idCard);
        log.info("省份：{}", province);
    }

    public void validator(){
        String email = "460373645@qq.com";
        boolean isEmail = Validator.isEmail(email);
        log.info("邮箱：{}，是否正确：{}", email, isEmail);
    }

    @Test
    public void test(){
        String hide = IdcardUtil.hide("110101200003071052", 12, 18);
        System.out.println(hide);
    }

    @Test
    public void casTest(){
        RSA rsa = SecureUtil.rsa();
    }


    @Test
    public void valueOfTest(){
        //
//        Integer integer = Integer.valueOf("123");
//
//        Long aLong = Long.valueOf("12345667889");

        // -128 ~ 127
//        Byte aByte = Byte.valueOf("889");

        Map<String, String> map = new HashMap<>();


        Integer integer = Integer.valueOf("889");
        System.out.println(integer);
    }
}
