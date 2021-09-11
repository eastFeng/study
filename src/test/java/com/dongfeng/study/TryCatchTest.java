package com.dongfeng.study;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author eastFeng
 * @date 2021-01-04 14:20
 */
public class TryCatchTest {
    public static void main(String[] args) {
//        try {
//            System.out.println("try do...");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("finally do...");
//        }

        /*
         * finally内的代码不管有无异常都会执行 ：
         * 1. 如果（try里面）没有异常发生，在try内的代码执行结束后执行
         * 2. 如果异常且被catch捕获，在catch内的代码执行结束之后执行
         * 3. 如果有异常发生但没有捕获，则在异常被抛给上层之前执行
         *
         * 由于finally的这个特点，它一般用于释放资源，如数据库连接、文件流等。
         *
         * finally语句有一个执行细节，如果在try或者catch语句内有return语句，
         * 则return语句在finally语句执行结束后才执行，但finally并不能改变返回值。
         *
         * 如果在finally中也有return语句呢？
         * try和catch内的return会丢失，实际会返回finally中的返回值。
         * finally中有return不仅会覆盖try和catch内的返回值，还会掩盖try和catch内的异常，就像异常没有发生一样。
         *
         * finally中，如果finally中抛出了异常，则原异常也会被掩盖。
         *
         * 一般而言，为避免混淆，应该避免在finally中使用return语句或者抛出异常，
         * 如果finally中调用的其他代码可能抛出异常，则应该捕获异常并进行处理。
         */

//        System.out.println(division(0));
        System.out.println(division2(1000));
//        System.out.println(division3());
//        division4();

        // try-with-resources
        try {
            tryWithResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tryWithResource() throws Exception {
        /*
         * 对于一些使用资源的场景，比如文件和数据库连接，典型的使用流程是首先打开资源，最后在finally语句中调用资源的关闭方法，
         * 针对这种场景，Java 7开始支持一种新的语法，称之为try-with-resources，这种语法针对实现了java.lang.AutoCloseable接口的对象。
         */

        // 没用try-with-resources时：
        FileInputStream stream = new FileInputStream("hello");

        try {
            // 使用资源
            int read = stream.read();
        }finally {
            stream.close();
        }

        // 用try-with-resources语法，形式如下：
        // 资源r的声明和初始化放在try语句内，不用再调用finally，在语句执行完try语句后，会自动调用资源的close()方法。
        // 资源可以定义多个，以分号分隔。
        try (FileInputStream r = new FileInputStream("hello")
             ; FileInputStream f = new FileInputStream("hello")) {
            // 使用资源
            int read = r.read();
            int read1 = f.read();
        }
    }

    public static int division(int i){
        try {
            return 6 / i;
        } finally {
            // try里面如果有异常，会先执行finally，然后再抛出异常给上层
            System.out.println("finally do...");
        }
    }

    /**
     * 如果在try或者catch语句内有return语句，则return语句在finally语句执行结束之后才执行，
     * 但finally并不能改变返回值。
     */
    public static int division2(int i){
        int ret = i;
        try {
            // 在执行这个return语句之前，会先将返回值ret保存在一个临时变量中，然后才执行finally语句
            // finally中对ret的修改不会被返回
            return ret;
        } finally {
            System.out.println("finally do...");
            ret = 2;
        }
    }

    /**
     * 如果在 finally 中也有 return 语句，则 try和catch 内的 return 语句会丢失，实际会返回 finally 中的返回值。
     * finally 中的 return 不仅会覆盖 try和catch 内的返回值，还会覆盖 try和catch 内的异常，就像异常没有发生一样。
     */
    public static int division3(){
        int ret = 0;
        try {
            System.out.println("try do...");
            int a = 5/0;
            return ret;
        } finally {
            System.out.println("finally do...");
            return 2;
        }
    }


    /**
     * finally中不仅return语句会掩盖异常，如果finally中抛出了异常，则原异常（try和catch中的异常）就会被覆盖。
     * 所以，为了避免混淆，应该避免在finally中使用return语句或者抛出异常。
     */
    public static void division4(){
        int ret = 0;
        try {
            System.out.println("try do...");
            int a = 5/0;
        } finally {
            System.out.println("finally do...");
            // 该RuntimeException异常会 覆盖 ArithmeticException异常，ArithmeticException异常就丢失了
            throw new RuntimeException("hello!!!");
        }
    }

}
