package com.dongfeng.study;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;

/**
 * 位移运算符
 *
 * @author eastFeng
 * @date 2021-01-20 15:01
 */
public class ShiftBitwiseOperators {
    // 位移运算符 : 位移位运算符是将数据看成二进制数，对其进行向左或向右移动若干位的运算。
    // 位移位运算符分为左移和右移两种，均为双目运算符。第一运算对象是移位对象，第二个运算对象是所移的二进制位数。

    // <<< (左移运算符) : 将一个数的各二进制位全部左移若干位，移动的位数由右操作数指定，右操作数必须是非负值，其右边空出的位用0填补，高位左移溢出则舍弃该高位。
    // 左移一位相当于该数乘以2，左移2位相当于该数乘以2^2=4。但此结论只适用于该数左移时被溢出舍弃的高位中不包含1的情况。
    //

    // >> (带符号右移运算符) : 按二进制形式把所有的数字向右移动对应的位数，低位移出(舍弃)，高位的空位补符号位，即正数补零，负数补1。
    // 右移一位相当于除2，右移n位相当于除以2的n次方。这里是取商哈，余数就不要了。

    // >>> (无符号右移运算符) : 按二进制形式把所有的数字向右移动对应位数，低位移出(舍弃)，高位的空位补零。对于正数来说和带符号右移相同，对于负数来说不同。


    public static void main(String[] args) {
//        left(-3, 2);
//        right(-11, 2);

        System.out.println(1<<16);

        ThreadUtil.execute(()->{
            System.out.println("Hhh");
        });
    }


    public static void left(int num, int x){
        System.out.println(num + "二进制: " + NumberUtil.getBinaryStr(num));
        System.out.println(num + "左移"+x+"位 : " + (num<<x));
        System.out.println(num + "左移之后的二进制: " + NumberUtil.getBinaryStr(num<<x));
    }

    public static void right(int num, int x){
        System.out.println(num + "二进制: " + NumberUtil.getBinaryStr(num));
        System.out.println(num + "右移"+x+"位 : " + (num>>x));
        System.out.println(num + "右移之后的二进制: " + NumberUtil.getBinaryStr(num>>x));
    }

    public static void unsignedRight(int num, int x){
        System.out.println(num + "二进制: " + NumberUtil.getBinaryStr(num));
        System.out.println(num + "无符号右移"+x+"位 : " + (num>>>x));
        System.out.println(num + "无符号右移之后的二进制: " + NumberUtil.getBinaryStr(num>>>x));
    }
}
