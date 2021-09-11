package com.dongfeng.study.basicstudy;

import java.util.concurrent.TimeUnit;

/**
 * <b> {@link StringBuilder} 学习 </b>
 *
 * @author eastFeng
 * @date 2021-04-23 14:22
 */
public class StringBuilderStudy {
    public static void main(String[] args) {
        /*
         * 如果字符串修改操作比较频繁，应该采用StringBuilder和StringBuffer类，
         * 这两个类的方法基本是完全一样的，它们的实现代码也几乎一样，
         * 唯一的不同就在于StringBuffer类是线程安全的，而StringBuilder类不是。
         */

        // StringBuilder基本实现原理
        basicImplementPrinciple();

        // String的+和+=运算符
        stringPlus();

        // StringBuilder线程不安全演示
        stringBuilderNotSafeDemo();
    }

    /**
     * StringBuilder基本实现原理
     */
    public static void basicImplementPrinciple(){

        /*
         * 与String类似，StringBuilder类也封装了一个字符数组，定义如下：
         * char[] value;
         * 与String不同，它不是final的，可以修改。
         *
         * 另外，与String不同，字符数组中不一定所有位置都已经被使用，它有一个实例变量，表示数组中已经使用的字符个数，定义如下：
         * int count;
         *
         * StringBuilder继承自AbstractStringBuilder，它的默认构造方法是：
         * public StringBuilder() {
         *     super(16);
         * }
         * 调用父类的构造方法，父类对应的构造方法是：
         * AbstractStringBuilder(int capacity) {
         *     value = new char[capacity];
         * }
         * 也就是说，new StringBuilder()代码内部会创建一个长度为16的字符数组，count的默认值为0。
         *
         * 来看append方法的代码：
         * public AbstractStringBuilder append(String str) {
         *    if (str == null)
         *         return appendNull();
         *
         *    int len = str.length();
         *    // 会确保数组的长度足以容纳新添加的字符，
         *    // 如果长度不够就会分配一个足够长度的新数组，然后将原内容复制到这个新数组中，最后让内部的字符数组指向这个新数组
         *    ensureCapacityInternal(count + len);
         *    str.getChars(0, len, value, count);
         *    // 实际使用的长度用count体现
         *    count += len;
         *    return this;
         * }
         *
         * ensureCapacityInternal方法：
         * private void ensureCapacityInternal(int minimumCapacity) {
         *         if (minimumCapacity - value.length > 0) {
         *             // 现有char数组长度不够才会扩展
         *             value = Arrays.copyOf(value,
         *                     newCapacity(minimumCapacity));
         *         }
         *    }
         * newCapacity方法：
         * private int newCapacity(int minCapacity) {
         *         // 这里的扩展策略是跟当前长度相关的，当前长度乘以2，再加上2，如果这个长度不够最小需要的长度，才用minimumCapacity。
         *         // 为什么要加2？这样，在原长度为0时也可以一样工作。
         *         int newCapacity = (value.length << 1) + 2;
         *         if (newCapacity - minCapacity < 0) {
         *             newCapacity = minCapacity;
         *         }
         *         return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
         *             ? hugeCapacity(minCapacity)
         *             : newCapacity;
         *     }
         *
         * 不过，如果预先就知道需要多长，那么可以调用StringBuilder的另外一个构造方法：
         * public StringBuilder(int capacity) {
         *         super(capacity);
         *     }
         *
         *  toString方法的代码：
         *  public String toString() {
         *         // Create a copy, don't share the array
         *         // 基于内部char数组value新建了一个String。
         *         // 注意，这个String构造方法不会直接用value数组，而会新建一个，以保证String的不可变性。
         *         return new String(value, 0, count);
         *     }
         *
         *
         */

        /*
         * StringBuilder的插入方法：在指定索引offset处插入字符串str
         * 原来的字符后移，offset为0表示在开头插，为length()表示在结尾插。
         *
         * 实现思路是：
         * 在确保有足够长度后，首先将原数组中offset开始的内容向后挪动n个位置，
         * n为待插入字符串的长度，然后将待插入字符串复制进offset位置。
         */

        // 插入方法实例
        StringBuilder sb = new StringBuilder();
        sb.append("hh");
        sb.insert(0, "ttt");
        System.out.println(sb.toString());
    }

    /**
     * String的+和+=运算符
     */
    public static void stringPlus(){
        // Java中，String可以直接使用+和+=运算符，这是Java编译器提供的支持，
        // 背后，Java编译器一般会生成StringBuilder, +和+=操作会转换为append。

        // 比如：
        String hello = "hello";
        hello += ",world";
        System.out.println(hello);
        // Java编译器一般会转换为：
        StringBuilder builder = new StringBuilder("hello");
        builder.append(",world");
        System.out.println(builder.toString());

        // 既然直接使用+和+=就相当于使用StringBuilder和append，那还有什么必要直接使用StringBuilder呢？
        // 在简单的情况下，确实没必要。不过，在稍微复杂的情况下，Java编译器可能没有那么智能，它可能会生成过多的StringBuilder。
        String hello1 = "hello";
        for (int i=0; i<3; i++){
            hello1 += ",world";
        }
        System.out.println(hello1);
        // Java编译器转换后的代码大致如下所示：
        String hello2 = "hello";
        for (int i=0; i<3; i++){
            StringBuilder sb = new StringBuilder(hello2);
            sb.append(",world");
            hello2 = sb.toString();
        }
        System.out.println(hello2);

        // 所以，对于简单的情况，可以直接使用String的+和+=，对于复杂的情况，尤其是有循环的时候，应该直接使用StringBuilder。
    }



    /**
     * StringBuilder线程不安全演示
     */
    public static void stringBuilderNotSafeDemo(){
        StringBuilder stringBuilder = new StringBuilder();
        // 创建十个线程
        for (int i=0; i<10; i++){
            // 每个线程对stringBuilder进行1000次append操作
            new Thread(() -> {
                for (int j=0; j<1000; j++){
                    stringBuilder.append("a");
                }
            }).start();
        }

        try {
            // 主线程睡眠5秒，确保创建的10个线程都执行完
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 大多数情况都要少于10000
        System.out.println(stringBuilder.length());
    }
}
