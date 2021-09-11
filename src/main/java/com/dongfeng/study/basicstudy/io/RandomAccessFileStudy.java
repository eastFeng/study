package com.dongfeng.study.basicstudy.io;

/**
 * <b> {@link java.io.RandomAccessFile} </b>
 *
 * @author eastFeng
 * @date 2021-05-01 22:00
 */
public class RandomAccessFileStudy {
    public static void main(String[] args) {
        /*
         * 字节流和字符流，它们都是以流的方式读写文件，流的方式有几个限制：
         * 1. 要么读，要么写，不能同时读和写。
         * 2. 不能随机读写，只能从头读到尾，且不能重复读，虽然通过缓冲可以实现部分重读，但是有限制。
         * Java中还有一个类RandomAccessFile，它没有这两个限制，既可以读，也可以写，还可以随机读写，是一个更接近于操作系统API的封装类。
         *
         * 随机读写文件
         */
    }
}
